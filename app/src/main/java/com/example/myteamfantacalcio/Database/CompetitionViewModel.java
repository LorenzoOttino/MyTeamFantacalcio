package com.example.myteamfantacalcio.Database;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myteamfantacalcio.Database.Competition;
import com.example.myteamfantacalcio.Database.CompetitionRepostiory;

import java.util.ArrayList;
import java.util.List;

public class CompetitionViewModel extends AndroidViewModel {
    private CompetitionRepostiory competitionRepostiory;

    public CompetitionViewModel(Application application){
        super(application);
        competitionRepostiory = CompetitionRepostiory.getInstance(application);
    }

    public void insertCompetition(Competition competition){
        competitionRepostiory.insert(competition);
    }

    public void deleteAllCompetitions(){
        competitionRepostiory.deleteAllCompetitions();
    }

    public void deleteSingleCompetition(Competition competition){
        competitionRepostiory.delete(competition);
    }

    public LiveData<List<Competition>> getAllCompetitions(){
        return competitionRepostiory.getAllCompetitions();
    }
}
