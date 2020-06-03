package com.labstechnology.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.labstechnology.project1.adapters.NotificationRecViewAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final Utils utils = new Utils(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: logged in :" + utils.isSignedIn());
                if (utils.isSignedIn()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    Log.d(TAG, "run: sending user to Home activity");
                    startActivity(intent);
                } else {
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Log.d(TAG, "run: sending user to login activity");
                    startActivity(intent1);
                }
            }
        }, 2000);

    }
}
