package com.example.techshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getName();

    EditText emailET;
    EditText passwordET;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_login);

        this.emailET = findViewById(R.id.editTextEmail);
        this.emailET.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
        this.passwordET = findViewById(R.id.editTextPassword);
        this.passwordET.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Login(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), R.string.signin_successful, Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "User " + email + " logged in successfully");
                openShopActivity();
            } else {
                Toast.makeText(getApplicationContext(), R.string.signin_failed + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "User " + email + " login failed");
            }
        });
    }

    private void openShopActivity() {
        Intent openLoginIntent = new Intent(this, ShopActivity.class);
        startActivity(openLoginIntent);
    }
}