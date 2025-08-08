package com.example.reservation_app.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reservation_app.model.User;

// Userエンティティを対象にし、主キーの型がLongであることを指定
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
  Optional<User> findByUsername(String username); // ユーザー名でユーザーを検索するメソッド

  boolean existsByUsername(String username); // ユーザー名が既に存在するか確認するメソッド

}
