package com.example.androidchess91.pieces;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchess91.Chess;

/**
 * Rook Piece
 * @author Bryan Law
 * @author Ryan Kim
 *
 */
public class Rook extends Piece{
    /**
     * boolean to keep track if the rook moved for castling
     */
    private boolean moved;

    /**
     * Class constructor fields inherited from Piece
     * @param x Vertical position on the board
     * @param y Horizontal position on the board
     * @param color true for white, false for black
     */
    public Rook(int x, int y, boolean color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
        moved = false;
    }

    /**
     * Determines whether specified position is a valid move for the rook and doesn't cause a check for own king
     * Moves in any horizontal and vertical row and must not have a piece in between
     *
     * @param boardObj Current state of board to check for collisions
     * @param x Vertical position on board
     * @param y Horizontal position on board
     * @return If the movement to new position is valid or not
     */
    public boolean move(Board boardObj, int x, int y, boolean swap, AppCompatActivity context) {
        Piece[][] board = boardObj.getBoard();
        if(board[x][y]!=null && board[x][y].getColor() == this.color || xPos==x && yPos==y) return false;

        if(Math.abs(xPos-x)!=0 && Math.abs(yPos-y)==0 || Math.abs(yPos-y)!=0 && Math.abs(xPos-x)==0) {
            //Checking for collisions moving up and down
            if(Math.abs(xPos-x)!=0 && Math.abs(yPos-y)==0) {
                if(xPos-x<0) { //moving down
                    for(int i=xPos+1; i<x; i++) {
                        if(board[i][yPos]!=null) return false;
                    }
                }else if(xPos-x>0) { //moving up
                    for(int i=xPos-1; i>x; i--) {
                        if(board[i][yPos]!=null) return false;
                    }
                }
            }
            //Checking for collisions moving left and right
            else if(Math.abs(yPos-y)!=0 && Math.abs(xPos-x)==0) {
                if(yPos-y<0) {
                    for(int i=yPos+1; i<y; i++) { //moving right
                        if(board[xPos][i]!=null) return false;
                    }
                }else if(yPos-y>0) {
                    for(int i=yPos-1; i>y; i--) { //moving left
                        if(board[xPos][i]!=null) return false;
                    }
                }
            }
            //Swap positions on the board
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
                moved = true;
            }else{
                board[xPos][yPos] = this;
                board[x][y] = temp;
            }
            return true;
        }
        return false;
    }

    /**
     * Used in board's check and checkmate. Determines if the piece can move to the new location
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
        if(Math.abs(xPos-x)!=0 && Math.abs(yPos-y)==0 || Math.abs(yPos-y)!=0 && Math.abs(xPos-x)==0) {
            //Checking for collisions moving up and down
            if(Math.abs(xPos-x)!=0 && Math.abs(yPos-y)==0) {
                if(xPos-x<0) { //moving down
                    for(int i=xPos+1; i<x; i++) {
                        if(board[i][yPos]!=null) return false;
                    }
                }else if(xPos-x>0) { //moving up
                    for(int i=xPos-1; i>x; i--) {
                        if(board[i][yPos]!=null) return false;
                    }
                }
                if(board[x][y]==null || !board[x][y].color==color)
                    return true;
            }
            //Checking for collisions moving left and right
            else if(Math.abs(yPos-y)!=0 && Math.abs(xPos-x)==0) {
                if(yPos-y<0) {
                    for(int i=yPos+1; i<y; i++) { //moving right
                        if(board[xPos][i]!=null) return false;
                    }
                }else if(yPos-y>0) {
                    for(int i=yPos-1; i>y; i--) { //moving left
                        if(board[xPos][i]!=null) return false;
                    }
                }
                if(board[x][y]==null || !board[x][y].color==(color))
                    return true;
            }
        }
        return false;
    }

    /**
     * @return prints wR for white, bR for black
     */
    public String toString() {
        return color?"wR":"bR";
    }

    /**
     * @return If the rook has moved, for castling purposes
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * sets the moved variable
     * @param moved Whether the rook has moved
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved(){
        return moved;
    }
}