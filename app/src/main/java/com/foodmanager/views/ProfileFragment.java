package com.foodmanager.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.foodmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProfileFragment extends Fragment {

    private FloatingActionButton fabLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fabLogout = root.findViewById(R.id.fabLogout);

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = ProfileFragment.this.getActivity().getSharedPreferences(LoginActivity.FOODMAN_SHARED_PREFS, Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(LoginActivity.NOME, "");
                editor.putString(LoginActivity.EMAIL, "");
                editor.putString(LoginActivity.APIKEY, "");
                editor.putBoolean(LoginActivity.IS_LOGGED_IN, false);
                editor.apply();

                Intent intent = new Intent(ProfileFragment.this.getActivity(), LoginActivity.class);
                startActivity(intent);
                ProfileFragment.this.getActivity().finish();
            }
        });

        return root;
    }
}