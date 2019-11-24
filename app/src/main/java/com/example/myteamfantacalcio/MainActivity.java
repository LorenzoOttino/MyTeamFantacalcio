package com.example.myteamfantacalcio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myteamfantacalcio.Adapters.FullPlayerAdapter;
import com.example.myteamfantacalcio.Network.Player;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FullPlayerAdapter.OnListItemClickListener{

    private static final int RC_SIGN_IN = 123;
    TeamViewModel teamViewModel;
    FirebaseUser user;
    RecyclerView fullPlayersList;
    ArrayList<Player> teamPlayers;
    TextView res;
    TextView matchDay;
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
        teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
        calc = findViewById(R.id.calculateButton);
        matchDay = findViewById(R.id.matchDayField);
        res = findViewById(R.id.scoreField);
        matchDay.setText("0");
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

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
        //teamPlayers = teamViewModel.loadAllPlayers(sharedPreferences);
        /*int[] vett = {0,1,2,3,4};
        teamPlayers = new ArrayList<>();
        teamPlayers.add(new Player(1, "A", vett));
        teamPlayers.add(new Player(2, "B", vett));
        teamPlayers.add(new Player(3, "C", vett));
        teamPlayers.add(new Player(4, "D", vett));
        teamPlayers.add(new Player(5, "E", vett));
        teamPlayers.add(new Player(6, "F", vett));
        teamPlayers.add(new Player(7, "G", vett));
        teamPlayers.add(new Player(8, "H", vett));
        teamPlayers.add(new Player(9, "I", vett));
        teamPlayers.add(new Player(10, "J", vett));
        teamPlayers.add(new Player(11, "K", vett));
        teamPlayers.add(new Player(12, "L", vett));
        teamPlayers.add(new Player(13, "M", vett));
        teamPlayers.add(new Player(14, "N", vett));
        teamPlayers.add(new Player(15, "O", vett));*/
        final FullPlayerAdapter fullPlayersAdapter = new FullPlayerAdapter(this);
        fullPlayersList.setAdapter(fullPlayersAdapter);

        teamViewModel.loadAllPlayers(sharedPreferences).observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                fullPlayersAdapter.setPlayers(players);
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
        String val = matchDay.getText().toString();
        int day = Integer.parseInt(val);
        if(teamPlayers != null) {
            if (day < teamPlayers.get(0).getMarks().length) {
                int result = 0;
                int[] v;

                for (Player p : teamPlayers) {
                    if (p.isStarter()) {
                        v = p.getMarks();
                        result += v[day];
                    }
                }
                res.setText(Integer.toString(result));
            } else
                Toast.makeText(getApplicationContext(), getString(R.string.meaasge_insert_day), Toast.LENGTH_SHORT).show();
        }
    }

    public void plus(View v){
        int val = Integer.parseInt(matchDay.getText().toString());
        val++;
        matchDay.setText(String.valueOf(val));
    }

    public void less(View v){
        int val = Integer.parseInt(matchDay.getText().toString());
        val--;
        if (val < 0) val = 0;
        matchDay.setText(String.valueOf(val));
    }
}
