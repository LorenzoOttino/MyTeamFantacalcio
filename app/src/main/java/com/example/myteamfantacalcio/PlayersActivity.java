package com.example.myteamfantacalcio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myteamfantacalcio.Adapters.PlayerNameAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity implements PlayerNameAdapter.OnListItemClickListener {

    RecyclerView playerNames;
    RecyclerView.Adapter playerNameAdapter;
    TeamViewModel teamViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        //Navigation drawer components
        Toolbar toolbar = findViewById(R.id.toolbarPlayers);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_players);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent toNext;
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        toNext = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(toNext);
                        finish();
                        return true;
                    case R.id.nav_club:
                        toNext = new Intent(getApplicationContext(), ClubDescriptionActivity.class);
                        startActivity(toNext);
                        finish();
                        return true;
                    case R.id.nav_palmares:
                        toNext = new Intent(getApplicationContext(), PalmaresActivity.class);
                        startActivity(toNext);
                        finish();
                        return true;
                    case R.id.nav_squad:
                        Toast.makeText(getApplicationContext(), R.string.message_no_change, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        //Players Recyclerview and SharedPreferences
        playerNames = findViewById(R.id.playersRecyclerview);
        playerNames.hasFixedSize();
        playerNames.setLayoutManager(new LinearLayoutManager(this));
        teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
        ArrayList<String> allPlayers;
        SharedPreferences sharedPreferences = getSharedPreferences("players_preferences", MODE_PRIVATE);
        allPlayers = teamViewModel.loadAllPlayersStrings(sharedPreferences);

        playerNameAdapter = new PlayerNameAdapter(allPlayers, this);
        playerNames.setAdapter(playerNameAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String message) {
        SharedPreferences preferences = getSharedPreferences("players_preferences", MODE_PRIVATE);
        teamViewModel.updatePlayerString(preferences, clickedItemIndex, message);
        Toast.makeText(getApplicationContext(), R.string.message_done, Toast.LENGTH_SHORT).show();
    }
}
