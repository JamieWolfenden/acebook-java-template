package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDate date = LocalDate.now();

    public Comment() {}

    public Comment(String commentContent, LocalDate date) {
        this.commentContent = commentContent;
        this.date = date;
    }

    public String getTimeAgo() {
        long days = ChronoUnit.DAYS.between(this.date, LocalDate.now());

        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        return days + " days ago";
    }

}
