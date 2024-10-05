package com.WTV;

public class BatsmanStats {

	int totalRuns;
    int totalBalls;

    public BatsmanStats() {
        this.totalRuns = 0;
        this.totalBalls = 0;
    }

    public void addRuns(int runs) {
        this.totalRuns += runs;
    }

    public void addBall() {
        this.totalBalls += 1;
    }

    @Override
    public String toString() {
        return totalRuns + "," + totalBalls;
    }
}
