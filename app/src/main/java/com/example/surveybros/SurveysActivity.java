package com.example.surveybros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveysActivity extends AppCompatActivity {

    ListView surveys;
    ArrayList<String> titles =  new ArrayList<String>();
    ArrayList<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);
        surveys=findViewById(R.id.surveys_list);
        readSurveys();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.surveys);

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

        surveys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), SurveyActivity.class);
                intent.putExtra("key", titles.get(position));
                startActivity(intent);
            }
        });
    }

    public void readSurveys(){
        titles.add("first");
        titles.add("second");
        titles.add("third");
        titles.add("fourth");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        surveys.setAdapter(adapter);
    }
}