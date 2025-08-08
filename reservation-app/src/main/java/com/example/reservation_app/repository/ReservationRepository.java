package com.example.reservation_app.repository;

import com.example.reservation_app.model.Reservation;
import com.example.reservation_app.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


// ReservationRepositoryは、基本的にSpring Data JPAが自動で実装してくれるインターフェースなのである。
// 単純なCRUD(登録・取得・更新・削除)操作なら、何も記述しなくても既に使える。


@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long>{ //CrudRepositoryを使うことで、save()メソッドが使えるようになる
    // ここに必要なクエリメソッドを追加できます
    // 例: List<Reservation> findByDate(String date);
    // ただし、基本的なCRUD操作はCrudRepositoryで提供されるので
    // 特別なクエリが必要な場合にのみ追加してください
    // 例えば、日付で予約を検索するメソッドを追加することができます
    // List<Reservation> findByDate(String date);

    // ユーザーの予約だけ取得するRepositoryメソッド
    // Spring Data JPAがこの命名を元に自動でクエルを生成してくれる。
    // List<Reservation> findByUsername(String username); // ユーザー名で予約を検索するメソッド

    List<Reservation> findByUser(User user);

}
