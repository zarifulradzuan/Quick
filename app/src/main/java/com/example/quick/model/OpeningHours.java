package com.example.quick.model;

public class OpeningHours {
    String opening;
    String closing;

    public OpeningHours() {
    }

    public OpeningHours(String opening, String closing){
        this.opening = opening;
        this.closing = closing;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getOpening() {
        return opening;
    }

    public String getClosing() {
        return closing;
    }
}
