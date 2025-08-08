package com.example.reservation_app.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reservation_app.model.Reservation;
import com.example.reservation_app.model.ReservationStatus;
import com.example.reservation_app.repository.ReservationRepository;

@Service
public class ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;

  public void cancel(Long id){
    Reservation reservation = reservationRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("予約が見つかりません"));
    reservation.setStatus(ReservationStatus.CANCELLED); // <-Enumなどで管理
    reservationRepository.save(reservation);
  }


  public List<Reservation> findAll(){
    return StreamSupport
      .stream(reservationRepository.findAll().spliterator(), false)
      .collect(Collectors.toList());
  } 
}
