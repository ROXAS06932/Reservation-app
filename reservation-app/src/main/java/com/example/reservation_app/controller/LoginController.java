package com.example.reservation_app.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.reservation_app.model.LoginForm;
import com.example.reservation_app.model.SignupForm;
import com.example.reservation_app.service.UserService;


@Controller
public class LoginController {

    private UserService userService;

    private UserDetailsService userDetailsService;

    @GetMapping("/")
    public String redirectToLogin(){
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login"; // templates/login.html を表示
    }
    
    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("signupForm", new SignupForm());
        return "redirect:/home"; // templates/register.htmlに分離
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute SignupForm signupForm){
        //１、ユーザー登録処理
        userService.registerNewUser(signupForm);

        // ２、登録したユーザー情報を取得
        UserDetails userDetails = userDetailsService.loadUserByUsername(signupForm.getUsername());

        // ３、承認トークンを作成してセッションに設定
        UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // ４、ログイン済みとして /home に遷移
        return "redirect:/home";
    }
}

