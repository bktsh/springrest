package com.bktsh.practice.controller;

import com.bktsh.practice.dao.TodoRepository;
import com.bktsh.practice.dao.UserRepository;
import com.bktsh.practice.exception.UserNotFoundException;
import com.bktsh.practice.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

/**
 * Created on 2017-Feb-22
 */
@RestController
@RequestMapping("/{username}/todos")
class TodoRestController {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    TodoRestController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Todo> readTodos(@PathVariable String username) {
        this.validateUser(username);
        return this.todoRepository.findByUserUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String username, @RequestBody Todo input) {
        this.validateUser(username);
        return this.userRepository.findByUsername(username).map(user -> {
                    Todo result = todoRepository.save(new Todo(user,input.getDescription()));
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
                    return ResponseEntity.created(location).build();
                }).orElse(ResponseEntity.noContent().build());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{todoId}")
    Todo readTodo(@PathVariable String username, @PathVariable Long todoId) {
        this.validateUser(username);
        return this.todoRepository.findOne(todoId);
    }

    private void validateUser(String username) {
        this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}