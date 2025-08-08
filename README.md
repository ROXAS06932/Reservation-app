# Reservation Management App

このアプリは、ユーザーが予約を登録・確認・キャンセルでき、管理者が予約一覧を管理・編集・削除できる "Spring Boot製の予約管理システム"です。

---

## 機能概要

### ユーザー機能
- 予約フォームから新規予約
- 予約確認画面 → 完了画面
- 自分の予約一覧表示
- 予約キャンセル

### 管理者機能
- 全予約一覧の表示
- 予約の編集（ステータス更新含む）
- 予約の削除
- ロールベースアクセス制御（`ROLE_ADMIN`）

---

## 🛠 技術スタック

| 技術 | 内容 |
|------|------|
| Spring Boot | アプリケーションのベース |
| Spring Security | ロールベース認証・認可 |
| JPA (Hibernate) | DBアクセス・エンティティ管理 |
| Thymeleaf | HTMLテンプレートエンジン |
| H2 / MySQL | 開発用DB（切り替え可能） |
| HTML / CSS / JS | UI構築とバリデーション |

---

## ロールとアクセス制御

| ロール | アクセス可能な画面 |
|--------|--------------------|
| `USER` | `/reserve`, `/reservations`, `/cancel/{id}` |
| `ADMIN` | `/reservations/all`, `/edit/{id}`, `/delete/{id}` |

---

## 起動方法

```bash
# 1. プロジェクトをクローン
git clone https://github.com/your-username/reservation-app.git

# 2. 必要な依存をインストール（IDEで自動）
# 3. アプリを起動
./mvnw spring-boot:run

## アクセス
・ユーザー画面 → http://localhost:8080/reserve

・管理者画面 → http://localhost:8080/reservations/all

<今後の改善案>
・メール通知機能（予約完了・キャンセル）
・カレンダーUIで予約状況を表示
・CSVエクスポート機能（管理者用）
・予約者検索・フィルター機能

<開発者>
hiroaki-kikuchi

技術スタック：Spring Boot / JPA / Thymeleaf / HTML/CSS / javaScript
