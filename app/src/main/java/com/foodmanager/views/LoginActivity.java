package com.foodmanager.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.foodmanager.R;
import com.foodmanager.listeners.LoginListener;
import com.foodmanager.models.SingletonDatabaseManager;
import com.foodmanager.models.Utilizador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    public static final String FOODMAN_SHARED_PREFS = "foodman_shared_prefs";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";
    public static final String APIKEY = "apikey";
    public static final String IS_LOGGED_IN = "isloggedin";

    public TextInputEditText tiEmail, tiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tiEmail = findViewById(R.id.tiEmail);
        tiPassword = findViewById(R.id.tiPassword);

        FloatingActionButton fabLogin = findViewById(R.id.fabLogin);
        SingletonDatabaseManager.getInstance(getApplicationContext()).setLoginListener(this);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonDatabaseManager.getInstance(getApplicationContext()).login(tiEmail.getText().toString(), tiPassword.getText().toString(), getApplicationContext());
            }
        });

        FloatingActionButton fabResgister = findViewById(R.id.fabRegister);
        fabResgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChromeCustomTabs("http://192.168.1.74/foodman/frontend/web/site/signup");
            }
        });

    }

    @Override
    public void onLogin(Utilizador utilizador) {
        SharedPreferences sharedPrefs = getSharedPreferences(FOODMAN_SHARED_PREFS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(NOME, utilizador.getNome());
        editor.putString(EMAIL, utilizador.getEmail());
        editor.putString(APIKEY, utilizador.getApikey());
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.apply();

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(mainIntent);
        finish();
    }


    private void openChromeCustomTabs(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(url));
    }
}