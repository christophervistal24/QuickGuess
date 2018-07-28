package com.example.michellebiol.sampleapp.Models;

public class AnswerRequest {
    private int question_id;
    private String question_result;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_result() {
        return question_result;
    }

    public void setQuestion_result(String question_result) {
        this.question_result = question_result;
    }
}
