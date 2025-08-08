package com.example.reservation_app.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.reservation_app.model.Reservation;
import com.example.reservation_app.model.User;
import com.example.reservation_app.repository.ReservationRepository;
import com.example.reservation_app.repository.UserRepository;
import com.example.reservation_app.service.ReservationService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;



// 動作の流れ
// フォームから送信　→ submitForm()が呼ばれる
// reservationRepository.save()によってDBに保存
// 確認画面に予約内容が表示される
// 確認画面から戻ると、再度フォームが表示される
// 予約内容を変更して再度送信すると、同じ予約内容がDBに保存される


@Controller
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository; // これを使うことで、DBにアクセスできるようになる

    @Autowired
    private UserRepository userRepository;

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
      this.reservationService = reservationService;
    }

    @GetMapping("/reserve") // ユーザーが最初にフォーム入力画面にアクセスするためのエントリーポイント
    public String showForm(Model model){
      model.addAttribute("reservation", new Reservation());
      return "reserve"; // templates/reserve.html を表示  
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservations/all") // 予約一覧を表示するためのエンドポイント(管理者用)
    public String showReservations(Model model){
      List<Reservation> reservations = new ArrayList<>();
      reservationRepository.findAll().forEach(reservations::add);
      model.addAttribute("reservations", reservations);
      return "list"; // templates/list.html を表示
    }

    @GetMapping("/reservations")  // ログインユーザーの予約一覧を表示するためのエンドポイント
    public String showUserReservations(Model model){
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      User user = userRepository.findByUsername(username).orElseThrow();
      List<Reservation> reservations = reservationRepository.findByUser(user); // ログインユーザーの予約を取得
      model.addAttribute("reservations", reservations);
      return "list";
    }

    // 予約の削除機能
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}") // 削除用のエンドポイント
    public String deleteReservation(@PathVariable Long id, RedirectAttributes redirectAttributes){
      if(id == null|| !reservationRepository.existsById(id)){
        redirectAttributes.addFlashAttribute("flashMessage", "削除対象の予約が見つかりませんでした。");
        return "redirect:reservations/all";
      }
      reservationRepository.deleteById(id); // IDに基づいて予約を削除
      redirectAttributes.addFlashAttribute("flashMessage", "予約情報を削除しました。");
      return "redirect:/reservations/all"; //　管理者用一覧に戻す

      
    }

    // 編集フォーム機能
    @GetMapping("/edit/{id}") // 編集用のエンドポイント
    public String editReservation(@PathVariable Long id, Model model){
      Reservation reservation = reservationRepository.findById(id).orElse(null);
      model.addAttribute("reservation", reservation);

      // LocalDateTime → 文字列フォーマット（null安全）
      String formattedDateTime = "";
      if (reservation != null && reservation.getReservationTime() != null) {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
          formattedDateTime = reservation.getReservationTime().format(formatter);
      }
      model.addAttribute("formattedDateTime", formattedDateTime); // フォーマット済みの日時をモデルに追加
      return "edit"; // templates/edit.html を表示)
    }

    @GetMapping("/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, RedirectAttributes redirectAttributes){
      reservationService.cancel(id);
      redirectAttributes.addFlashAttribute("flashMessage", "予約をキャンセルしました。");
      return "redirect:/reservations";
    }


    // 入力　→ 確認画面
    @PostMapping("/reserve")
    public String confirm(@Valid @ModelAttribute Reservation reservation, BindingResult result, Model model){
      if(result.hasErrors()){
        return "reserve"; // 入力エラーがある場合は再度フォームを表示
      }
      model.addAttribute("reservation", reservation);
      return "confirmation"; // templates/confirmation.html を表示(まだDBに保存していない！)
    }

    // 予約の更新機能
    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute Reservation reservation, BindingResult result, RedirectAttributes redirectAttributes) {
      
      if(result.hasErrors()){
        return "edit"; // 入力エラーがある場合は再度編集フォームを表示
      }

      // 元の予約情報を取得
      Reservation original = reservationRepository.findById(reservation.getId())
        .orElseThrow(() -> new IllegalArgumentException("予約が見つかりません。"));

      // 元の予約者を保持
      reservation.setUser(original.getUser());

    // 予約してるユーザー名を再度取得する
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username).orElseThrow();
    reservation.setUser(user); // 編集時にユーザー名を設定
    reservationRepository.save(reservation); // 予約情報をDBに保存

    // フラッシュメッセージを設定
    redirectAttributes.addFlashAttribute("flashMessage", "予約情報を更新しました");
    return "redirect:/reservations"; // 更新後、一覧画面にリダイレクト
    }

    // ログイン時の予約保存
    @PostMapping("/confirm")
    public String confirmReservation(@ModelAttribute Reservation reservation){
      String username = SecurityContextHolder.getContext().getAuthentication().getName();
      User user = userRepository.findByUsername(username).orElseThrow();
      reservation.setUser(user);
      reservationRepository.save(reservation);
      return "complete"; // 完了画面にリダイレクト
    }
    
}
