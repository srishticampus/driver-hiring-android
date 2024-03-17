package com.project.driverhiring;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // showLoadingIndicator(true);
        delayFlow();
    }

    private void delayFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sP = getSharedPreferences("login_pref", MODE_PRIVATE);
                if (sP.getBoolean("session", false)) {
                    switch (sP.getString("role", "")) {
                        case "driver":
                            Intent intent = new Intent(MainActivity.this, DriverHomeActivity.class);
                            startActivity(intent);
                            break;
                        case "user":
                            Intent intentC = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intentC);
                            break;
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
//                startActivity(intent);
//                finish();
//                //showLoadingIndicator(false);
//                overridePendingTransition(0, 0);
            }
        }, SPLASH_DELAY);
    }
}