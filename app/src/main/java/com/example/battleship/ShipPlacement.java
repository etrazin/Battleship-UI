package com.example.battleship;

public class ShipPlacement {
    GridPoint[] points;
    boolean[] hits;

    public ShipPlacement(GridPoint[] points){
        this.points = points;
        this.hits = new boolean[points.length];
        for (int i = 0; i < points.length; i++) {
            this.hits[i] = false;
        }
    }

    public GridPoint[] getPoints() {
        return points;
    }
}
