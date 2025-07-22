package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDate date = LocalDate.now(); //today's date yyyy/mm/dd
    private Integer likeCount = 0; //defaults to 0

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public Post() {}

    public Post(String content, LocalDate date, Integer likeCount) {
        this.content = content;
        this.date = date;
        this.likeCount = likeCount;
    }
}
