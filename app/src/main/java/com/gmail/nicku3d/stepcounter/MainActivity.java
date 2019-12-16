package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    //private static String PACKAGE_NAME;
    static SharedPreferences mPreferences;
    private TextView tvWelcome;
    //private String mSharedPrefFile = getApplicationContext().getPackageName()+"preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences file name
        String sharedPrefFile = getApplicationContext().getPackageName()+".preferences";


        //Init preferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        boolean isUserLoggedIn = mPreferences.getBoolean("isUserLoggedIn", false);

        tvWelcome = findViewById(R.id.tv_welcome);

        if(!isUserLoggedIn) {

            //open login activity
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);
        } else {
            String name = mPreferences.getString("user_name", "Anonymous");
            int height = mPreferences.getInt("user_height", 180);
            int age = mPreferences.getInt("user_age", 18);

            tvWelcome.setText("Welcome "+name+ ", your height is: "+ height + "cm and " +
                    "you are "+ age + " years old. Have good time here.");
        }



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String name = mPreferences.getString("user_name", "Anonymous");
        int height = mPreferences.getInt("user_height", 180);
        int age = mPreferences.getInt("user_age", 18);

        tvWelcome.setText("Welcome "+ name + ", your height is: "+ height + "cm and " +
                "you are "+ age + " years old. Have good time here.");
    }

    @Override
    protected void onPause() {
        super.onPause();

//        //Save preferences
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        //prefs to save here
//        preferencesEditor.apply();
    }
}
