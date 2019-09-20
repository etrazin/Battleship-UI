package com.example.battleship;

public class UserStats {
    private int wins;
    private int losses;
    private double accuracy;

    public UserStats(int wins, int losses, double accuracy){
        this.wins = wins;
        this.losses = losses;
        this.accuracy = accuracy;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
