package com.gmail.nicku3d.stepcounter;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;



public class StepCounter implements SensorEventListener {

    private static StepCounter INSTANCE;
    private MutableLiveData<Integer> startingStepCount = new MutableLiveData<>();
    private MutableLiveData<Integer> stepCount = new MutableLiveData<>();
    //rebootsteps is temporary debug thing
    private MutableLiveData<Integer> rebootSteps = new MutableLiveData<>();

    public MutableLiveData<Integer> getRebootSteps() {
        return rebootSteps;
    }

    public MutableLiveData<Integer> getStartingStepCount() {
        return startingStepCount;
    }

    public MutableLiveData<Integer> getStepCount() {
        return stepCount;
    }

    public int getStartingStepCountValue() {
        if(startingStepCount.getValue() == null) {
            return 0;
        }
        return startingStepCount.getValue();
    }

    public void setStartingStepCountValue(int startingStepCount) {
        this.startingStepCount.setValue(startingStepCount);
    }


    public int getStepCountValue() {
        if(stepCount.getValue() == null) {
            return 0;
        }
        return stepCount.getValue();
    }

    public void setStepCountValue(int stepCount) {
        this.stepCount.setValue(stepCount);
    }

    private StepCounter() {
        this.startingStepCount.setValue(0);
        this.stepCount.setValue(0);
        this.rebootSteps.setValue(0);
    }

    static StepCounter getStepCounter(){
        if(INSTANCE == null) {
            INSTANCE = new StepCounter();
        }
        return INSTANCE;
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            int rebootSteps = (int)e.values[0];

            this.rebootSteps.setValue(rebootSteps);


            if (getStartingStepCountValue() < 1) {
                // initial value
                startingStepCount.setValue(rebootSteps);
            }

            // Calculate steps taken based on first counter value received.
            //stepCount = rebootSteps - startingStepCount;
            stepCount.setValue(rebootSteps- getStartingStepCountValue());
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
