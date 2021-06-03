package com.example.surveybros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.log_out);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
                preferences.edit().clear().commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.my_profile);

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
    }
}