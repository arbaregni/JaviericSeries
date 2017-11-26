package com.company;

public class ProgressTracker {
    String message;
    int ticks, iterations;
    public ProgressTracker(int iterations, String loadingMessage) {
        ticks = 0;
        this.message = loadingMessage;
        this.iterations = iterations;
    }
    public void tick() {
        if (ticks < iterations) {
            int percent = (int) (100.0 * ticks / iterations);
            ticks++;
            System.out.print(message + percent + "%\r");
        } else {
            System.out.println(message + "complete.");
            ticks = 0;
        }

    }
}
