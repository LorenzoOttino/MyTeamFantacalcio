package com.example.myteamfantacalcio.Network;

public class PlayerResponse {
    private int id;             //Player id
    private String name;        //Player name
    //private Stat[] stats;      //Array of marks in each match day

    public Player getPlayer(){
        int[] v = {0,1,2,3};
        return new Player(id, name, v); //getStatsVector()
    }

    /*public int[] getStatsVector(){
        int[] v = new int[6];
        int i = 0;
        for(Stat s : stats){
            v[i] = s.getBaseStat();
        }
        return v;
    }

    private class Stat{
        private int base_stat;

        int getBaseStat(){return base_stat;}
    }*/
}
