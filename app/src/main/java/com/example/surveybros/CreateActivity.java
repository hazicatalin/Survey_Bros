package com.example.surveybros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CreateActivity extends AppCompatActivity {

    EditText title, question;
    ListView list;
    Spinner spinner;
    Button add, create;
    ArrayList<Question> questions = new ArrayList<Question>();
    ArrayList<String> questionsArr=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        title = findViewById(R.id.title);
        question = findViewById(R.id.question);
        list = findViewById(R.id.questions_list);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.addQuestion);
        create = findViewById(R.id.createSurvey);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.create_survey);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                switch (menuItem.getItemId()){
                    case R.id.surveys:
                        startActivity(new Intent(getApplicationContext(), SurveysActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.create_survey:
                        startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.my_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSurvey();
            }
        });
    }

    public void addQuestion(){
        String type, questionStr;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateActivity.this, R.array.answerType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        type = spinner.getSelectedItem().toString();
        questionStr = question.getText().toString();
        if(questionStr.isEmpty()){
            TextView questionTw=findViewById(R.id.question_tw);
            questionTw.setError("Start location is required!");
            questionTw.requestFocus();
            return;
        }
        question.setText("");
        questions.add(new Question(questionStr, type));
        questionsArr.add(questionStr);
        list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionsArr));
    }

    public void uploadSurvey(){
        String titleStr=title.getText().toString();
        if(titleStr.isEmpty()){
            TextView titleTw=findViewById(R.id.title_tw);
            titleTw.setError("Start location is required!");
            titleTw.requestFocus();
            return;
        }
        title.setText("");
        question.setText("");
        questionsArr.clear();
        list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionsArr));
    }
}