package com.example.androidchess91.pieces;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchess91.Chess;

/**
 * King Piece
 * @author Bryan Law
 * @author Ryan Kim
 *
 */
public class King extends Piece{
    /**
     * Boolean to keep track if the king moved for castling
     */
    private boolean moved;

    /**
     * Class constructor fields inherited from Piece
     * @param x Vertical position on the board
     * @param y Horizontal position on the board
     * @param color true for white, false for black
     */
    public King(int x, int y, boolean color) {
        this.xPos = x;
        this.yPos = y;
        this.color = color;
        moved = false;
    }

    /**
     * Determines whether specified position is a valid move for the king, includes castling
     * King can not move within one space of enemy king
     * Castling must not have king be in check in between and both king and rook must have not moved
     * Regular move must be one space in any direction
     *
     * @param boardObj Current state of board to check for collisions
     * @param x Vertical position on board
     * @param y Horizontal position on board
     * @return If the movement to new position is valid or not
     */
    public boolean move(Board boardObj, int x, int y, boolean swap, AppCompatActivity context) {
        Piece[][] board = boardObj.getBoard();
        if(board[x][y]!=null && board[x][y].getColor() == this.color || xPos==x && yPos==y) return false;

        //check if king's new spot would be adjacent to the opponent king
        if(x+1<8 && y+1<8 && board[x+1][y+1]!=this && board[x+1][y+1] instanceof King //bottom right
                || x+1<8 &&board[x+1][y]!=this && board[x+1][y] instanceof King //bottom
                || x+1<8 && y-1>-1 && board[x+1][y-1]!=this && board[x+1][y-1] instanceof King //bottom left
                || y-1>-1 && board[x][y-1]!=this && board[x][y-1] instanceof King //left
                || x-1>-1 && y-1>-1 && board[x-1][y-1]!=this && board[x-1][y-1] instanceof King //top left
                || x-1>-1 && board[x-1][y]!=this && board[x-1][y] instanceof King //top
                || x-1>-1 && y+1<8 && board[x-1][y+1]!=this && board[x-1][y+1] instanceof King //top right
                || y+1<8 && board[x][y+1]!=this && board[x][y+1] instanceof King) { //right
            return false;
        }

        //castling
        if(!boardObj.getCheck() && !moved && color && x==7 && y==6
                ||!moved && color && x==7 && y==2
                ||!moved && !color && x==0 && y==6
                ||!moved && !color && x==0 && y==2) {
            if(y==6) {
                if (!rCastle(boardObj, x, y, swap)) return false;
            }
            else if(y==2) {
                if (!lCastle(boardObj, x, y, swap)) return false;
            }

            if(swap){
                boardObj.setKing(color, x, y);
                board[xPos][yPos] = null;
                xPos = x;
                yPos = y;
                moved = true;
            }
            return true;
        }

        //if movement is not the same square and only one space around then valid standard move
        if(Math.abs(xPos-x)<=1 && Math.abs(yPos-y)<=1) {
            Piece temp = board[x][y];
            board[xPos][yPos] = null;
            board[x][y] = this;
            boardObj.setKing(color, x, y);

            //Checks if this new move would result in a check for the king
            boardObj.check(color);

            //reverts swapped positions on the board if it would result in a check
            if(boardObj.getCheck()) {
                board[xPos][yPos] = this;
                board[x][y] = temp;
                boardObj.setKing(color, xPos, yPos);
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
                boardObj.setKing(color, xPos, yPos);
            }
            return true;
        }
        return false;
    }

    /**
     * Determines whether a castle in file 3 would cause collisions or a check
     *
     * @param boardObj State of the board to check for collisions
     * @param x Vertical position of castle move
     * @param y Horizontal position of the castle move
     * @return Whether the castle move is valid for file 3
     */
    public boolean lCastle(Board boardObj, int x, int y, boolean swap) {
        Piece[][] board = boardObj.getBoard();
        if(board[x][0]!=null && board[x][0] instanceof Rook) {
            if(!((Rook) board[x][0]).getMoved()){
                for(int i=yPos-1; i>=2;i--) {
                    if(board[xPos][i]!=null) return false;
                    board[xPos][i+1]=null;
                    board[xPos][i] = this;
                    board[xPos][yPos] = null;
                    boardObj.setKing(color, xPos, i);
                    boardObj.check(color);
                    if(boardObj.getCheck()) {
                        boardObj.setKing(color, xPos, yPos);
                        board[xPos][i] = null;
                        board[xPos][yPos] = this;
                        return false;
                    }
                }
                if(swap){
                    board[x][2] = new King(x,2,color);
                    board[x][3] = new Rook(x,3,color);
                    ((Rook) board[x][3]).setMoved(true);
                    board[x][0] = null;
                }else{
                    board[xPos][yPos] = this;
                    boardObj.setKing(color, xPos, yPos);
                    board[xPos][2] = null;
                    board[xPos][yPos] = this;
                }
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * Determines whether a castle in file 7 would cause collisions or a check
     *
     * @param boardObj State of the board to check for collisions
     * @param x Vertical position of castle move
     * @param y Horizontal position of the castle move
     * @return Whether the castle move is valid for file 7
     */
    public boolean rCastle(Board boardObj, int x, int y, boolean swap) {
        Piece[][] board = boardObj.getBoard();
        if(board[x][7]!=null && board[x][7] instanceof Rook) {
            if(!((Rook) board[x][7]).getMoved()){
                for(int i=yPos+1; i<=6;i++) {
                    if(board[xPos][i]!=null) return false;
                    board[xPos][i-1]=null;
                    board[xPos][i] = this;
                    board[xPos][yPos] = null;
                    boardObj.setKing(color, xPos, i);
                    boardObj.check(color);
                    if(boardObj.getCheck()) {
                        boardObj.setKing(color, xPos, yPos);
                        board[xPos][i] = null;
                        board[xPos][yPos] = this;
                        return false;
                    }
                }
                if(swap){
                    board[x][6] = new King(x,6,color);
                    board[x][5] = new Rook(x,5,color);
                    ((Rook) board[x][5]).setMoved(true);
                    board[x][7] = null;
                }else{
                    board[xPos][yPos] = this;
                    boardObj.setKing(color, xPos, yPos);
                    board[xPos][6] = null;
                    board[xPos][yPos] = this;
                }
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * Checks if the king can move to a new location to avoid checkmate
     *
     * @param boardObj State of the board to check if new space is empty
     * @param x Vertical position on the board
     * @param y Horizontal position on the board
     * @return Valid move to specified x and y position
     */
    public boolean check(Board boardObj, int x, int y) {
        Piece[][] board = boardObj.getBoard();
        if(Math.abs(xPos-x)<=1 && Math.abs(yPos-y)<=1) {
            if(board[x][y]==null || !board[x][y].color==color)
                return true;
        }
        return false;
    }

    /**
     * @return prints wK for white, bK for black
     */
    public String toString() {
        return color?"wK":"bK";
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved(){
        return moved;
    }
}
