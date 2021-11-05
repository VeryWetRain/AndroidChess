package com.example.androidchess91;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {
    Button play, recordedgames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        recordedgames = findViewById(R.id.recordedgames);
    }

    public void play(View view){
        Intent intent = new Intent(this, Chess.class);
        startActivity(intent);
    }

    public void records(View view){
        Intent intent = new Intent(this,Records.class);
        startActivity(intent);
    }
}