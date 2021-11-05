package com.example.androidchess91;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.example.androidchess91.pieces.Bishop;
import com.example.androidchess91.pieces.Board;
import com.example.androidchess91.pieces.King;
import com.example.androidchess91.pieces.Knight;
import com.example.androidchess91.pieces.Pawn;
import com.example.androidchess91.pieces.Piece;
import com.example.androidchess91.pieces.Queen;
import com.example.androidchess91.pieces.Rook;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Pair;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Chess extends AppCompatActivity {

    private GridView chessBoard;
    private Board b;
    private Piece[] boardGrid;
    private PieceAdapter pieceadapter;
    private Coordinate currentClick = null;
    private boolean white;
    private ArrayList<Move> moveHistory;
    private ArrayList<GameRecord> recordGames;
    private Board previousBoard;

    private Object[] saveData;
    private TextView turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Toolbar toolbar = findViewById(R.id.toolbar);
        turn = findViewById(R.id.turn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b = new Board();
        boardGrid = new Piece[64];
        white = true;
        previousBoard = null;
        moveHistory = new ArrayList<Move>();

        loadSaveData();

        chessBoard = findViewById(R.id.chessBoard);
        pieceadapter = new PieceAdapter(this, R.layout.grid_list, Arrays.asList(boardGrid),b);
        updateGrid();

        chessBoard.setOnItemClickListener((p, V, pos, id) ->
                handleClick(pos));

    }

    private void loadSaveData() {
        //Read recorded games from storage
        FileInputStream fis = null;
        try {
            fis = openFileInput("data.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            saveData = (Object[]) is.readObject();
            if(saveData[0]!=null) white = (boolean) saveData[0];
            if(saveData[1]!=null) b = (Board) saveData[1];
            if(saveData[2]!=null) previousBoard = (Board) saveData[2];
            if(saveData[3]!=null) moveHistory = (ArrayList<Move>) saveData[3];
            recordGames = (ArrayList<GameRecord>) saveData[4];

            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            //Creating the data file if it doesn't exist
            try {
                FileOutputStream fos = openFileOutput("data.dat", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                recordGames = new ArrayList<GameRecord>();
                saveData = new Object[]{white, b, previousBoard, moveHistory, recordGames};

                os.writeObject(saveData);
                os.close();
                fos.close();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void handleClick(int pos) {
        int row = pos/8;
        int col = pos%8;
        System.out.println(row+","+col);

        Piece[][] board = b.getBoard();
        if(board[row][col] == null && currentClick==null) return;

        Coordinate click = new Coordinate(row,col);

        if(b.validMoves.contains(click)){
            Move m = new Move(new Coordinate(currentClick.getX(),currentClick.getY()),new Coordinate(row,col));
            if(board[currentClick.getX()][currentClick.getY()] instanceof Pawn && row==7
                    || board[currentClick.getX()][currentClick.getY()] instanceof Pawn && row==0){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Promote");
                LayoutInflater vi = LayoutInflater.from(this);
                View v = vi.inflate(R.layout.promote_dialog, null);
                builder.setView(v);

                Button promoteQueen = (Button) v.findViewById(R.id.promoteQueen);
                Button promoteRook = (Button) v.findViewById(R.id.promoteRook);
                Button promoteKnight = (Button) v.findViewById(R.id.promoteKnight);
                Button promoteBishop = (Button) v.findViewById(R.id.promoteBishop);

                final AlertDialog promote = builder.create();
                Pawn p = (Pawn) board[currentClick.getX()][currentClick.getY()];

                promoteQueen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Queen newPiece = new Queen(row,col,p.getColor());
                        Move promoteMove = new Move(new Coordinate(currentClick.getX(),currentClick.getY()),new Coordinate(row,col), new Queen(row,col,p.getColor()));
                        executeMove(promoteMove,newPiece);
                        updateGrid();
                        promote.dismiss();
                    }
                });
                promoteRook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Rook newPiece = new Rook(row,col,p.getColor());
                        Move promoteMove = new Move(new Coordinate(currentClick.getX(),currentClick.getY()),new Coordinate(row,col), new Rook(row,col,p.getColor()));
                        executeMove(promoteMove,newPiece);
                        updateGrid();
                        promote.dismiss();
                    }
                });
                promoteKnight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Knight newPiece = new Knight(row,col,p.getColor());
                        Move promoteMove = new Move(new Coordinate(currentClick.getX(),currentClick.getY()),new Coordinate(row,col), new Knight(row,col,p.getColor()));
                        executeMove(promoteMove,newPiece);
                        updateGrid();
                        promote.dismiss();
                    }
                });
                promoteBishop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bishop newPiece = new Bishop(row,col,p.getColor());
                        Move promoteMove = new Move(new Coordinate(currentClick.getX(),currentClick.getY()),new Coordinate(row,col), new Bishop(row,col,p.getColor()));
                        executeMove(promoteMove,newPiece);
                        updateGrid();
                        promote.dismiss();
                    }
                });
                promote.show();
            }else {
                executeMove(m,null);
            }
        } else if(currentClick!=null) {
            b.validMoves.clear();
            currentClick = null;
            updateGrid();
            return;
        }else if(board[row][col]!=null && board[row][col].getColor()==white){
            b.validMoves.clear();
            if(board[row][col] == null){
                currentClick= null;
                updateGrid();
                return;
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[row][col].move(b, i, j,false,this)) {
                        b.validMoves.add(new Coordinate(i, j));
                        currentClick = click;
                    }
                }
            }
        }
        updateGrid();
    }

    public void undo(View view){
        if(previousBoard == null){
            return;
        }
        moveHistory.remove(moveHistory.size()-1);
        b = previousBoard.deepClone();
        b.validMoves.clear();
        pieceadapter = new PieceAdapter(this, R.layout.grid_list, Arrays.asList(boardGrid),b);
        previousBoard = null;
        currentClick = null;
        changeTurn();
        updateGrid();
    }

    private void gameOver(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);
        builder.setMessage("Enter Title and hit OK to record this game");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString();
                try{
                    GameRecord finishedGame = new GameRecord(title,moveHistory);
                    recordGames.add(finishedGame);

                    FileOutputStream fos = openFileOutput("data.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    saveData = new Object[]{null,null,null,null, recordGames};
                    os.writeObject(saveData);
                    os.close();
                    fos.close();
                }catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    FileOutputStream fos = openFileOutput("data.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    saveData = new Object[]{null,null,null,null, recordGames};
                    os.writeObject(saveData);
                    os.close();
                    fos.close();
                }catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                dialog.cancel();
                finish();
            }
        });

        builder.show();
    }

    private void updateGrid(){
        for(int i=0; i<8;i++){
            for(int j=0; j<8; j++){
                boardGrid[i*8+j] = b.getBoard()[i][j];
            }
        }
        chessBoard.setAdapter(pieceadapter);
    }

    private void changeTurn(){
        if(white){
            turn.setText("Black's Turn");
            turn.setTextColor(Color.BLACK);
        } else{
            turn.setText("White's Turn");
            turn.setTextColor(Color.WHITE);
        }
        white = !white;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    FileOutputStream fos = openFileOutput("data.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    saveData = new Object[]{white, b, previousBoard, moveHistory, recordGames};

                    os.writeObject(saveData);
                    os.close();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();  // finish() here.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void draw(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Call Draw?");
        if(white)
            builder.setMessage("White is offering a draw. Accept?");
        else
            builder.setMessage("Black is offering a draw. Accept?");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameOver("Game Over. Draw");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void resign(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resign?");
        if(white)
            builder.setMessage("Confirm White Resign");
        else
            builder.setMessage("Confirm Black Resign");

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(white)
                    gameOver("Game Over. Black Wins by resignation");
                else
                    gameOver("Game Over. White Wins by resignation");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void randomMove(View view){
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        Piece[][] board = b.getBoard();
        b.validMoves.clear();
        ArrayList<Move> moves = new ArrayList<Move>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j]!=null && white==board[i][j].getColor())
                    pieces.add(board[i][j]);
            }
        }

        for(Piece p : pieces){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if(p.move(b,i,j,false,this))
                        moves.add(new Move(new Coordinate(p.getxPos(),p.getyPos()),new Coordinate(i,j)));
                }
            }
        }

        Move random = moves.get((int) (Math.random()*moves.size()));
        executeMove(random,null);
        updateGrid();
    }

    private void executeMove(Move m, Piece promote){
        int x1 = m.start.getX();
        int y1 = m.start.getY();
        int x2 = m.end.getX();
        int y2 = m.end.getY();
        Piece[][] board = b.getBoard();

        previousBoard = b.deepClone();
        if(promote == null){
            board[x1][y1].move(b,x2,y2,true,this);
        }else{
            Pawn pawn = (Pawn) board[x1][y1];
            pawn.promote(b,x2,y2, promote);
        }
        moveHistory.add(m);

        changeTurn();
        b.check(white);
        if(b.checkMate(b,white)){
            if(white)
                gameOver("Checkmate. Black Wins");
            else
                gameOver("Checkmate. White Wins");
        }

        currentClick=null;
        b.validMoves.clear();
    }

}