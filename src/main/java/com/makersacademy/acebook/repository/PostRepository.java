package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Iterable<Post> findAllByOrderByIdAsc();
    Iterable<Post> findAllByOrderByIdDesc();
    List<Post> findByUser(User user);
}
