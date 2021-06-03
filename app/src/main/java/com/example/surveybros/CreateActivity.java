package com.example.surveybros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateActivity extends AppCompatActivity {

    EditText title, question;
    ListView list;
    Spinner spinner;
    Button add, create;
    String token, userId;
    ArrayList<String> typesArr=new ArrayList<String>();
    ArrayList<String> questionsArr=new ArrayList<String>();
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
        token = preferences.getString("token", null);
        userId = preferences.getString("id", null);
        Log.d("raspunsasd", token);

        title = findViewById(R.id.title);
        question = findViewById(R.id.question);
        list = findViewById(R.id.questions_list);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.addQuestion);
        create = findViewById(R.id.createSurvey);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CreateActivity.this, R.array.answerType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
        type = spinner.getSelectedItem().toString();
        questionStr = question.getText().toString();
        if(questionStr.isEmpty()){
            TextView questionTw=findViewById(R.id.question_tw);
            questionTw.setError("Start location is required!");
            questionTw.requestFocus();
            return;
        }
        if(type.equals("text")){
            typesArr.add("text");
        }
        else if(type.equals("yes/no")){
            typesArr.add("yesno");
        }
        question.setText("");
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
        JSONArray jsArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        for(int i=0; i<questionsArr.size(); i++){
            Map<String, String> params = new HashMap<String, String>();
            params.put("question_text", questionsArr.get(i));
            params.put("question_type", typesArr.get(i));
            params.put("option_names", "");
            JSONObject json =  new JSONObject(params);
            jsArray.put(json);
        }
        try {
            jsonObject.put("user_id", userId);
            jsonObject.put("survey_title", titleStr);
            jsonObject.put("survey_questions", jsArray);
        }catch (JSONException e){
            e.printStackTrace();
        }

        String url ="http://10.0.2.2:4000/surveys";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("raspunsasd", response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("raspunsasd", error.toString());
            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ token);
                return params;
            }
        };
        queue.add(jsonObjReq);

        Log.d("raspunsasd", jsonObject.toString());
        title.setText("");
        question.setText("");
        questionsArr.clear();
        list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, questionsArr));
    }
}