package com.example.myteamfantacalcio;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myteamfantacalcio.Database.Competition;
import com.example.myteamfantacalcio.Network.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamViewModel extends AndroidViewModel {
    private TeamRepository teamRepository;

    public TeamViewModel(Application application){
        super(application);
        teamRepository = TeamRepository.getInstance(application);
    }

    public void insertCompetition(Competition competition){
        teamRepository.insert(competition);
    }

    public void deleteAllCompetitions(){
        teamRepository.deleteAllCompetitions();
    }

    public void deleteSingleCompetition(Competition competition){
        teamRepository.delete(competition);
    }

    public LiveData<List<Competition>> getAllCompetitions(){
        return teamRepository.getAllCompetitions();
    }

    public LiveData<List<Player>> loadAllPlayers(SharedPreferences preferences){
        return teamRepository.loadAllPlayers(preferences);
    }

    public ArrayList<String> loadAllPlayersStrings(SharedPreferences preferences){
        return teamRepository.loadAllPlayersStrings(preferences);
    }

    public void updatePlayerString(SharedPreferences preferences, int clickedItemIndex, String message){
        teamRepository.updatePlayerString(preferences, clickedItemIndex, message);
    }
}
