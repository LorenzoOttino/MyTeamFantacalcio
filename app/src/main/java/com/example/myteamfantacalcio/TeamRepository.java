package com.example.myteamfantacalcio;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.myteamfantacalcio.Database.Competition;
import com.example.myteamfantacalcio.Database.CompetitionDao;
import com.example.myteamfantacalcio.Database.CompetitionDatabase;
import com.example.myteamfantacalcio.Network.Player;
import com.example.myteamfantacalcio.Network.PlayerApi;
import com.example.myteamfantacalcio.Network.PlayerResponse;
import com.example.myteamfantacalcio.Network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamRepository {
    private CompetitionDao competitionDao;
    private static TeamRepository instance;
    private LiveData<List<Competition>> allCompetitions;
    private MutableLiveData<List<Player>> allPlayers;
    private MutableLiveData<Player> requestedPlayer;
    private List<Player> playerList;
    private Observer<Player> observer;

    private TeamRepository(Application application){
        CompetitionDatabase database = CompetitionDatabase.getInstance(application);
        competitionDao = database.compDao();
        allCompetitions = competitionDao.getAllCompetitions();
        allPlayers = new MutableLiveData<>();
        requestedPlayer = new MutableLiveData<>();
        playerList = new ArrayList<>();
        observer = new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                if(!playerList.contains(player))
                    playerList.add(player);
            }
        };
    }

    public static synchronized TeamRepository getInstance(Application application){
        if(instance == null)
            instance = new TeamRepository(application);
        return instance;
    }

    public LiveData<List<Competition>> getAllCompetitions(){
        return allCompetitions;
    }

    public void insert(Competition competition){
        new InsertCompetitionAsync(competitionDao).execute(competition);
    }

    public void delete(Competition competition){
        new DeleteSingleCompetitionAsync(competitionDao).execute(competition);
    }

    public void deleteAllCompetitions(){
        new DeleteAllCompetitionsAsync(competitionDao).execute();
    }

    private static class InsertCompetitionAsync extends AsyncTask<Competition, Void, Void> {
        private CompetitionDao competitionDao;

        InsertCompetitionAsync(CompetitionDao competitionDao){
            this.competitionDao = competitionDao;
        }

        @Override
        protected Void doInBackground(Competition... competitions) {
            competitionDao.insert(competitions[0]);
            return null;
        }
    }

    private static class DeleteAllCompetitionsAsync extends AsyncTask<Void, Void, Void>{
        private CompetitionDao competitionDao;

        DeleteAllCompetitionsAsync(CompetitionDao competitionDao){
            this.competitionDao = competitionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            competitionDao.deleteAllCompetitions();
            return null;
        }
    }

    private static class DeleteSingleCompetitionAsync extends AsyncTask<Competition, Void, Void>{
        private CompetitionDao competitionDao;

        DeleteSingleCompetitionAsync(CompetitionDao competitionDao){
            this.competitionDao = competitionDao;
        }

        @Override
        protected Void doInBackground(Competition... competitions) {
            competitionDao.delete(competitions[0]);
            return null;
        }
    }

    public LiveData<List<Player>> loadAllPlayers(SharedPreferences sharedPreferences){
        ArrayList<String> players = new ArrayList<>();
        String key;
        String val;
        playerList = new ArrayList<>();

        for (int i = 0; i < 23; i++){
            key = "player" + i;
            val = sharedPreferences.getString(key, "EMPTY_SLOT");
            if(!val.equals("EMPTY_SLOT")){
                players.add(val);
                requestPlayer(players.get(i));
            }
        }//requestPlayer("caterpie");

        return allPlayers;
    }

    public List<Player> getAllPlayers(){
        return playerList;
    }

    public ArrayList<String> loadAllPlayersStrings(SharedPreferences preferences){
        ArrayList<String> players = new ArrayList<>();
        String key;

        for (int i = 0; i < 23; i++){
            key = "player" + i;
            players.add(preferences.getString(key, "EMPTY_SLOT"));
        }
        return players;
    }

    public void updatePlayerString(SharedPreferences preferences, int clickedItemIndex, String message){
        String key = "player" + clickedItemIndex;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, message);
        editor.apply();

    }

    public void requestPlayer(String playerName){
        PlayerApi playerApi = ServiceGenerator.getPlayerApi();
        Call<PlayerResponse> call = playerApi.getPlayer(playerName);

        call.enqueue(new Callback<PlayerResponse>() {
            @Override
            public void onResponse(Call<PlayerResponse> call, Response<PlayerResponse> response) {
                if(response.isSuccessful()){
                    requestedPlayer.postValue(response.body().getPlayer());
                    requestedPlayer.observeForever(observer);
                    allPlayers.postValue(playerList);
                }
            }

            @Override
            public void onFailure(Call<PlayerResponse> call, Throwable t) {
                Log.i("Retrofit", "Could not load player");
            }
        });
    }

    public MutableLiveData<Player> getRequestedPlayer(){
        return requestedPlayer;
    }

    public Observer<Player> getObserver(){
        return observer;
    }

}
