package com.example.thechshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignupActivity.class.getName();

    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_signup);

        this.emailET = findViewById(R.id.editTextEmail);
        this.emailET.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
        this.passwordET = findViewById(R.id.editTextPassword);
        this.passwordET.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        this.passwordAgainET = findViewById(R.id.editTextPasswordAgain);
        this.passwordAgainET.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Sighup(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (!password.equals(passwordAgain)) {
            Toast.makeText(getApplicationContext(), R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), R.string.signup_successful, Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "User " + email + " created successfully");
                openShopActivity();
            } else {
                Toast.makeText(getApplicationContext(), R.string.signup_failed + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "User " + email + " signup failed");
            }
        });
    }

    private void openShopActivity() {
        Intent openLoginIntent = new Intent(this, ShopActivity.class);
        startActivity(openLoginIntent);
    }
}