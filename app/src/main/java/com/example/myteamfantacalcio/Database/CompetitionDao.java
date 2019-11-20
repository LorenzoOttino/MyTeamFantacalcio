package com.example.myteamfantacalcio.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myteamfantacalcio.Database.Competition;

import java.util.List;
@Dao
public interface CompetitionDao {
    @Insert
    void insert(Competition competition);
    @Delete
    void delete(Competition competition);
    @Query("DELETE FROM competitions_table")
    void deleteAllCompetitions();
    @Query("SELECT * FROM competitions_table")
    LiveData<List<Competition>> getAllCompetitions();
}
