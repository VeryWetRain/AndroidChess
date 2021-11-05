package com.example.androidchess91;

import com.example.androidchess91.pieces.Piece;

import java.io.Serializable;

public class Move implements Serializable {
    public Coordinate start;
    public Coordinate end;
    public Piece promote;

    public Move(Coordinate start, Coordinate end){
        this.start = start;
        this.end = end;
        this.promote = null;
    }

    public Move(Coordinate start, Coordinate end, Piece promote){
        this.start = start;
        this.end = end;
        this.promote = promote;
    }
}
