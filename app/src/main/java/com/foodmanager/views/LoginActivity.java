package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.foodmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                openChromeCustomTabs("http://192.168.1.82/foodman/frontend/web/site/signup");
            }
        });

    }
    private void openChromeCustomTabs(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(url));
    }
}