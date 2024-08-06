package com.example.personalexpensestracker;

import java.time.LocalDate;
import java.time.LocalTime;

public class Expenses {
    private int id;
    private String type;
    private double amount;
    private String notes;
    private LocalDate date;
    private LocalTime time;

    public Expenses(int id, String type, double amount, String notes) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.notes = notes;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public Expenses(int id, String type, double amount, String notes, LocalDate date, LocalTime time) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
