package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void applyLogin(View view) {
//        EditText etAge = findViewById(R.id.et_age);
//        String age = etAge.getText().toString();
        String xd = ((EditText)findViewById(R.id.et_age)).getText().toString();
    }
}
