package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {

    @Autowired
    PostRepository repository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = repository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @PostMapping("/posts")
    public String create(
            @Valid @ModelAttribute("post") Post post, // Triggers Validation
            BindingResult result, // Hold Error
            Model model // Used to pass data back to view
    ) {
        if (result.hasErrors()) {
            // Return to form and display existing posts + error
            Iterable<Post> posts = repository.findAll();
            model.addAttribute("posts", posts);
            model.addAttribute("errorMessage", "Post content cannot be empty.");
            return "posts/index"; // error page
        }
        
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        Optional<User> user = userRepository.findUserByUsername(username);

        post.setUser(user.get());

        repository.save(post);
        return "redirect:/posts";
    }

//    @PostMapping("/posts")
//    public RedirectView create(@ModelAttribute Post post) {
//        repository.save(post);
//        return new RedirectView("/posts");
//    }
}
