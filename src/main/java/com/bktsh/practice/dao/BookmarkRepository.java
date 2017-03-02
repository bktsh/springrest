package com.bktsh.practice.dao;

import com.bktsh.practice.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created on 2017-Feb-22
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Collection<Bookmark> findByUserUsername(String username);
}
