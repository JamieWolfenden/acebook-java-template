package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Imports NotBlank

import lombok.Data;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Post cannot be empty.") // adding validation to prevent blank posts.
    private String content;

    public Post() {}

    public Post(String content) {
        this.content = content;
    }
}
