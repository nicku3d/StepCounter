package com.gmail.nicku3d.stepcounter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "steps_table")
public class DailySteps {

    @NonNull
    @ColumnInfo(name = "steps")
    private int steps;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    public DailySteps (@NonNull int steps, @NonNull String date) {
        this.steps = steps;
        //java.util.Date utilDateNow = new java.util.Date();
        this.date = date;//new java.sql.Date(utilDateNow.getTime()).toString();
    }

    public int getSteps() {
        return this.steps;
    }

    public String getDate() {
        return this.date;
    }
}
