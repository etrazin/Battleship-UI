package com.example.battleship.ServerClasses;


public class PlayResponse extends StatusResponse {

    private boolean hit;

    public PlayResponse(Game game, String user, String opponent, boolean hit) {
        super(game, user, opponent);
        this.hit = hit;
    }

    public boolean getHit() {
        return this.hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}