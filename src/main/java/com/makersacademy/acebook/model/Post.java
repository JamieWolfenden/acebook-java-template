package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Imports NotBlank

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Post cannot be empty.") // adding validation to prevent blank posts.
    private String content;
    private LocalDate date = LocalDate.now(); //today's date yyyy/mm/dd
    private Integer likeCount = 0; //defaults to 0

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments;

    public Post() {}

    public Post(String content, LocalDate date, Integer likeCount) {
        this.content = content;
        this.date = date;
        this.likeCount = likeCount;
    }
}
