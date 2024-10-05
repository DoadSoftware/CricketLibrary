package com.WTV;

public class BowlerStats {

	int totalWickets;
    int totalBallsBowled;
    int totalRunsConceded;

    public BowlerStats() {
        this.totalWickets = 0;
        this.totalBallsBowled = 0;
        this.totalRunsConceded = 0;
    }

    public void addBall() {
        this.totalBallsBowled += 1;
    }

    public void addRunsConceded(int runs) {
        this.totalRunsConceded += runs;
    }

    public void addWicket() {
        this.totalWickets += 1;
    }

    public double getOvers() {
        return totalBallsBowled / 6 + (totalBallsBowled % 6) / 10.0;
    }

    @Override
    public String toString() {
        return totalWickets + "," + getOvers() + "," + totalRunsConceded;
    }
}
