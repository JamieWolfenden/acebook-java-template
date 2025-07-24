package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import static java.lang.Boolean.TRUE;

@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private boolean enabled;
    private String profilePicture;


    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> likedPosts;

    public User() {
        this.enabled = TRUE;
    }

    public User(String username) {
        this.username = username;
        this.enabled = TRUE;
        this.profilePicture = "/images/placeholder_pp.webp";
    }

    public User(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }
}
