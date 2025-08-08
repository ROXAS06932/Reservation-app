package com.example.reservation_app.model;

public enum ReservationStatus {
    PENDING("保留中", "text-warning"),
    CONFIRMED("確認済み", "text-success"),
    CANCELLED("キャンセル済み", "text-danger"),;

    private final String label;
    private final String cssClass;

    ReservationStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
    }

    public String getLabel() {
        return label;
    }
    public String getCssClass() {
        return cssClass;
    }

    // 共通メソッド
    public boolean isCancelable(){
      return this == PENDING;
    }

    public boolean isConfirmed(){
      return this == CONFIRMED;
    }

    public boolean isCancelad(){
      return this == CANCELLED;
    }
}
