package com.example.battleship.ServerClasses;

public class BattleshipExport {

    private int length;
    private BattleshipState state;

    public BattleshipExport(Battleship battleship) {
        this.length = battleship.getLength();
        this.state = battleship.getState();
    }

    public int getLength() {
        return this.length;
    }

    public BattleshipState getState() {
        return this.state;
    }



}
