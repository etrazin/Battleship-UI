package com.example.battleship;

public class Cell {
    private Status status;

    public Cell( Status status) {
        this.status = status;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        VACANT, OCCUPIED, MISSED,INVALID,HIT
    }
}
