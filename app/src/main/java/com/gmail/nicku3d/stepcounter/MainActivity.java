package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private static String PACKAGE_NAME;
    private SharedPreferences mPreferences;
    //private String mSharedPrefFile = getApplicationContext().getPackageName()+"preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences file name
        String sharedPrefFile = getApplicationContext().getPackageName()+".preferences";


        //Init preferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Boolean isUserLoggedIn = mPreferences.getBoolean("isUserLoggedIn", false);

        if(!isUserLoggedIn) {

            //open login activity
            Intent loginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(loginActivityIntent);

            //change isUserLoggedIn to true here or in the other activity, probably in the other one
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.putBoolean("isUserLoggedIn", true);
            preferencesEditor.apply();
        }

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
