package com.bktsh.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 2017-Feb-22
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    Long id;

    @JsonIgnore
    public String password;

    public String username;

    @OneToMany(mappedBy = "user")
    Set<Todo> todos = new HashSet<>();

    @OneToMany(mappedBy = "user")
    Set<Bookmark> bookmarks = new HashSet<>();

    public User() { // jpa only
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Todo> getTodos() {
        return todos;
    }

    public void setTodos(Set<Todo> todos) {
        this.todos = todos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Set<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
