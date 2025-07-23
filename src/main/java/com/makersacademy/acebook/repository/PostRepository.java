package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.model.Post;

import java.util.List;


public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findByUser(User user);

}
