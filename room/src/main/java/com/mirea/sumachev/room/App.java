package com.mirea.sumachev.room;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    public static App instance;
    private DataBase.AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DataBase.AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }
    public static App getInstance() {
        return instance;
    }
    public DataBase.AppDatabase getDatabase() {
        return database;
    }
}
