package com.example.michellebiol.sampleapp.Models;

public class AccountsItem {
    private String username;
    private int id;

    public AccountsItem(String username, int id) {
        this.username = username;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }
}
