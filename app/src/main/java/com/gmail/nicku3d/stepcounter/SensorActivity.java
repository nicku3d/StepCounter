package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    //stepcounting things
    private SensorManager sensorManager;
    private Sensor sensor;
    private int startingStepCount = 0;
    private int stepCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("user_step_count", stepCount);
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = MainActivity.mPreferences.edit();
        preferencesEditor.putInt("starting_step_count", startingStepCount);
        preferencesEditor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //TO DOs
        /*open shared prefs to save startingstepcount when application is killed or something
        *  next -> stepcount will be saved to database :)
        * when activity is destroyed and then created again save step count :)*/

//        if(savedInstanceState != null){
//            stepCount = savedInstanceState.getInt("user_step_count");
//        }

        startingStepCount = MainActivity.mPreferences.getInt("starting_step_count", 0);




        //stepcounting things init
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
