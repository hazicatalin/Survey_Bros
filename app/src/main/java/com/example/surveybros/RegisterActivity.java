package com.example.surveybros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etLastName, etEmail, etPassword, etConfPassword;
    TextView textView;
    private Button register;
    Spinner spinner;
    RequestQueue queue;
    int subscriptionType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();

        etName = findViewById(R.id.name_register);
        etLastName = findViewById(R.id.last_name_register);
        etEmail = findViewById(R.id.email_register);
        etPassword= findViewById(R.id.password_register);
        etConfPassword = findViewById(R.id.password_confirm_register);
        register = findViewById(R.id.register);
        spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.txt);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisterActivity.this, R.array.accountType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                if(text.equals("Free")){
                    textView.setText("2 weeks lifetime, no price, expensive per week extension, low completion count");
                    subscriptionType=1;
                }
                if(text.equals("Standard")){
                    textView.setText("2 weeks lifetime, 0.1 per 1000 completions, 0.05 extension per 1000 completions, 10k completion max");
                    subscriptionType=2;
                }
                if(text.equals("Premium")){
                    textView.setText("3 weeks, 0.11 per 1000 comp, 0.045 ext per 1000 comp, 15k compl total");
                    subscriptionType=3;
                }
                if(text.equals("Enterprise")){
                    textView.setText("4 weeks, 0.12 per 1000, 0.04 ext per 1000, 50k compl total");
                    subscriptionType=4;
                }
                if(text.equals("Professional")){
                    textView.setText("8 weeks, 0.15 per 1000, 0.03 ext per 1000, no compl limit");
                    subscriptionType=5;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(spinner.getSelectedItem().toString().equals("Free")){
            textView.setText("2 weeks lifetime, no price, expensive per week extension, low completion count");
        }
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        String name = etName.getText().toString().trim(),
                lastName = etLastName.getText().toString().trim(),
                email = etEmail.getText().toString().trim(),
                password = etPassword.getText().toString().trim(),
                confPassword = etConfPassword.getText().toString().trim();
        if(name.isEmpty()){
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please provide a valid email");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if(confPassword.isEmpty()){
            etConfPassword.setError("Password confirmation is required!");
            etConfPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("Password length should be min 6 characters!");
            etPassword.requestFocus();
            return;
        }
        if(!password.equals(confPassword)) {
            etConfPassword.setError("Password do not match!");
            etConfPassword.requestFocus();
            return;
        }

        String url ="http://10.0.2.2:4000/accounts/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("pass", password);
        params.put("first_name", name);
        params.put("last_name", lastName);

        JSONObject json =  new JSONObject(params);
        try{
            json.put("subscription_type", subscriptionType);
        } catch (JSONException e) {
            Log.d("raspunsasd", e.toString());
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, json,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            SharedPreferences preferences = getSharedPreferences("token", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userToken", token);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), SurveysActivity.class));
                            Log.d("raspunsasd", response.toString());

                        } catch (JSONException e) {
                            Log.d("raspunsasd", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("raspunsasd", error.toString());
            }
        });
        queue.add(jsonObjReq);

    }
}