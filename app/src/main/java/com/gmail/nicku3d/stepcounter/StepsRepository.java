package com.gmail.nicku3d.stepcounter;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StepsRepository {
    private StepsDao mStepsDao;
    private LiveData<List<DailySteps>> mAllDailySteps;

    StepsRepository(Application application) {
        StepsRoomDatabase db = StepsRoomDatabase.getDatabase(application);
        mStepsDao = db.stepsDao();
        mAllDailySteps = mStepsDao.getAllDailySteps();
    }

    LiveData<List<DailySteps>> getAllDailySteps() {
        return mAllDailySteps;
    }

    public void insert (DailySteps steps) {
        new insertAsyncTask(mStepsDao).execute(steps);
    }

    private static class insertAsyncTask extends AsyncTask<DailySteps, Void, Void> {

        private StepsDao mAsyncTaskDao;

        insertAsyncTask(StepsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DailySteps... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
