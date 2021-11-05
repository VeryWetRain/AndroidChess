package com.example.androidchess91.pieces;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchess91.Chess;
import com.example.androidchess91.R;

/**
 * Pawn Piece
 * @author Bryan Law
 * @author Ryan Kim
 *
 */
public class Pawn extends Piece{
    /**
     * If pawn has made a move.
     */
    private boolean moved;

    /**
     * If the pawn has taken the double step from its initial position
     */
    private boolean double_stepped;

    /**
     * If the pawn is going to promote with its next move
     */
    private boolean promote;

    /**
     * Class constructor for the pawn piece
     *
     * @param x Vertical position on the board
     * @param y Horizontal position on the board
     * @param color True for white, false for black
     */
    public Pawn(int x, int y, boolean color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
        this.moved = false;
        this.double_stepped = false;
        this.promote = false;
    }

    /**
     * Checks if pawn moving to new position would be a valid move and doesn't cause a check for own king
     * Moves in only vertical line towards the enemy. Can take a double step from its initial position
     * Can take an enemy piece if its one diagonal away
     * Can promote to a different piece if it moves to the end of the board
     * Special move: en passant. A pawn can move diagonally behind another pawn if the opposing pawn's last move was a double step
     *
     * @param boardObj board object to check for collisions with other pieces
     * @param x vertical position to be moved to
     * @param y horizontal position to be moved to
     * @return Whether to new move to x and y would be valid
     */
    public boolean move(Board boardObj, int x, int y, boolean swap, AppCompatActivity context) {
        Piece[][] board = boardObj.getBoard();
        if(xPos==x && yPos==y) return false;

        boolean move = false;
        if(color) {
            // first move can move up 2
            if(!moved && xPos-x==2 && yPos-y==0) {
                for(int i=xPos-1; i>=x; i--) {
                    if(board[i][yPos]!=null) return false;
                }
                double_stepped = true;
                boardObj.double_stepped_white = true;
                move = true;
                // can move up 1 space if it is empty
            }else if(xPos-x==1 && yPos-y==0 && board[x][y]==null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_white = false;
                // can move up 1 left 1 if there is a piece to capture
            }else if(xPos-x==1 && yPos-y==1 && board[x][y]!=null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_white = false;
                // can move up 1 right 1 if there is a piece to capture
            }else if(xPos-x==1 && yPos-y==-1 && board[x][y]!=null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_white = false;
                // en passant: can move up 1 right 1 to capture a pawn that has double-stepped to the right of it
            } else if(xPos-x==1 && yPos-y==-1 && // up 1 right 1
                    board[x][y] == null && // empty space
                    xPos==3 && // fifth rank for white
                    board[xPos][yPos+1] instanceof Pawn && // capturing piece must be pawn
                    boardObj.double_stepped_black // immediate last move was double-step
            ) {
                // check if captured pawn's last move was a double step
                if(((Pawn) board[xPos][yPos+1]).double_stepped) {
                    move = true;
                    if(swap) board[xPos][yPos+1] = null;
                    double_stepped = false;
                    boardObj.double_stepped_white = false;
                }
                // en passant: can move up 1 left 1 to capture a pawn that has double-stepped to the right of it
            } else if(xPos-x==1 && yPos-y==1 && // up 1 left 1
                    board[x][y] == null && // empty space
                    xPos==3 && // fifth rank for white
                    board[xPos][yPos-1] instanceof Pawn && // capturing piece must be pawn
                    boardObj.double_stepped_black // immediate last move was double-step
            ) {
                // check if captured pawn's last move was a double step
                if (((Pawn) board[xPos][yPos - 1]).double_stepped) {
                    move = true;
                    if(swap) board[xPos][yPos - 1] = null;
                    double_stepped = false;
                    boardObj.double_stepped_white = false;
                }
            }

            // promote if move is valid and destination is on the 8th rank
            if(move && x==0) {promote = true;}

        }else if(!color) {
            // first move can move down 2
            if(!moved && xPos-x==-2 && yPos-y==0) {
                for(int i=xPos+1; i<=x; i++) {
                    if(board[i][yPos]!=null) return false;
                }
                double_stepped = true;
                boardObj.double_stepped_black = true;
                move = true;
                // can move down 1 space if it is empty
            }else if(xPos-x==-1 && yPos-y==0 && board[x][y]==null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_black = false;
                // can move down 1 left 1 if there is a piece to capture
            }else if(xPos-x==-1 && yPos-y==1 && board[x][y]!=null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_black = false;
                // can move down 1 right 1 if there is a piece to capture
            }else if(xPos-x==-1 && yPos-y==-1 && board[x][y]!=null) {
                move = true;
                double_stepped = false;
                boardObj.double_stepped_black = false;
                // en passant: can move down 1 left 1 to capture a pawn that has double-stepped to the right of it
            } else if(xPos-x==-1 && yPos-y==1 && // down 1 left 1
                    board[x][y] == null && // empty space
                    xPos==4 && // fifth rank for black
                    board[xPos][yPos-1] instanceof Pawn && // capturing piece must be pawn
                    boardObj.double_stepped_white // immediate last move was double-step
            ) {
                // check if captured pawn's last move was a double step
                if(((Pawn) board[xPos][yPos-1]).double_stepped) {
                    move = true;
                    if(swap) board[xPos][yPos-1] = null;
                    double_stepped = false;
                    boardObj.double_stepped_black = false;
                }
                // en passant: can move down 1 right 1 to capture a pawn that has double-stepped to the right of it
            } else if(xPos-x==-1 && yPos-y==-1 && // down 1 right 1
                    board[x][y] == null && // empty space
                    xPos==4 && // fifth rank for black
                    board[xPos][yPos+1] instanceof Pawn && // capturing piece must be pawn
                    boardObj.double_stepped_white // immediate last move was double-step
            ) {
                // check if captured pawn's last move was a double step
                if (((Pawn) board[xPos][yPos + 1]).double_stepped) {
                    move = true;
                    if(swap) board[xPos][yPos + 1] = null;
                    double_stepped = false;
                    boardObj.double_stepped_black = false;
                }
            }

            // promote if move is valid and destination is on the 8th rank
            if(move && x==7) {promote = true;}
        }
        if(move) {
            if(board[x][y]!=null && board[x][y].getColor() == this.color || xPos==x && yPos==y) return false;
            Piece temp = board[x][y];
            board[x][y] = this;
            board[xPos][yPos] = null;

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
                moved = true;
            }else{
                board[xPos][yPos] = this;
                board[x][y] = temp;
            }
            return true;
        }
        return false;
    }

    public void promote(Board boardObj, int x, int y, Piece p){
        Piece[][] board = boardObj.getBoard();

        board[xPos][yPos] = null;
        board[x][y] = p;
    }

    /**
     * Creates a new piece at x and y position for uses of promotion
     *
     * @param x Vertical position of the board
     * @param y Horizontal position of the board
     * @param type Piece type to be created
     * @param color True for white, false for black
     * @return The new piece that was created
     */

    private static Piece getPiece(int x, int y, String type, boolean color) {
        switch(type) {
            case "B":
                return new Bishop(x, y, color);
            case "R":
                return new Rook(x, y, color);
            case "N":
                return new Knight(x, y, color);
            default:
                return new Queen(x, y, color);
        }
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
        if(color) {
            // first move can move up 2
            if(!moved && xPos-x==2 && yPos-y==0) {
                for(int i=xPos-1; i>=x; i--) {
                    if(board[i][yPos]!=null) return false;
                }
                return true;
                // can move up 1 space if it is empty
            }else if(xPos-x==1 && yPos-y==0 && board[x][y]==null) {
                return true;
                // can move up 1 left 1 if there is a piece to capture
            }else if(xPos-x==1 && yPos-y==1 && board[x][y]!=null && !board[x][y].color==(color)) {
                return true;
                // can move up 1 right 1 if there is a piece to capture
            }else if(xPos-x==1 && yPos-y==-1 && board[x][y]!=null && !board[x][y].color==(color)) {
                return true;
            }
        }else {
            if(!moved && xPos-x==-2 && yPos-y==0) {
                for(int i=xPos+1; i<=x; i++) {
                    if(board[i][yPos]!=null) return false;
                }
                return true;
                // can move down 1 space if it is empty
            }else if(xPos-x==-1 && yPos-y==0 && board[x][y]==null) {
                return true;
                // can move down 1 left 1 if there is a piece to capture
            }else if(xPos-x==-1 && yPos-y==1 && board[x][y]!=null && !board[x][y].color==(color)) {
                return true;
                // can move down 1 right 1 if there is a piece to capture
            }else if(xPos-x==-1 && yPos-y==-1 && board[x][y]!=null && !board[x][y].color==(color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return prints wB for white and bB for black
     */
    public String toString() {
        return color?"wp":"bp";
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved(){
        return moved;
    }
}
