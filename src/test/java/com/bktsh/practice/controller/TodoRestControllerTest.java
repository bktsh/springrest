package com.bktsh.practice.controller;

import com.bktsh.practice.SpringRestApplication;
import com.bktsh.practice.dao.BookmarkRepository;
import com.bktsh.practice.dao.TodoRepository;
import com.bktsh.practice.dao.UserRepository;
import com.bktsh.practice.model.Bookmark;
import com.bktsh.practice.model.Todo;
import com.bktsh.practice.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created on 2017-Feb-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRestApplication.class)
@WebAppConfiguration
public class TodoRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String userName = "George";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private User user;

    private List<Todo> todoList = new ArrayList<>();

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.todoRepository.deleteAllInBatch();
        this.bookmarkRepository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();

        this.user = userRepository.save(new User(userName, "password"));
        this.todoList.add(todoRepository.save(new Todo(user, "1. A todo description")));
        this.todoList.add(todoRepository.save(new Todo(user, "2. A todo description")));
    }

    @Test
    public void readTodos() throws Exception {
        mockMvc.perform(get("/" + userName + "/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.todoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].description", is("1. A todo description")))
                .andExpect(jsonPath("$[1].id", is(this.todoList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].description", is("2. A todo description")));
    }

    @Test
    public void add() throws Exception {
        String bookmarkJson = json(new Todo(this.user, "http://spring.io"));
        this.mockMvc.perform(post("/" + userName + "/todos")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void readTodo() throws Exception {
        mockMvc.perform(get("/" + userName + "/todos/"
                + this.todoList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.todoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.description", is("1. A todo description")));
    }

    @Test
    public void userNotFound() throws Exception {
        mockMvc.perform(post("/george/bookmarks/")
                .content(this.json(new Todo()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}