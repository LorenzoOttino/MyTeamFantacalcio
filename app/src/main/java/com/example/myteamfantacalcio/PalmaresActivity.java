package com.example.myteamfantacalcio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myteamfantacalcio.Adapters.CupsAdapter;
import com.example.myteamfantacalcio.Database.Competition;
import com.example.myteamfantacalcio.Database.CompetitionViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class PalmaresActivity extends AppCompatActivity implements CupsAdapter.OnListItemClickListener{

    CompetitionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palmares);
        //Navigation drawer components
        Toolbar toolbar = findViewById(R.id.toolbarPalm);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_palmares);
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
                        Toast.makeText(getApplicationContext(), R.string.message_no_change, Toast.LENGTH_SHORT).show();
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
        //Layout components
        RecyclerView cupsList;
        final CupsAdapter cupsAdapter;
        cupsList = findViewById(R.id.cupsRecyclerView);
        cupsList.hasFixedSize();
        cupsList.setLayoutManager(new LinearLayoutManager(this));

        //Adapter for RecyclerView
        cupsAdapter = new CupsAdapter(this);
        cupsList.setAdapter(cupsAdapter);

        viewModel = ViewModelProviders.of(this).get(CompetitionViewModel.class);
        viewModel.getAllCompetitions().observe(this, new Observer<List<Competition>>() {
            @Override
            public void onChanged(List<Competition> competitions) {
                    cupsAdapter.setCompetitions(competitions);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.palmares_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent palm;
        if (item.getItemId() == R.id.topActionPalm) {
            palm = new Intent(getApplicationContext(), AddToPalmaresActivity.class);
            startActivityForResult(palm, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 1) &&(resultCode == 1)){
            String comp = data.getStringExtra("COMPETITION_EXTRA");
            String year = data.getStringExtra("YEAR_EXTRA");
            String pos = data.getStringExtra("POSITION_EXTRA");
            viewModel.insertCompetition(new Competition(comp, year, pos));
            Toast.makeText(getApplicationContext(), R.string.message_done, Toast.LENGTH_SHORT).show();
        }
        if((requestCode == 1) &&(resultCode == 2)){
            viewModel.deleteAllCompetitions();
            Toast.makeText(getApplicationContext(), R.string.message_done, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        viewModel.deleteSingleCompetition(viewModel.getAllCompetitions().getValue().get(clickedItemIndex));
    }
}
