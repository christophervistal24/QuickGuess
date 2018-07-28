package com.example.michellebiol.sampleapp.Models;

public class QuestionsItem {

    private String id;
    private String quest;
    private String question_category_id;
    private String choice_a;
    private String choice_b;
    private String choice_c;
    private String choice_d;
    private String correct_answer;
    private String fun_facts;
    private String question_result;

    public QuestionsItem(String id, String quest, String question_category_id, String choice_a, String choice_b, String choice_c, String choice_d, String correct_answer , String fun_facts , String question_result) {
        this.id = id;
        this.quest = quest;
        this.question_category_id = question_category_id;
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

    public String getQuest() {
        return quest;
    }

    public String getQuestion_category_id() {
        return question_category_id;
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
