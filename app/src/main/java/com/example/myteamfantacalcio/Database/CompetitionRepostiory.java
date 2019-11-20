package com.example.myteamfantacalcio.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CompetitionRepostiory {
    private CompetitionDao competitionDao;
    private static CompetitionRepostiory instance;
    private LiveData<List<Competition>> allCompetitions;

    private CompetitionRepostiory(Application application){
        CompetitionDatabase database = CompetitionDatabase.getInstance(application);
        competitionDao = database.compDao();
        allCompetitions = competitionDao.getAllCompetitions();
    }

    public static synchronized CompetitionRepostiory getInstance(Application application){
        if(instance == null)
            instance = new CompetitionRepostiory(application);
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
}
