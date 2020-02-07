package com.gmail.nicku3d.stepcounter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StepsDAO {

    @Insert
    void insert(DailySteps dailySteps);

    @Query("DELETE FROM steps_table")
    void deleteAll();

    @Query("SELECT * from steps_table ORDER BY date")
    LiveData<List<DailySteps>> getAllDailySteps();
}
