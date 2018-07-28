package com.example.michellebiol.sampleapp.Models;

public class RegisterQuestionResponse {
    private String id;
    private String question;

    public RegisterQuestionResponse(String id, String question) {
        this.id = id;
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

}
