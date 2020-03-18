package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static SharedPreferences mPreferences;
    private TextView tvWelcome;

    //stepcounting things
    private SensorManager sensorManager;
    private Sensor sensor;
    static int startingStepCount = 0;
    private int stepCount = 0;
    private int previousDayStepCount = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences file name
        String sharedPrefFile = getApplicationContext().getPackageName()+".preferences";
        //getting app context once per create
        Context context = getApplicationContext();

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

        setAlarmManager(context);


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
    protected void onPostResume() {
        super.onPostResume();

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
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt("starting_step_count", startingStepCount);
        preferencesEditor.apply();
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

    public void setAlarmManager(Context context){
        //alarmManager to reset steps at the end of the day
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
