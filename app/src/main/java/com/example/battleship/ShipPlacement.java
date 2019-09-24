package com.example.battleship;

public class ShipPlacement {
    GridPoint[] points;

    public ShipPlacement(GridPoint[] points){
        this.points = points;
    }

    public GridPoint[] getPoints() {
        return points;
    }
}
