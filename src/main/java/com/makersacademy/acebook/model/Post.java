package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Imports NotBlank

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
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

    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> likes;

    public Post() {}

    public Post(String content, LocalDate date, Integer likeCount) {
        this.content = content;
        this.date = date;
        this.likeCount = likeCount;
    }
}
