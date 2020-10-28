package com.foodmanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inventory, R.id.navigation_recipes, R.id.navigation_shopping_list, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navigation_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        final ConstraintLayout constraintLayout = this.findViewById(R.id.container);
        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                /*Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();*/
            }

            public void onSwipeRight() {

                int selectedItemId = navView.getSelectedItemId();
                MenuItem selectedItem = navView.getMenu().findItem(selectedItemId);

                switch (selectedItem.toString()){
                    case "Inventory":
                        navView.setSelectedItemId(R.id.navigation_profile);
                        break;
                    case "Recipes":
                        navView.setSelectedItemId(R.id.navigation_inventory);
                        break;

                    case "Shopping List":
                        navView.setSelectedItemId(R.id.navigation_recipes);
                        break;

                    case "Profile":
                        navView.setSelectedItemId(R.id.navigation_shopping_list);
                        break;
                }

              /*  Toast.makeText(MainActivity.this,"Right" , Toast.LENGTH_SHORT).show();*/
            }

            public void onSwipeLeft() {
                int selectedItemId = navView.getSelectedItemId();
                MenuItem selectedItem = navView.getMenu().findItem(selectedItemId);

                switch (selectedItem.toString()){
                    case "Inventory":
                        navView.setSelectedItemId(R.id.navigation_recipes);
                        break;
                    case "Recipes":
                        navView.setSelectedItemId(R.id.navigation_shopping_list);
                        break;

                    case "Shopping List":
                        navView.setSelectedItemId(R.id.navigation_profile);
                        break;

                    case "Profile":
                        navView.setSelectedItemId(R.id.navigation_inventory);
                        break;
                }
                /*Toast.makeText(MainActivity.this,selectedItem.toString() , Toast.LENGTH_SHORT).show();*/

            }
            public void onSwipeBottom() {
                /*Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();*/
            }

        });
    }

    private int getSelectedItem(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                return menuItem.getItemId();
            }
        }
        return 0;
    }
}