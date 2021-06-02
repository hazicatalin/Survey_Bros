package com.example.surveybros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private Button register;
    RequestQueue queue;

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