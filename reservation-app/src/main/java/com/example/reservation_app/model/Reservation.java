package com.example.reservation_app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity //EntityはこのクラスがDBのデーブルに対応することを示す
public class Reservation {

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    //@IDと@GeneratedValueは、IDフィールドが自動生成されることを示す（主キーと自動採番の設定）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    //予約情報のフィールド
    //例: 名前、メールアドレス、日付、時間など
    //必要に応じてフィールドを追加
    // バリデーション処理を行うために、フィールドにアノテーションを追加
    private Long id;

    @NotBlank(message = "名前は必須です")
    @Size(min = 2, max = 50, message = "2文字以上50文字以内で入力してください")
    private String name;
    
    @NotBlank(message = "電話番号は必須です")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "有効な電話番号を入力してください")
    private String phoneNumber;

    @NotBlank(message = "メールアドレスは必須です")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "有効なメールアドレスを入力してください")
    private String email;

    @Future(message = "予約日時は未来の日付を選んでくささい")
    @NotNull(message = "予約日時は必須です")
    private LocalDateTime reservationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    //ゲッター・セッター
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public ReservationStatus getStatus() {
        return status;
    }
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public LocalDateTime getReservationTime(){return reservationTime;}
    public void setReservationTime(LocalDateTime reservationTime){this.reservationTime = reservationTime;}

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
