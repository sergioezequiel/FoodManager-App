package com.foodmanager.models;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}

 /* ISTO ADICIONA SE NA ATIVIDADE PARA FAZER O USO DA CLASS
        final ConstraintLayout constraintLayout = this.findViewById(R.id.container);
        constraintLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                *//*Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();*//*
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

              *//*  Toast.makeText(MainActivity.this,"Right" , Toast.LENGTH_SHORT).show();*//*
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
                *//*Toast.makeText(MainActivity.this,selectedItem.toString() , Toast.LENGTH_SHORT).show();*//*

            }
            public void onSwipeBottom() {
                *//*Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();*//*
            }

        });*/