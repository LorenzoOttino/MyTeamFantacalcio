package com.example.myteamfantacalcio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddToPalmaresActivity extends AppCompatActivity {
    EditText comp;
    EditText year;
    EditText pos;
    Button add;
    Button clear;
    Button exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_palmares);

        comp = findViewById(R.id.competitionField);
        year = findViewById(R.id.yearField);
        pos = findViewById(R.id.positionField);
        add = findViewById(R.id.addToPalmares);
        clear = findViewById(R.id.clearPalmares);
        exit = findViewById(R.id.exitAddToPalmares);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!comp.getText().toString().trim().isEmpty()) &&
                        (!year.getText().toString().trim().isEmpty()) &&
                        (!pos.getText().toString().trim().isEmpty())){

                    String compMex = comp.getText().toString();
                    String yearMex = year.getText().toString();
                    String posMex = pos.getText().toString();

                    Intent data = new Intent();
                    data.putExtra("COMPETITION_EXTRA", compMex);
                    data.putExtra("YEAR_EXTRA", yearMex);
                    data.putExtra("POSITION_EXTRA", posMex);
                    setResult(1, data);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.message_invalid_input, Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(2, data);
                finish();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(3, data);
                finish();
            }
        });
    }
}
