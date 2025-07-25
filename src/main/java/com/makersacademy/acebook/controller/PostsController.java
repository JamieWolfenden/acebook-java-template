package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Comment;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.CommentRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {

    @Autowired
    PostRepository postsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentsRepository;

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = postsRepository.findAllByOrderByIdDesc();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
//        model.addAttribute("image", "");

        Optional<User> user = getCurrentUser();
        user.ifPresent(value -> model.addAttribute("user", value));

        return "posts/index";
    }

    @PostMapping("/posts")
    public String create(
            @Valid @ModelAttribute("post") Post post, // Triggers Validation
            @RequestParam("image") MultipartFile image,
            BindingResult result, // Hold Error
            Model model // Used to pass data back to view
    ) {
        if (result.hasErrors()) {
            // Return to form and display existing posts + error
            Iterable<Post> posts = postsRepository.findAll();
            model.addAttribute("posts", posts);
            model.addAttribute("errorMessage", "Post content cannot be empty.");
            return "posts/index"; // error page
        }

        Optional<User> user = getCurrentUser();

        user.ifPresent(post::setUser);

        postsRepository.save(post);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}")
    public String likePost(@PathVariable Long id) {
        Optional<Post> likedPost = postsRepository.findById(id);

        if (likedPost.isPresent()) {
            Optional<User> currentUser = getCurrentUser();

            if (currentUser.isPresent()) {
                User user = currentUser.get();
                Post post = likedPost.get();

                // Check if user has already liked the post
                if (!user.getLikedPosts().contains(post)) {
                    post.setLikeCount(post.getLikeCount() + 1);

                    post.getLikes().add(user);
                    user.getLikedPosts().add(post);
                    userRepository.save(user);
                }
            }
        }

        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPostAndComments(@PathVariable Long id, Model model) {
        Post post = postsRepository.findById(id).orElseThrow();
        Iterable<Comment> comments = commentsRepository.findByPost(post); // assuming this method exists
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment());
        model.addAttribute("user", getCurrentUser().get());
        System.out.println(model);

        return "posts/post-comments"; // html template
    }

    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Long id, @ModelAttribute("comment") Comment comment) {
        Post post = postsRepository.findById(id).orElseThrow();
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (String) principal.getAttributes().get("email");
        User user = userRepository.findUserByUsername(username).orElseThrow();

        comment.setPost(post);
        comment.setUser(user);
        comment.setId(null);// added in as per error re. no user set
        commentsRepository.save(comment);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/posts/search")
    public ModelAndView searchPosts(@ModelAttribute("q") String searchedText) {
        ModelAndView modelAndView = new ModelAndView("/posts/search");

        List<Post> allPosts = postsRepository.findAll();
        ArrayList<Post> matchingPosts = new ArrayList<>();

        for (Post post : allPosts) {
            if (post.getContent().toLowerCase().contains(searchedText.toLowerCase())) {
                matchingPosts.add(post);
            }
        }

        modelAndView.addObject("posts", matchingPosts);
        Optional<User> user = getCurrentUser();
        user.ifPresent(value -> modelAndView.addObject("user", value));

        return modelAndView;
    }
  
    public Optional<User> getCurrentUser() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        return userRepository.findUserByUsername(username);


    }
}
