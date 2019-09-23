package com.example.battleship.ServerClasses;

import com.example.battleship.GridPoint;

import java.util.Arrays;

public class PlayerData {
    private static final int[] SHIPS = {2, 2, 2, 2, 2, 2, 2, 2};
    private Board board;
    private Battleship[] ships;
    private boolean isPlaced;
    Statistics stats;

    public PlayerData(){
        // Init board
        this.board = new Board();
        this.isPlaced = false;

        // Init statistics
        this.stats = new Statistics();

        // Init Battleship array
        this.ships = new Battleship[SHIPS.length];
        for (int i = 0; i < SHIPS.length; i++) {
            ships[i] = new Battleship(SHIPS[i]);
        }
    }

    public String[][] getBoard(boolean isOpponent) {
        return this.board.export(isOpponent);
    }

    public Statistics getStats() {
        return this.stats;
    }

    public boolean isPlaced() {
        return this.isPlaced;
    }

    public BattleshipExport[] getBattleship() {
        BattleshipExport[] shipsExport = new BattleshipExport[this.ships.length];
        for (int i = 0; i < this.ships.length; i++) {
            shipsExport[i] = new BattleshipExport(this.ships[i]);
        }

        return shipsExport;
    }

    public PlayResult play(int row, int col) {
        PlayResult hit = this.board.hitCell(row, col);

        // Update Battleship and Stats
        if (hit == PlayResult.HIT) {
            this.stats.incHits();

            for (int i = 0; i < this.ships.length; i++) {
                boolean isSunk = this.ships[i].hitShip(row, col);
                if (isSunk) {
                    return PlayResult.SINK;
                }
            }

            return PlayResult.HIT;
        }

        // In case of miss
        this.stats.incMisses();
        return PlayResult.MISS;
    }

    public String place(ShipPlacement[] placements) {
        // Check that each placement is valid by itself
        for (int i = 0; i < placements.length; i++) {
            if (!placements[i].isValid()) {
                return String.format("Ship number %d placement is invalid", i);
            }
        }

        // check that the number of the ships is valid
        if (placements.length != SHIPS.length) {
            return "Illegal ships amount";
        }

        Arrays.sort(placements);
        String err;

        for (int i = 0; i < SHIPS.length; i++) {
            // Check that the ship size is valid
            if (SHIPS[i] != placements[i].getSize()) {
                return "Ships size mismatch, sizes should be [2, 2, 2, 2, 2, 2, 2, 2]";
            }

            // Place ship and fill relevant cells
            this.ships[i].setPlacement(placements[i]);
            GridPoint[] points = placements[i].getPoints();

            // Check point placement validity
            for (GridPoint point : points) {
                if (!this.board.isValidPlacement(point.getX(), point.getY())) {
                    return String.format("Invalid placement or point [%d, %d]", point.getX(), point.getY());
                }
            }

            // Place points
            for (GridPoint point : points) {
                this.board.fillCell(point.getX(), point.getY());
            }
        }

        this.isPlaced = true;
        return null;
    }


    public boolean isAllSunk() {
        for (int i = 0; i < this.ships.length; i++) {
            if (this.ships[i].getState() != BattleshipState.SUNK) {
                return false;
            }
        }

        return true;
    }
}
