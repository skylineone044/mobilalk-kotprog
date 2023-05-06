package com.example.thechshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.color.DynamicColors;

public class SignupActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignupActivity.class.getName();

    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());
        setContentView(R.layout.activity_signup);

        this.emailET = findViewById(R.id.editTextEmail);
        this.passwordET = findViewById(R.id.editTextPassword);
        this.passwordAgainET = findViewById(R.id.editTextPasswordAgain);
    }
}