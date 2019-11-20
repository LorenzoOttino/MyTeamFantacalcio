package com.example.myteamfantacalcio.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static PlayerApi playerApi = retrofit.create(PlayerApi.class);

    public static PlayerApi getPlayerApi(){
        return playerApi;
    }
}
