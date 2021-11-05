package com.example.androidchess91;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class Records extends AppCompatActivity {
    private ListView recordlist;
    private ArrayList<GameRecord> records;
    private Object[] saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            loadRecords();
        } catch (IOException e) {
            e.printStackTrace();
            records = new ArrayList<GameRecord>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        recordlist = findViewById(R.id.recordlist);
        recordlist.setAdapter(new ArrayAdapter<GameRecord>(this, R.layout.record, records));
        recordlist.setOnItemClickListener((p, V, pos, id) ->
                showGame(pos));
    }

    private void showGame(int pos) {
        Bundle bundle = new Bundle();
        GameRecord record = records.get(pos);
        bundle.putSerializable("game", record);
        Intent intent = new Intent(this, replay.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadRecords() throws IOException, ClassNotFoundException {
        FileInputStream fis = openFileInput("data.dat");
        ObjectInputStream is = new ObjectInputStream(fis);
        saveData = (Object[]) is.readObject();
        records = (ArrayList<GameRecord>) saveData[4];
    }

    public void sortTitle(View view){
        records.sort(new Comparator<GameRecord>() {
            @Override
            public int compare(GameRecord o1, GameRecord o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        recordlist.setAdapter(new ArrayAdapter<GameRecord>(this, R.layout.record, records));
    }

    public void sortDate(View view){
        records.sort(new Comparator<GameRecord>() {
            @Override
            public int compare(GameRecord o1, GameRecord o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        recordlist.setAdapter(new ArrayAdapter<GameRecord>(this, R.layout.record, records));
    }
}