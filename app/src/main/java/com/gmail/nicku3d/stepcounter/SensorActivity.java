package com.gmail.nicku3d.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    private int counterSteps = 0;
    private int stepCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //stepcounting things init
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (counterSteps < 1) {
                // initial value
                counterSteps = (int)e.values[0];
            }

            // Calculate steps taken based on first counter value received.
            stepCounter = (int)e.values[0] - counterSteps;
            TextView stepCount = findViewById(R.id.tv_steps_count);
            stepCount.setText(Integer.toString(stepCounter));
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
