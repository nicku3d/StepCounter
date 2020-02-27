package com.gmail.nicku3d.stepcounter;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StepsViewModel extends AndroidViewModel {
    private StepsRepository mRepository;
    private LiveData<List<DailySteps>> mAllDailySteps;

    public StepsViewModel (Application application) {
        super(application);
        mRepository = new StepsRepository(application);
        mAllDailySteps = mRepository.getAllDailySteps();
    }

    LiveData<List<DailySteps>> getAllDailySteps() { return mAllDailySteps; }

    public void insert(DailySteps dailySteps) { mRepository.insert(dailySteps); }
}
