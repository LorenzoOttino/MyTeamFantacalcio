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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class PalmaresActivity extends AppCompatActivity {

    CompetitionViewModel competitionViewModel;

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
        cupsAdapter = new CupsAdapter();
        cupsList.setAdapter(cupsAdapter);

        competitionViewModel = ViewModelProviders.of(this).get(CompetitionViewModel.class); //This is the Log error message: "Emulator: Trying to erase a non-existent color buffer with handle 0"
        competitionViewModel.getAllCompetitions().observe(this, new Observer<List<Competition>>() {
            @Override
            public void onChanged(List<Competition> competitions) {
                if(!competitions.isEmpty())
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
            competitionViewModel.insertCompetition(new Competition(comp, year, pos));
            Toast.makeText(getApplicationContext(), R.string.message_done, Toast.LENGTH_SHORT).show();
        }
        if((requestCode == 1) &&(resultCode == 2)){
            competitionViewModel.deleteAllCompetitions();
            Toast.makeText(getApplicationContext(), R.string.message_done, Toast.LENGTH_SHORT).show();
        }
    }

    public class CupsAdapter extends RecyclerView.Adapter<CupsAdapter.ViewHolder>{
        private List<Competition> allCompetitions = new ArrayList<>();

        public CupsAdapter() {
            allCompetitions.add(new Competition("er","1","asdsa"));
        }

        public void setCompetitions(List<Competition> competitions){
            this.allCompetitions = competitions;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.competition_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.compName.setText(allCompetitions.get(position).getName());
            holder.compYear.setText(allCompetitions.get(position).getYear());
            holder.compPos.setText(allCompetitions.get(position).getPosition());
        }

        @Override
        public int getItemCount() {
            int size = 0;
            if(allCompetitions != null)
                size = allCompetitions.size();
            return size;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView compName;
            TextView compYear;
            TextView compPos;

            ViewHolder(View view){
                super(view);
                compName = findViewById(R.id.textCompetitionName);
                compYear = findViewById(R.id.textCompetitionYear);
                compPos = findViewById(R.id.textCompetitionPos);
            }
        }
    }
}
