package com.artlighter.glucosecontrolservice.diary.entity;

import com.artlighter.glucosecontrolservice.user.UserDTO;

import java.util.Date;

public class DiaryEntry {
    private double measurement;
    private Date date;
    private String notes;
    private UserDTO user;

    public DiaryEntry() {

    }

    public DiaryEntry(double measurement, Date date, String notes, UserDTO user) {
        this.measurement = measurement;
        this.date = date;
        this.notes = notes;
        this.user = user;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
