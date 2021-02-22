package com.example.lifetracker;

public class track_model {
    String date,temp,oxygen,heart_rate,symptoms,possible;

    public track_model(String date, String temp, String oxygen, String heart_rate, String symptoms,String possible) {
        this.date = date;
        this.temp = temp;
        this.oxygen = oxygen;
        this.heart_rate = heart_rate;
        this.symptoms = symptoms;
        this.possible = possible;
    }

    public String getPossible() {
        return possible;
    }

    public void setPossible(String possible) {
        this.possible = possible;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }
}
