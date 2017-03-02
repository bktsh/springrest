package com.bktsh.practice.dao;

import com.bktsh.practice.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Created on 2017-Feb-22
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
    Collection<Todo> findByUserUsername(String username);
}
