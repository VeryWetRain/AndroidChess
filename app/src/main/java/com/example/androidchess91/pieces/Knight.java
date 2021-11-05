package com.example.androidchess91.pieces;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchess91.Chess;

/**
 * Knight Piece
 * @author Bryan Law
 * @author Ryan Kim
 *
 */
public class Knight extends Piece{
    /**
     * Class constructor fields inherited from Piece
     * @param x Vertical position on the board
     * @param y Horizontal position on the board
     * @param color true for white, false for black
     */
    public Knight(int x, int y, boolean color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
    }

    /**
     * Checks if knight moving to new position would be a valid move and doesn't cause a check for own king
     * Moves in L shape. If moving two horizontal then moves one vertical and if two vertical than one horizontal space
     *
     * @param boardObj board object to check for collisions with other pieces
     * @param x vertical position to be moved to
     * @param y horizontal position to be moved to
     * @return Whether to new move to x and y would be valid
     */
    public boolean move(Board boardObj, int x, int y, boolean swap, AppCompatActivity context) {
        Piece[][] board = boardObj.getBoard();
        if(board[x][y]!=null && board[x][y].getColor() == this.color || xPos==x && yPos==y) return false;

        if (Math.abs(xPos-x)==2 && Math.abs(yPos-y)==1 || Math.abs(xPos-x)==1 && Math.abs(yPos-y)==2){
            Piece temp = board[x][y];
            board[xPos][yPos] = null;
            board[x][y] = this;

            //Checks if this new move would result in a check for the king
            boardObj.check(color);

            //reverts swapped positions on the board if it would result in a check
            if(boardObj.getCheck()) {
                board[xPos][yPos] = this;
                board[x][y] = temp;
                boardObj.check(color);
                return false;
            }
            if(swap){
                xPos = x;
                yPos = y;
            }else{
                board[xPos][yPos] = this;
                board[x][y] = temp;
            }
            return true;
        }
        return false;
    }

    /**
     * Used in board's check and checkmate.  Determines if the piece can move to the new location
     * board's check - if the piece can reach the enemy king
     * board's checkmate - if the piece can make a move somewhere that would cause board to no longer be in check.
     *
     * @param boardObj board object to check for collisions with other pieces
     * @param x vertical position to move to
     * @param y horizontal position to move to
     * @return Whether to new move to x and y would be valid
     */
    public boolean check(Board boardObj, int x, int y) {
        Piece[][] board = boardObj.getBoard();
        if(Math.abs(xPos-x)==2 && Math.abs(yPos-y)==1 || Math.abs(xPos-x)==1 && Math.abs(yPos-y)==2)
            return (board[x][y]==null || !board[x][y].color==(color));
        return false;
    }

    /**
     * returns color associated with piece
     * @return true-white, false-black
     */
    public boolean getColor() {
        return color;
    }

    /**
     * @return print wN or bN based on color
     */
    public String toString() {
        return color?"wN":"bN";
    }
}
