package com.example.myteamfantacalcio.Database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
