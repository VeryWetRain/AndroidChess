package com.example.androidchess91;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int x,y;

    public Coordinate(int x , int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString(){
        return x+","+y;
    }

    public boolean equals(Object o){
        if(o instanceof Coordinate){
            Coordinate toCompare = (Coordinate) o;
            return this.x == toCompare.getX() && this.y == toCompare.getY();
        }
        return false;
    }
}
