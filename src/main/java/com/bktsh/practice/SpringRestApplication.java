package com.bktsh.practice;

import com.bktsh.practice.dao.BookmarkRepository;
import com.bktsh.practice.dao.TodoRepository;
import com.bktsh.practice.dao.UserRepository;
import com.bktsh.practice.model.Bookmark;
import com.bktsh.practice.model.Todo;
import com.bktsh.practice.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           TodoRepository todoRepository,
                           BookmarkRepository bookmarkRepository) {
        return (evt) -> Arrays.asList(
                "Hashem, Baktash".split(","))
                .forEach(user -> {
                    User u = userRepository.save(new User(user, "password"));
                    bookmarkRepository.save(new Bookmark(u, "http://bookmark.com/1/" + u, "A description"));
                    bookmarkRepository.save(new Bookmark(u, "http://bookmark.com/2/" + u, "A description"));
                    todoRepository.save(new Todo(u, "1. FirstTodo/" + u));
                    todoRepository.save(new Todo(u, "2. SecondTodo/" + u));
                });
    }
}
