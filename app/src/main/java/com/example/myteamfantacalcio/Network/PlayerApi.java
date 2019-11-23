package com.example.myteamfantacalcio.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
*   In this app players are retrieved from pokemon API because there are not unrestricted
*   APIs for this kind of object. Therefore pokemons will act as football players in this
*   app.
*/

public interface PlayerApi {

    @GET("api/v2/pokemon/{name}")
    Call<PlayerResponse> getPlayer(@Path("name") String name);
}
