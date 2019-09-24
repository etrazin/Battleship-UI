package com.example.battleship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class AdapterBoard extends ArrayAdapter<Cell> {
    private LayoutInflater inflater;

    public AdapterBoard(Context context, List<Cell> objects) {
        super(context, -1, objects);
        inflater = LayoutInflater.from(context);
    }


    //  sets appearance color according to and cell.Status
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_cell, parent, false);
        Cell cell = getItem(position);

        Button button = (Button) view.findViewById(R.id.button_board_cell);

        switch (cell.getStatus())
        {
            case HIT:
            {
                button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorHit));
                break;
            }
            case MISSED:
            {
                button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMissed));
                break;
            }
            case VACANT:
            {
                button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorVacant));
                break;
            }
            case OCCUPIED:
            {
                button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOccupied));
                break;
            }
        }

        return view;
    }


        //populates board with vacant cells
        public void addCells(GridView gridView, int numCells) {
        gridView.setAdapter(this);
        for (int i = 0; i < numCells; i++)
            this.add(new Cell(Cell.Status.VACANT));
    }
}
