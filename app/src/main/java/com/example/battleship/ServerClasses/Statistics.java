package com.example.battleship.ServerClasses;

public class Statistics {

    private int hits, misses;
    public Statistics() {
        this.hits = 0;
        this.misses = 0;
    }

    public void incHits() {
        this.hits++;
    }

    public void incMisses() {
        this.misses++;
    }

    public int getHits() {
        return this.hits;
    }

    public int getMisses() {
        return this.misses;
    }

}