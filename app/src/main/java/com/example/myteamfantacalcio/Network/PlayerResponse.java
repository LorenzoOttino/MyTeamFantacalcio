package com.example.myteamfantacalcio.Network;

public class PlayerResponse {
    private int id;             //Player id
    private String name;        //Player name
    private int[] stats;        //Array of marks in each match day

    public Player getPlayer(){
        return new Player(id, name, stats);
    }
}
