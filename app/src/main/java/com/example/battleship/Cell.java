package com.example.battleship;

public class Cell {
    private int playerNum;
    private Status status;

    public Cell(int playerNum, Status status) {
        this.playerNum = playerNum;
        this.status = status;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        VACANT, OCCUPIED, HIT, MISSED
    }
}
