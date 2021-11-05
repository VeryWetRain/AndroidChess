package com.example.androidchess91;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.androidchess91.pieces.Board;
import com.example.androidchess91.pieces.King;
import com.example.androidchess91.pieces.Pawn;
import com.example.androidchess91.pieces.Piece;
import com.example.androidchess91.pieces.Rook;

import java.util.ArrayList;
import java.util.Arrays;

public class replay extends AppCompatActivity {
    private Board b;
    private Piece[] boardGrid;
    private ArrayList<Move> moveHistory;
    private int index = 0;
    private boolean white;

    private PieceAdapter pieceadapter;
    private GridView replayBoard;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        button3 = findViewById(R.id.button3);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b = new Board();
        boardGrid = new Piece[64];
        loadMoves();

        replayBoard = findViewById(R.id.replayBoard);
        pieceadapter = new PieceAdapter(this, R.layout.grid_list, Arrays.asList(boardGrid),b);
        updateGrid();
    }

    public void playMove(View view){
        if(index>=moveHistory.size()){
            button3.setText("Game Over");
            return;
        }

        Move move = moveHistory.get(index++);
        Piece[][] board = b.getBoard();
        int x1 = move.start.getX();
        int y1 = move.start.getY();

        int x2 = move.end.getX();
        int y2 = move.end.getY();

        if(move.promote == null) {
            board[x1][y1].move(b, x2, y2, true, this);
        }else{
            ((Pawn) board[x1][y1]).promote(b,x2,y2,move.promote);
        }
        updateGrid();
    }

    private void loadMoves() {
        GameRecord game;
        Bundle bundle = getIntent().getExtras();
        game = (GameRecord) bundle.getSerializable("game");
        moveHistory = game.getMoveHistory();
    }

    private void updateGrid(){
        for(int i=0; i<8;i++){
            for(int j=0; j<8; j++){
                boardGrid[i*8+j] = b.getBoard()[i][j];
            }
        }
        replayBoard.setAdapter(pieceadapter);
    }
}