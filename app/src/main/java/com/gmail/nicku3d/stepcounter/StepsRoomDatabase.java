package com.gmail.nicku3d.stepcounter;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DailySteps.class}, version = 1, exportSchema = false)
public abstract class StepsRoomDatabase extends RoomDatabase {
    public abstract StepsDao stepsDao();

    private static StepsRoomDatabase INSTANCE;

    public static StepsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StepsRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StepsRoomDatabase.class, "steps_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
