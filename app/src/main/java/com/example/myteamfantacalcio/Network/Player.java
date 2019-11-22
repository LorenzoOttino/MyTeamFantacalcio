package com.example.myteamfantacalcio.Network;

public class Player {
    private int id;
    private String name;
    private int[] marks;
    private boolean starter;

    public Player(int id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
        starter = false;
    }

    public int getId() {
        return id;
    }

    public boolean isStarter() {
        return starter;
    }

    public void setStarter(boolean starter) {
        this.starter = starter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getMarks() {
        return marks;
    }

    public void setMarks(int[] marks) {
        this.marks = marks;
    }
}
