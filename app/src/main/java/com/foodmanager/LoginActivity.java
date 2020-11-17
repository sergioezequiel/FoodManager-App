package com.foodmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FloatingActionButton fabLogin = findViewById(R.id.fabLogin);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(mainIntent);
                finish();
            }
        });

        FloatingActionButton fabResgister = findViewById(R.id.fabRegister);
        fabResgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(registerIntent);
            }
        });

    }
}