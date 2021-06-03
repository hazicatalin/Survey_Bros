package com.example.surveybros;

public class Question {
    String question, type;

    public Question(){}
    public Question(String question, String type) {
        this.question=question;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }
}
