package com.example.michellebiol.sampleapp.Models;

import com.google.gson.annotations.SerializedName;

public class AnswerResponse {
    @SerializedName("fun_facts")
    private String facts;

    public AnswerResponse(String facts) {
        this.facts = facts;
    }

    public String getFacts() {
        return facts;
    }
}
