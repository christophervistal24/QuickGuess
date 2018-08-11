package com.example.michellebiol.sampleapp.QuestionModule;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.michellebiol.sampleapp.BR;

public class Question   {
    public String questionId;
    public String question;
    public String choice_a;
    public String choice_b;
    public String choice_c;
    public String choice_d;
    public String funFacts;

    public Question(String questionId, String question, String choice_a, String choice_b, String choice_c, String choice_d , String funFacts) {
        this.setQuestionId(questionId);
        this.setQuestion(question);
        this.setChoice_a(choice_a);
        this.setChoice_b(choice_b);
        this.setChoice_c(choice_c);
        this.setChoice_d(choice_d);
        this.setFunFacts(funFacts);

    }

    public String getFunFacts() {
        return funFacts;
    }

    public void setFunFacts(String funFacts) {
        this.funFacts = funFacts;
    }


    public String getChoice_a() {
        return choice_a;
    }

    public void setChoice_a(String choice_a) {
        this.choice_a = choice_a;
    }

    public String getChoice_b() {
        return choice_b;
    }

    public void setChoice_b(String choice_b) {
        this.choice_b = choice_b;
    }

    public String getChoice_c() {
        return choice_c;

    }

    public void setChoice_c(String choice_c) {
        this.choice_c = choice_c;
    }

    public String getChoice_d() {
        return choice_d;
    }

    public void setChoice_d(String choice_d) {
        this.choice_d = choice_d;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
