package com.java.forum.controllers;

import com.java.forum.models.Post;
import com.java.forum.models.User;
import com.java.forum.services.PostRepository;
import com.java.forum.services.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController{

  @Autowired
  private PostRepository repo;

  @Autowired
  private UserRepository userRepository;
  @GetMapping({"","/"})
  public String getPosts(Model model){
    List<Post> post = repo.findAll();
    model.addAttribute("posts", post);
    return "posts/index";
  }

  @GetMapping("/{id}")
  public String getPost(Model model, @PathVariable("id") Integer id){
    if (id == null){
      return "redirect:/posts";
    }
    return repo.findById(id)
            .map(post -> {
              model.addAttribute("post", post);
              return "posts/show";
            }).orElseGet(() -> {
              return "redirect:/posts";
            });
  }

    @GetMapping("/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute("post") Post post, HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            model.addAttribute("error", "Title is required");
            return "posts/new";
        }

        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            model.addAttribute("error", "Content is required");
            return "posts/new";
        }

        try {

            User author = userRepository.findById(1).orElse(null);
            if (author == null) {
                model.addAttribute("error", "Author not found");
                return "posts/new";
            }

            post.setAuthor(author);
            post.setCreatedAt(LocalDateTime.now());
            post.setTitle(post.getTitle().trim());
            post.setContent(post.getContent().trim());

            Post savedPost = repo.save(post);
            return "redirect:/posts/" + savedPost.getId();

        } catch (Exception e) {
            model.addAttribute("error", "Error creating post");
            return "posts/new";
        }
    }
}