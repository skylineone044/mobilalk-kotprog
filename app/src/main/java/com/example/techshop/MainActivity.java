package com.example.techshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_main);

        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void OpenLoginActivity(View view) {
        Intent openLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(openLoginIntent);
    }

    public void OpenSignupActivity(View view) {
        Intent openLoginIntent = new Intent(this, SignupActivity.class);
        startActivity(openLoginIntent);
    }

    public void LoginAsGuest(View view) {
        firebaseAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), R.string.signin_successful, Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Anon logged in successfully");
                openShopActivity();
            } else {
                Toast.makeText(getApplicationContext(), R.string.signin_failed + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Anon login failed");
            }
        });
    }

    private void openShopActivity() {
        Intent openLoginIntent = new Intent(this, ShopActivity.class);
        startActivity(openLoginIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            openShopActivity();
        }
    }
}