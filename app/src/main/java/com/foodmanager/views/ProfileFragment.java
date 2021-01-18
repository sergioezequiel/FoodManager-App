package com.foodmanager.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.foodmanager.R;
import com.foodmanager.listeners.StatsListener;
import com.foodmanager.models.SingletonDatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment implements StatsListener {

    private static final int STAT_ATIVO = 10;
    private static final int STAT_INATIVO = 9;
    private static final int STAT_ELIMINADO = 0;

    private FloatingActionButton fabLogout;
    private TextView textViewProfileName, txtProfileEmail, txtProfileCreated, txtState, textViewTotalRecipes, textViewTotalInventory;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fabLogout = root.findViewById(R.id.fabLogout);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences(LoginActivity.FOODMAN_SHARED_PREFS, Context.MODE_PRIVATE);

        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        textViewProfileName = root.findViewById(R.id.textViewProfileName);
        textViewProfileName.setText(sharedPrefs.getString(LoginActivity.NOME, ""));
        txtProfileEmail = root.findViewById(R.id.txtProfileEmail);
        txtProfileEmail.setText(sharedPrefs.getString(LoginActivity.EMAIL, ""));

        txtProfileCreated = root.findViewById(R.id.txtProfileCreated);
        txtState = root.findViewById(R.id.txtState);
        textViewTotalRecipes = root.findViewById(R.id.textViewTotalRecipes);
        textViewTotalInventory = root.findViewById(R.id.textViewTotalInventory);

        SingletonDatabaseManager.getInstance(getContext()).setStatsListener(this);
        SingletonDatabaseManager.getInstance(getContext()).getStatsUtilizador(getContext());
        SingletonDatabaseManager.getInstance(getContext()).getStatsDespensa(getContext());
        SingletonDatabaseManager.getInstance(getContext()).getStatsReceitas(getContext());

        return root;
    }

    @Override
    public void onGetStatsUser(int createdAt, int status) {
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM");

        txtProfileCreated.setText(spf.format(new Date(createdAt)));
        switch (status) {
            case STAT_ATIVO:
                txtState.setText("Ativo");
                break;
            case STAT_INATIVO:
                txtState.setText("Inativo");
                break;
            case STAT_ELIMINADO:
                txtState.setText("Eliminado");
                break;
            default:
                txtState.setText("Estado desconhecido");
                break;
        }
    }

    @Override
    public void onGetStatsReceitas(int stat) {
        textViewTotalRecipes.setText(stat + "");
    }

    @Override
    public void onGetStatsDespensa(int stat) {
        textViewTotalInventory.setText(stat + "");
    }
}