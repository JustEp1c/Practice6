package com.mirea.sumachev.room;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public abstract class DataBase extends RoomDatabase {

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract EmployeeDAO employeeDao();

    @Database(entities = {Employee.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        public abstract EmployeeDAO employeeDao();
    }

}
