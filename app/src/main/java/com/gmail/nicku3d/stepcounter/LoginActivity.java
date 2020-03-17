package com.gmail.nicku3d.stepcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText etName, etHeight, etAge;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.et_name);
        etHeight = findViewById(R.id.et_height);
        etAge = findViewById(R.id.et_age);
        tvError = findViewById(R.id.tv_error);
    }

    public void applyLogin(View view) {

        tvError.setVisibility(View.INVISIBLE);

        if (isEmpty(etName) || isEmpty(etHeight) || isEmpty(etAge)) {
            tvError.setText("Fill all fields!");
            tvError.setVisibility(View.VISIBLE);
        } else {
            String name = etName.getText().toString();
            int height = Integer.parseInt(etHeight.getText().toString());
            int age = Integer.parseInt(etAge.getText().toString());

            //checking if all fields are fullfiled
            if (height <= 40 || height >= 300) {
                //error wrong height
                tvError.setText("Height field is wrongly filled");
                tvError.setVisibility(View.VISIBLE);
            }

            if (age > 150 || age < 0) {
                //wrong age
                tvError.setText("Age field is wrongly filled");
                tvError.setVisibility(View.VISIBLE);
            }


            //saving user information to preferences
            SharedPreferences.Editor preferencesEditor = MainActivity.mPreferences.edit();
            preferencesEditor.putString("user_name", name);
            preferencesEditor.putInt("user_height", height);
            preferencesEditor.putInt("user_age", age);
            //change isUserLoggedIn to true
            preferencesEditor.putBoolean("isUserLoggedIn", true);
            preferencesEditor.apply();
        }

        //
        //


        //going back to MainActivity
        if (tvError.getVisibility() == View.INVISIBLE) {
            finish();
        }
    }

    private boolean isEmpty(EditText etText) {

        return etText.getText().toString().trim().length() <= 0;
    }
}


