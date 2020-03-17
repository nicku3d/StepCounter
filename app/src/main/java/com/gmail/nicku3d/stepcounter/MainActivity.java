package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    //private static String PACKAGE_NAME;
    static SharedPreferences mPreferences;
    private TextView tvWelcome;
    //private String mSharedPrefFile = getApplicationContext().getPackageName()+"preferences";

    //stepcounting things
    private SensorManager sensorManager;
    private Sensor sensor;
    private int startingStepCount = 0;
    private int stepCount = 0;




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


        startingStepCount = mPreferences.getInt("starting_step_count", 0);
        //stepcounting things init
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);



    }

    @Override
    protected void onResume() {
        super.onResume();

        //maybe its better to use onSavedInstance or sth
        String name = mPreferences.getString("user_name", "Anonymous");
        int height = mPreferences.getInt("user_height", 180);
        int age = mPreferences.getInt("user_age", 18);

        tvWelcome.setText("Welcome "+ name + ", your height is: "+ height + "cm and " +
                "you are "+ age + " years old. Have good time here.");

        //sensor stuff
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null) {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (startingStepCount < 1) {
                // initial value
                startingStepCount = (int)e.values[0];
            }

            // Calculate steps taken based on first counter value received.
            stepCount = (int)e.values[0] - startingStepCount;
            TextView showStepCount = findViewById(R.id.tv_steps_count);
            //show steps since reboot
            ((TextView)findViewById(R.id.tv_steps_reboot)).setText(Integer.toString((int)e.values[0]));
            showStepCount.setText(Integer.toString(stepCount));
            //show number of starting steps
            ((TextView)findViewById(R.id.tv_starting_steps)).setText(Integer.toString(startingStepCount));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
