package com.example.surveybros;

public class Question {
    String question, answer, type;

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

    public void setAnswer(String answer){
        this.answer=answer;
    }
}
