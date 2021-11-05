package com.example.androidchess91;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.androidchess91.pieces.Bishop;
import com.example.androidchess91.pieces.King;
import com.example.androidchess91.pieces.Knight;
import com.example.androidchess91.pieces.Pawn;
import com.example.androidchess91.pieces.Piece;
import com.example.androidchess91.pieces.Queen;
import com.example.androidchess91.pieces.Rook;

import java.util.List;

public class PromoteAdapter extends ArrayAdapter<Piece> {

    private int resourceLayout;
    private Context context;

    public PromoteAdapter(Context context, int resource, List<Piece> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.context = context;
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


        return v;
    }
}
