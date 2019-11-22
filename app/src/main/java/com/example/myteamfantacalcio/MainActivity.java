package com.example.myteamfantacalcio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myteamfantacalcio.Adapters.FullPlayerAdapter;
import com.example.myteamfantacalcio.Network.Player;
import com.example.myteamfantacalcio.Network.PlayerApi;
import com.example.myteamfantacalcio.Network.PlayerResponse;
import com.example.myteamfantacalcio.Network.ServiceGenerator;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FullPlayerAdapter.OnListItemClickListener{

    private static final int RC_SIGN_IN = 123;
    FirebaseUser user;
    RecyclerView fullPlayersList;
    RecyclerView.Adapter fullPlayersAdapter;
    ArrayList<Player> teamPlayers;
    TextView res;
    EditText matchDay;
    Button calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Navigation drawer components
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent toNext;
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), R.string.message_no_change, Toast.LENGTH_SHORT).show();                        return true;
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
                        toNext = new Intent(getApplicationContext(), PlayersActivity.class);
                        startActivity(toNext);
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
        //Layout
        calc = findViewById(R.id.calculateButton);
        matchDay = findViewById(R.id.matchDayField);
        res = findViewById(R.id.scoreField);

        //Login with firebase
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            createSignInIntent();
        }else {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.login_welcome) + user.getDisplayName(),
                    Toast.LENGTH_SHORT).show();
        }

        //Players Recyclerview
        fullPlayersList = findViewById(R.id.mainRecyclerview);
        fullPlayersList.hasFixedSize();
        fullPlayersList.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences = getSharedPreferences("players_preferences", MODE_PRIVATE);
        teamPlayers = loadAllPlayers(sharedPreferences);
        fullPlayersAdapter = new FullPlayerAdapter(teamPlayers, this);
        fullPlayersList.setAdapter(fullPlayersAdapter);

    }

    public ArrayList<Player> loadAllPlayers(SharedPreferences preferences){
        ArrayList<String> players = new ArrayList<>();
        if(teamPlayers == null)
            teamPlayers = new ArrayList<>();
        else teamPlayers.clear();
        String key;

        for (int i = 0; i < 23; i++){
            key = "player" + i;
            players.add(preferences.getString(key, "EMPTY_SLOT"));
            if(!players.get(i).equals("EMPTY_SLOT"))
                requestPlayer(players.get(i));
        }
        return teamPlayers;
    }

    public void requestPlayer(String playerName){
        PlayerApi playerApi = ServiceGenerator.getPlayerApi();
        Call<PlayerResponse> call = playerApi.getPlayer(playerName);

        call.enqueue(new Callback<PlayerResponse>() {
            @Override
            public void onResponse(Call<PlayerResponse> call, Response<PlayerResponse> response) {
                if(response.code() == 200){
                    teamPlayers.add(response.body().getPlayer());
                }
            }

            @Override
            public void onFailure(Call<PlayerResponse> call, Throwable t) {
                Log.i("Retrofit", "Could not load player");
            }
        });
    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                 user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                createSignInIntent();
            }
        }
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        user = null;
                        finish();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        signOut();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        boolean starts = teamPlayers.get(clickedItemIndex).isStarter();
        teamPlayers.get(clickedItemIndex).setStarter(!starts);
    }

    public void calculateResult(){
        int day = Integer.parseInt(matchDay.getText().toString());
        int result = 0;

        for(Player p : teamPlayers){
            if(p.isStarter())
                result += p.getMarks()[day];
        }
        res.setText(result);
    }
}
