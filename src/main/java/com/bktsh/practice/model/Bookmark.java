package com.bktsh.practice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created on 2017-Feb-22
 */
@Entity
public class Bookmark {
    @Id
    @GeneratedValue
    Long id;

    String uri;

    String description;

    @JsonIgnore
    @ManyToOne
    User user;

    public Bookmark() {
    }

    public Bookmark(User user, String uri, String description) {
        this.user = user;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
