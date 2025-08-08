package com.example.reservation_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.reservation_app.model.SignupForm;
import com.example.reservation_app.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class SignupController {

  private final UserService userService;

  public SignupController(UserService userService){
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegisterForm(Model model){
    model.addAttribute("signupForm", new SignupForm());
    model.addAttribute("hasUsers", userService.hasRegisteredUsers());
    return "register";
  }

  @PostMapping("/register")
  public String processRegister(@Valid @ModelAttribute("signupForm") SignupForm form, BindingResult result, Model model, HttpServletRequest request){
    
    // 登録処理（重複チェックなど）
    if(userService.existsByUsername(form.getUsername())){
      result.rejectValue("username", "duplicate", "そのユーザー名は既に使われております");
      return "register";
    }
    
    if(!form.getPassword().equals(form.getConfirmPassword())){
      result.rejectValue("confirmPassword", "error.confirmPassword", "パスワードが一致しません");
    }
    
    if(result.hasErrors()){
      model.addAttribute("hasUsers", userService.hasRegisteredUsers());
      return "register";
    }

    userService.saveAndLogin(form, request);
    return "redirect:/reservations";
  }
}
