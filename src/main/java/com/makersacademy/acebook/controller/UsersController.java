package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        userRepository
                .findUserByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username)));

        return new RedirectView("/posts");
    }

    @GetMapping("/profile/my-profile")
    public ModelAndView myProfile() {
        ModelAndView modelAndView = new ModelAndView("profile/my-profile");

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isPresent()) {
            User currentUser = user.get();
            modelAndView.addObject("user", currentUser);

            List<Post> posts = postRepository.findByUser(currentUser);
            modelAndView.addObject("posts", posts);

            int totalLikes = 0;

            for (Post post : posts) {
                totalLikes += post.getLikeCount();
            }

            modelAndView.addObject("totalLikes", totalLikes);
        }

        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView viewProfile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/profile/other-profile");

        Optional<User> otherUser = userRepository.findById(id);

        if (otherUser.isPresent()) {
            User viewedUser = otherUser.get();
            modelAndView.addObject("viewedUser", viewedUser);

            List<Post> posts = postRepository.findByUser(viewedUser);
            modelAndView.addObject("posts", posts);

            int totalLikes = 0;

            for (Post post : posts) {
                totalLikes += post.getLikeCount();
            }

            modelAndView.addObject("totalLikes", totalLikes);
        }

        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        Optional<User> user = userRepository.findUserByUsername(username);

        user.ifPresent(currentUser -> modelAndView.addObject("user", currentUser));

        return modelAndView;
    }
}
