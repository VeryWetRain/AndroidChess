package com.example.androidchess91;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.androidchess91.pieces.*;

import java.util.List;

public class PieceAdapter extends ArrayAdapter<Piece> {

    private int resourceLayout;
    private Context context;
    private Board b;

    public PieceAdapter(Context context, int resource, List<Piece> items, Board b) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
        this.b = b;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }

        Piece p = getItem(position);

        int row = position/8;
        int col = position%8;
        ImageView background = (ImageView) v.findViewById(R.id.background);
        TextView piece = (TextView) v.findViewById(R.id.piece);
        FrameLayout square = (FrameLayout) v.findViewById(R.id.square);

        Coordinate pair = new Coordinate(row,col);
        if(b.validMoves.contains(pair)){
            background.setImageResource(R.drawable.movable);
        }
        else if(row%2==0 && col%2==0 || row%2==1 && col%2==1) {
            background.setImageResource(R.drawable.white_square);
        }
        else {
            background.setImageResource(R.drawable.black_square);
        }

        if(p!= null && p.getColor()) {
            if(p instanceof Pawn)
                piece.setText("p");
            else if(p instanceof Rook)
                piece.setText("r");
            else if(p instanceof Knight)
                piece.setText("n");
            else if(p instanceof Bishop)
                piece.setText("b");
            else if(p instanceof Queen)
                piece.setText("q");
            else if(p instanceof King)
                piece.setText("k");
        }else if(p!=null){
            if(p instanceof Pawn)
                piece.setText("o");
            else if(p instanceof Rook)
                piece.setText("t");
            else if(p instanceof Knight)
                piece.setText("m");
            else if(p instanceof Bishop)
                piece.setText("v");
            else if(p instanceof Queen)
                piece.setText("w");
            else if(p instanceof King)
                piece.setText("l");
        }
        piece.setTypeface(ResourcesCompat.getFont(context,R.font.chessfont));
        return v;
    }

}

