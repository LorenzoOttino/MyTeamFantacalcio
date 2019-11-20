package com.example.myteamfantacalcio.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "competitions_table", primaryKeys = {"name", "year"})
public class Competition {
    @NonNull
    private String name;
    @NonNull
    private String year;
    private String position;

    public Competition(String name, String year, String position){
        this.name = name;
        this.position = position;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getYear(){
        return year;
    }

    public String getPosition() {
        return position;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setYear(String year){
        this.year = year;
    }

    public void setPosition(String position){
        this.position = position;
    }
}
