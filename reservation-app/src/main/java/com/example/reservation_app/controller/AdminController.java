package com.example.reservation_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.reservation_app.repository.ReservationRepository;

@Controller
public class AdminController {

  private final ReservationRepository reservationRepository;

  public AdminController(ReservationRepository reservationRepository){
    this.reservationRepository = reservationRepository;
  }

  @GetMapping("/admin/reservations")
  public String viewAllReservation(Model model){
    model.addAttribute("reservations", reservationRepository.findAll());
    return "admin/reservations";
  }
}
