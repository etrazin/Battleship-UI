package com.example.battleship.ServerClasses;


public class ServerCell {

    private CellFill fillState;
    private CellHit hitState;

    public ServerCell() {
        this.fillState = CellFill.EMPTY;
        this.hitState = CellHit.UNTOUCHED;
    }

    public void fillCell() {
        this.fillState = CellFill.FULL;
    }

    public PlayResult hitCell() {
        if (this.fillState == CellFill.EMPTY) {
            this.hitState = CellHit.MISS;
            return PlayResult.MISS;
        } else if (this.hitState == CellHit.HIT) {
            return PlayResult.MISS; // Treat re-hitting a cell as a miss
        }

        this.hitState = CellHit.HIT;
        return PlayResult.HIT;
    }

    public CellFill getFill() {
        return this.fillState;
    }

    public CellHit getHit() {
        return this.hitState;
    }

    @Override
    public String toString() {
        String strToReturn = "";
        switch (this.hitState) {
            case HIT:
                strToReturn = "H";
                break;
            case MISS:
                strToReturn = "M";
                break;
            case UNTOUCHED:
                if (this.fillState == CellFill.EMPTY) {
                    strToReturn = "E";
                    break;
                }
                strToReturn = "F";
                break;
        }

        return strToReturn;
    }

    // Opponent sees all untouched cells as "empty"
    public String opponentView() {
        if (this.hitState == CellHit.UNTOUCHED) {
            return "E";
        }

        return this.toString();
    }



}