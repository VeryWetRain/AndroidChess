package com.example.androidchess91;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class GameRecord implements Serializable {
    private ArrayList<Move> moveHistory;
    private Date date;
    private String title;

    public GameRecord(String title, ArrayList<Move> moveHistory){
        this.title = title;
        date = new Date(System.currentTimeMillis());
        this.moveHistory= moveHistory;
    }

    public ArrayList<Move> getMoveHistory() {
        return moveHistory;
    }

    @Override
    public String toString() {
        return title+ "\n"+ date.toString();
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }
}
