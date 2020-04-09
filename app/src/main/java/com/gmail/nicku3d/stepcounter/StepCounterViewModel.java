package com.gmail.nicku3d.stepcounter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class StepCounterViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> startingStepCount;
    private MutableLiveData<Integer> stepCount;
    private MutableLiveData<Integer> rebootSteps;
    private StepCounter stepCounter;


    public StepCounterViewModel(@NonNull Application application) {
        super(application);
        stepCounter = StepCounter.getStepCounter();
        startingStepCount = stepCounter.getStartingStepCount();
        stepCount = stepCounter.getStepCount();
        rebootSteps = stepCounter.getRebootSteps();
    }

    MutableLiveData<Integer> getStepCount() { return stepCount; }
    MutableLiveData<Integer> getStartingStepCount() { return startingStepCount;}
    MutableLiveData<Integer> getRebootSteps() {return rebootSteps;}
}
