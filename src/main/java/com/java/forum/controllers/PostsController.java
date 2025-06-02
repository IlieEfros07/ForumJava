package com.java.forum.controllers;

import com.java.forum.models.Post;
import com.java.forum.services.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController{

  @Autowired
  private PostRepository repo;

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

}