package com.example.michellebiol.sampleapp.Models;

public class QuestionsResponse {
    private String id;
    private String question;
    private String question_categories_id;
    private String choice_a;
    private String choice_b;
    private String choice_c;
    private String choice_d;
    private String correct_answer;
    private String fun_facts;
    private String question_result;

    public QuestionsResponse(String id, String question, String question_categories_id, String choice_a, String choice_b, String choice_c, String choice_d, String correct_answer , String fun_facts , String question_result) {
        this.id = id;
        this.question = question;
        this.question_categories_id = question_categories_id;
        this.choice_a = choice_a;
        this.choice_b = choice_b;
        this.choice_c = choice_c;
        this.choice_d = choice_d;
        this.correct_answer = correct_answer;
        this.fun_facts = fun_facts;
        this.question_result = question_result;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestion_categories_id() {
        return question_categories_id;
    }

    public String getChoice_a() {
        return choice_a;
    }

    public String getChoice_b() {
        return choice_b;
    }

    public String getChoice_c() {
        return choice_c;
    }

    public String getChoice_d() {
        return choice_d;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public String getFun_facts() {
        return fun_facts;
    }

    public String getQuestion_result() {
        return question_result;
    }
}
