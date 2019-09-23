package com.example.battleship.ServerClasses;

import com.example.battleship.GridPoint;

public class ShipPlacement implements Comparable<ShipPlacement> {

    GridPoint[] points;
    boolean[] hits;

    public void setPoints(GridPoint[] points) {
        this.points = points;
        this.hits = new boolean[points.length];
        for (int i = 0; i < points.length; i++) {
            this.hits[i] = false;
        }
    }

    public GridPoint[] getPoints() {
        return this.points;
    }

    public int getSize() {
        if (this.points == null) {
            return 0;
        }

        return this.points.length;
    }

    public boolean isValid() {
        if (this.points.length == 0) {
            return false;
        }

        boolean xChanged = false, yChanged = false;
        for (int i = 0; i < this.points.length - 1; i++) {
            if (this.points[i].getX() != this.points[i + 1].getX()) {
                xChanged = true;
            }

            if (this.points[i].getY() != this.points[i + 1].getY()) {
                yChanged = true;
            }

            if (xChanged && yChanged) {
                return false;
            }
        }

        return true;
    }

    // Return true if ship sunk
    public boolean hit(int row, int col) {
        for (int i = 0; i < this.points.length; i++) {
            if (this.points[i].getX() == row && this.points[i].getY() == col) {
                this.hits[i] = true;
                if (this.isSunk()) {
                    return true;
                }

                return false;
            }
        }
        return false;
    }

    // Check ship is fully hit
    public boolean isSunk() {
        for (int i = 0; i < this.hits.length; i++) {
            if (!this.hits[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(ShipPlacement sp) {
        return this.getSize() - sp.getSize();
    }

}
