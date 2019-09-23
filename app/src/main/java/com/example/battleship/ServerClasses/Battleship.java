package com.example.battleship.ServerClasses;

public class Battleship {

    private int length;
    private BattleshipState state;
    private ShipPlacement placement;

    public Battleship(int length) {
        this.length = length;
        this.state = BattleshipState.INIT;
    }

    public void setPlacement(ShipPlacement placement) {
        if (this.state == BattleshipState.INIT) {
            this.placement = placement;
            this.state = BattleshipState.FLOATING;
        }
    }

    public BattleshipExport export() {
        return new BattleshipExport(this);
    }

    public int getLength() {
        return this.length;
    }

    public BattleshipState getState() {
        return this.state;
    }

    public boolean hitShip(int row, int col) {
        boolean isSunk = this.placement.hit(row, col);
        if (isSunk) {
            this.state = BattleshipState.SUNK;
        }

        return isSunk;
    }

}