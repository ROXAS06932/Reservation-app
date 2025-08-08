package com.example.reservation_app.service;

// import java.io.ObjectInputFilter.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import com.example.reservation_app.model.SignupForm;
import com.example.reservation_app.model.User;
import com.example.reservation_app.repository.UserRepository;
import com.example.reservation_app.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder
                      ){
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void registerNewUser(SignupForm form){
    User user = new User();
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setEmail(form.getEmail());
    user.setPhoneNumber(form.getPhoneNumber());
    user.setRole("USER");
    user.setEnabled(true);

    userRepository.save(user);
  }

  public void saveAndLogin(SignupForm form, HttpServletRequest request){
    User user = new User();
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword())); //パスワードをハッシュ化
    user.setEmail(form.getEmail());
    user.setPhoneNumber(form.getPhoneNumber());
    user.setRole("USER"); //デフォルトで一般ユーザーとして登録
    user.setEnabled(true);

    userRepository.save(user);

    // 自動ログイン処理
    CustomUserDetails userDetails = new CustomUserDetails(user);
    UsernamePasswordAuthenticationToken authToken = 
      new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  public boolean existsByUsername(String username){
    return userRepository.existsByUsername(username);
  }

  public boolean hasRegisteredUsers(){
    return userRepository.count() > 0;
  }
}
