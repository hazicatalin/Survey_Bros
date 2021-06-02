package com.example.surveybros;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    TextView title;
    ArrayList<Question> questions = new ArrayList<Question>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("key"));
        list = findViewById(R.id.list);

        readQuestions();
    }

    public void readQuestions(){
        questions.add(new Question("Ce fai?", "text"));
        questions.add(new Question("Cati ani ai0?", "number"));
        questions.add(new Question("Cand te ai nascut?", "date"));

        SurveyActivity.MyAdapter adapter = new MyAdapter(SurveyActivity.this, questions);
        list.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<Question> {
        Context context;
        List<Question> rQuestion;

        MyAdapter (Context c, ArrayList<Question> question){
            super(c, R.layout.answer, R.id.question, question);
            this.context=c;
            this.rQuestion=question;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View question = layoutInflater.inflate(R.layout.answer, parent, false);
            TextView myQuestion = question.findViewById(R.id.question);
            EditText myAnswer = question.findViewById(R.id.answer);

            myQuestion.setText(rQuestion.get(position).getQuestion());

            if(rQuestion.get(position).getType()=="text"){
                myAnswer.setHint("text");
                myAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else if(rQuestion.get(position).getType()=="number"){
                myAnswer.setHint("number");
                myAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);

            }
            else if(rQuestion.get(position).getType()=="date"){
                myAnswer.setHint("date");
                myAnswer.setInputType(InputType.TYPE_CLASS_DATETIME);
            }
            return question;
        }
    }
}