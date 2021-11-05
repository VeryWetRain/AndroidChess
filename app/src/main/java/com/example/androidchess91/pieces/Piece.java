package com.example.androidchess91.pieces;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchess91.Chess;

import java.io.Serializable;

/**
 * Represents a generic piece on the chess board.
 * @author Bryan Law
 * @author Ryan Kim
 *
 */
public abstract class Piece implements Serializable {
    /**
     * vertical and horizontal position of piece on the board
     */
    protected int xPos;
    protected int yPos;

    /**
     * true for white, false for black
     */
    protected boolean color;
    protected boolean moved;

    /**
     * Moves a chess piece to the given position.
     *
     * @param boardObj 	Object refering to the chessboard.
     * @param i			Translated x position for destination.
     * @param j			Translated y position for destination.
     * @return			true if move is successful, otherwise false
     */
    public abstract boolean move(Board boardObj, int i, int j, boolean swap, AppCompatActivity context);

    /**
     * Determines if the current piece puts the King piece in check.
     *
     * @param boardObj	Object refering to the chessboard.
     * @param x			Translated x position of the King.
     * @param y			Translated y positions of the King.
     * @return			true if the King is in check, otherwise false.
     */
    public abstract boolean check(Board boardObj, int x, int y);

    /**
     * Get method for the Piece's color.
     *
     * @return color of Piece
     */
    public boolean getColor() {
        return color;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved() {
        return moved;
    }
}
