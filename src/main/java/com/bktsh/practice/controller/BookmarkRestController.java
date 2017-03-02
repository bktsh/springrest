package com.bktsh.practice.controller;

import com.bktsh.practice.dao.BookmarkRepository;
import com.bktsh.practice.dao.UserRepository;
import com.bktsh.practice.exception.UserNotFoundException;
import com.bktsh.practice.model.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017-Feb-22
 */
@RestController
@RequestMapping("/{username}/bookmarks")
class BookmarkRestController {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @Autowired
    BookmarkRestController(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Resources<BookmarkResource> readBookmarks(@PathVariable String username) {
        this.validateUser(username);
        List<BookmarkResource> bookmarkResourceList = this.bookmarkRepository
                .findByUserUsername(username)
                .stream().map(BookmarkResource::new)
                .collect(Collectors.toList());

        return new Resources<>(bookmarkResourceList);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String username, @RequestBody Bookmark input) {
        this.validateUser(username);
        return this.userRepository.findByUsername(username).map(user -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(user,input.getUri(), input.getDescription()));
                    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
                    return ResponseEntity.created(location).build();
                }).orElse(ResponseEntity.noContent().build());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{bookmarkId}")
    BookmarkResource readBookmark(@PathVariable String username, @PathVariable Long bookmarkId) {
        this.validateUser(username);
        return new BookmarkResource(this.bookmarkRepository.findOne(bookmarkId));
    }

    private void validateUser(String username) {
        this.userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}