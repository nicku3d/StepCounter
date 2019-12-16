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

        // Restore the state.
//        if (savedInstanceState != null) {
//            if(savedInstanceState.containsKey("user_name")) {
//                String name = savedInstanceState.getString("user_name");
//                etName.setText(name);
//            }
//            if(savedInstanceState.containsKey("user_height")) {
//                int height = savedInstanceState.getInt("user_height");
//                etHeight.setText(height);
//            }
//            if(savedInstanceState.containsKey("user_age")) {
//                int age = savedInstanceState.getInt("user_age");
//                etAge.setText(age);
//            }
//        }

    }

    public void applyLogin(View view) {

        tvError.setVisibility(View.INVISIBLE);

        if(isEmpty(etName) || isEmpty(etHeight) || isEmpty(etAge)) {
            tvError.setText("Fill all fields!");
            tvError.setVisibility(View.VISIBLE);
        } else {
            String name = etName.getText().toString();
            int height = Integer.parseInt(etHeight.getText().toString());
            int age = Integer.parseInt(etAge.getText().toString());

            //sprawdzanie czy wszystkie pola sa odpowiednio wypelniony
            if(height <=40 || height >= 300) {
                //blad wysokosc niepoprawna
                tvError.setText("Height field is wrongly filled");
                tvError.setVisibility(View.VISIBLE);
            }

            if(age > 150 || age < 0){
                //bledny wiek
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
        if(tvError.getVisibility() == View.INVISIBLE) {
            finish();
        }
    }

    private boolean isEmpty(EditText etText) {

        return etText.getText().toString().trim().length() <= 0;
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if(!isEmpty(etName)) {
//            outState.putString("userName", etName.getText().toString());
//        }
//
//        if(!isEmpty(etHeight)) {
//            outState.putInt("userHeight", Integer.parseInt(etHeight.getText().toString()));
//        }
//
//        if(!isEmpty(etAge)) {
//            outState.putInt("userAge", Integer.parseInt(etAge.getText().toString()));
//        }
//    }
}


