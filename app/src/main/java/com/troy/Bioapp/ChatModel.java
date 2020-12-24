package com.troy.Bioapp;

public class ChatModel {
    public ChatModel(String mesg, String name) {
        this.mesg = mesg;
        this.name = name;
    }

    public ChatModel() {
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String mesg ;
    String name ;
}
