package com.troy.Bioapp;


public class writemodel {
    public writemodel(String score, String date) {
        Score = score;
        this.date = date;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String Score;
   String date;
}
