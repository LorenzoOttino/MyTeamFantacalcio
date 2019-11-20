package com.example.myteamfantacalcio.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Competition.class}, version = 1)
public abstract class CompetitionDatabase extends RoomDatabase {
    private static CompetitionDatabase instance;
    public abstract CompetitionDao compDao();

    public static synchronized CompetitionDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder
                    (context.getApplicationContext(), CompetitionDatabase.class, "competition_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
