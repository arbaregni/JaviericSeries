package com.company;

public class ProgressTracker {
    String message;
    int ticks, iterations;
    public ProgressTracker(int iterations, String message) {
        ticks = 0;
        this.message = message;
        this.iterations = iterations;
    }
    public void tick() {
        int percent = (int) (100.0 * ticks / iterations);
        ticks++;
        System.out.print(message + percent + "%\r");
    }
}
