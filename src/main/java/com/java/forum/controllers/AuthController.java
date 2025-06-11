package com.java.forum.controllers;

import com.java.forum.models.User;
import com.java.forum.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Controller
@RequestMapping("/auth")
public class AuthController {



  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @GetMapping("/login")
  public String login(Model model){
    model.addAttribute("user", new User());
    return "auth/login";
  }

  @GetMapping("/register")
  public String register(Model model){
    model.addAttribute("user", new User());
    return "auth/register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute("user") User user){
    if(userRepo.findByUsername(user.getUsername()).isPresent()){
      return "redirect:/auth/login";
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepo.save(user);
    return "redirect:/auth/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("user") User user) {
    Optional<User> optionalUser = userRepo.findByUsername(user.getUsername());

    if (optionalUser.isPresent() && passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
      return "/posts/index";
    }
    return "redirect:/auth/login";
  }
}

