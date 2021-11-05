package com.example.androidchess91.pieces;

import android.util.Pair;

import com.example.androidchess91.Coordinate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Board object to hold the matrix of pieces, information about pieces, and the state of the game
 *
 * @author Bryan Law
 * @author Ryan Kim
 */

public class Board implements Serializable {
    private Piece[][] board;
    /**
     * Keeps track of whether the last move was a double step from a pawn
     */
    public boolean double_stepped_white;
    public boolean double_stepped_black;

    /**
     * Holding the x and y position of the white and black King in order to determine check
     */
    private int wKingX, wKingY;
    private int bKingX, bKingY;

    /**
     * Keeps track of whether the board is in check and in checkmate
     */
    private boolean check = false;
    private boolean checkMate = false;

    public ArrayList<Coordinate> validMoves;

    /**
     * Class Constructor for chess board. Creates an 8x8 array of Pieces in initial chess formation
     */
    public Board() {
        board = new Piece[8][8];
        double_stepped_white = false;
        double_stepped_black = false;
        bKingX = 0;
        bKingY = 4;
        wKingX = 7;
        wKingY = 4;
        validMoves = new ArrayList<>();

        //Initialize the chess board with initial placements of the pieces
        for(int i=0; i<8; i++) {
            for(int j=0; j<8;j++) {
                if(i==0) {
                    //initialize the black pieces in first row
                    if(j==2 || j== 5) board[i][j] = new Bishop(i,j, false);
                    if(j==0 || j== 7) board[i][j] = new Rook(i,j,false);
                    if(j==1 || j== 6) board[i][j] = new Knight(i,j, false);
                    if(j==3) board[i][j] = new Queen(i,j, false);
                    if(j==4) board[i][j] = new King(i,j,false);
                }
                else if(i==1) {
                    //all black pawns
                    board[i][j] = new Pawn(i,j, false);
                }
                else if(i==6) {
                    //all white pawns
                    board[i][j] = new Pawn(i,j, true);
                }
                else if(i==7) {
                    //initialize the white pieces in the last row
                    if(j==2 || j== 5) board[i][j] = new Bishop(i,j,true);
                    if(j==0 || j== 7) board[i][j] = new Rook(i,j,true);
                    if(j==1 || j== 6) board[i][j] = new Knight(i,j,true);
                    if(j==3) board[i][j] = new Queen(i,j, true);
                    if(j==4) board[i][j] = new King(i,j,true);
                }
                //Black and white empty spaces are treated as null
                else board[i][j] = null;
            }
        }
    }

    /**
     * Determines if the board is in a check state.
     *
     * @param white Boolean for whose turn it is.
     */
    public void check(boolean white) {
        for(int i=0;i<8; i++) {
            for(int j=0;j<8;j++) {
                if(board[i][j]!=null && !(board[i][j]instanceof King) && board[i][j].getColor()!=(white)) {
                    if(white && board[i][j].check(this, wKingX, wKingY)) {
                        check = true;
                        return;
                    }else if (!white && board[i][j].check(this, bKingX, bKingY)) {
                        check = true;
                        return;
                    }
                }
            }
        }
        check = false;
    }

    /**
     * Determines if the given board is in checkmate.
     *
     * @param boardObj Object referring to the chess board.
     * @param white Boolean for whose turn it is.
     * @return true if in checkmate, otherwise false
     */
    public boolean checkMate(Board boardObj, boolean white) {
        Piece[][] board = boardObj.getBoard();
        if(!boardObj.getCheck()) return false;
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                if(board[i][j]!=null && board[i][j].getColor()== white) {
                    for(int x=0;x<8;x++) {
                        for(int y=0; y<8; y++) {
                            if(board[i][j].check(boardObj, x, y)) {
                                Piece temp = board[x][y];
                                board[x][y] = board[i][j];
                                board[i][j] = null;
                                if(board[x][y] instanceof King) {
                                    this.setKing(white, x, y);
                                }
                                boardObj.check(white);
                                board[i][j] = board[x][y];
                                board[x][y] = temp;
                                if(board[i][j] instanceof King) {
                                    this.setKing(white, i, j);
                                }
                                if(!boardObj.getCheck()) {
                                    check = true;
                                    checkMate = false;
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        checkMate = true;
        return true;
    }

    /**
     * Sets the position of either white or black king
     *
     * @param color Sets white or black kings based on string color
     * @param x New x position to set the king
     * @param y New y position to set the king
     */
    public void setKing(boolean color, int x, int y) {
        if(color) {
            wKingX=x;
            wKingY=y;
        }else {
            bKingX=x;
            bKingY=y;
        }
    }

    /**
     * Gets piece at specified position on the board
     *
     * @param x X position on the board
     * @param y Y position on the board
     * @return The Piece located at board position x,y
     */

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    /**
     * Gets the board for use outside of the object
     * @return The board double array associated with the board object
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Returns whether the board is in check or not
     * @return State of board in check or not
     */
    public boolean getCheck() {
        return check;
    }

    /**
     * Returns whether the board is in checkmate or not
     * @return State of board in checkmate or not
     */
    public boolean getCheckmate() {
        return checkMate;
    }

    public Board deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            baos.close();
            oos.close();
            bais.close();
            ois.close();
            return (Board) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

}
