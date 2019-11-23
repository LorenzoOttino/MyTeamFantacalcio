package com.example.myteamfantacalcio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ClubDescriptionActivity extends AppCompatActivity {
    ImageView clubLogo;
    TextView clubName;
    TextView clubFoundation;
    TextView clubStadium;
    TextView clubCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_description);
        //Navigation drawer components
        Toolbar toolbar = findViewById(R.id.toolbarClub);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_club);
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
                        Toast.makeText(getApplicationContext(), R.string.message_no_change, Toast.LENGTH_SHORT).show();
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
        //Layout components
        clubLogo = findViewById(R.id.desc_club_logo);
        clubName = findViewById(R.id.desc_club_name);
        clubFoundation = findViewById(R.id.desc_foundation);
        clubStadium = findViewById(R.id.desc_stadium);
        clubCoach = findViewById(R.id.desc_coach);
        SharedPreferences sharedPreferences = getSharedPreferences("club_preferences", MODE_PRIVATE);
        updateFields(sharedPreferences);
        clubLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeImage = new Intent(Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(changeImage, RESULT_OK);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){//Honestly I took this part from https://www.viralpatel.net/pick-image-from-galary-android-app/
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = findViewById(R.id.desc_club_logo);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.club_description_up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LinearLayout linearLayout = findViewById(R.id.desc_edit_layout);
        linearLayout.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    public void onClickOnEditButton(View view){
        EditText[] editTexts = new EditText[5];
        editTexts[0] = findViewById(R.id.editTeamName);
        editTexts[1] = findViewById(R.id.editFoundationYear);
        editTexts[2] = findViewById(R.id.editFounder);
        editTexts[3] = findViewById(R.id.editStadium);
        editTexts[4] = findViewById(R.id.editCoach);

        SharedPreferences sharedPreferences = getSharedPreferences("club_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String add;
        for (int i = 0; i < 5; i++){
            add = editTexts[i].getText().toString();
            if(add.length() > 0)
                editor.putString("field" + i, add);
        }
        editor.apply();
        LinearLayout linearLayout = findViewById(R.id.desc_edit_layout);
        linearLayout.setVisibility(View.GONE);
        updateFields(sharedPreferences);

    }

    public void updateFields(SharedPreferences sharedPreferences){
        clubName.setText(sharedPreferences.getString("field0", "NOT FOUND"));
        clubFoundation.setText(String.format("%s %s %s %s",
                getResources().getString(R.string.desc_foundation_year)
                , sharedPreferences.getString("field1", "NOT FOUND")
                , getResources().getString(R.string.desc_by)
                , sharedPreferences.getString("field2", "NOT FOUND")));
        clubStadium.setText(String.format("%s %s",
                getResources().getString(R.string.desc_stadium)
                ,sharedPreferences.getString("field3", "NOT FOUND")));
        clubCoach.setText(String.format("%s %s", getResources().getString(R.string.desc_coach)
                ,sharedPreferences.getString("field4", "NOT FOUND")));
    }
}
