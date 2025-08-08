package com.example.reservation_app.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.reservation_app.model.Reservation;
import com.example.reservation_app.model.User;
import com.example.reservation_app.repository.ReservationRepository;
import com.example.reservation_app.repository.UserRepository;
import com.example.reservation_app.security.CustomUserDetails;


@Controller
public class HomeController {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    public HomeController(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    } 


@GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            List<Reservation> allReservations = StreamSupport
                .stream(reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
            model.addAttribute("reservations", allReservations);
            return "home";
        } else {
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            List<Reservation> userReservations = reservationRepository.findByUser(user);
            model.addAttribute("reservations", userReservations);
            return "home";
        }
    }

}
