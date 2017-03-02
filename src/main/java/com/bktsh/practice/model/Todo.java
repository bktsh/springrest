package com.bktsh.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created on 2017-Feb-22
 */
@Entity
public class Todo {

    @Id
    @GeneratedValue
    Long id;

    String description;

    @JsonIgnore
    @ManyToOne
    User user;

    public Todo() {//jps only
    }

    public Todo(User user, String description) {
        this.user = user;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
