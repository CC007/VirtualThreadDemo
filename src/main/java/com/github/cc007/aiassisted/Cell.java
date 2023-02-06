package com.github.cc007.aiassisted;

public class Cell {
    private boolean alive;

    public Cell(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void updateState(Cell[][] grid, int x, int y, int width, int height) {
        int liveNeighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int row = x + i;
                int col = y + j;
                if (row >= 0 && row < height && col >= 0 && col < width && grid[row][col].isAlive()) {
                    liveNeighbors++;
                }
            }
        }
        if (alive) {
            if (liveNeighbors < 2 || liveNeighbors > 3) {
                setAlive(false);
            }
        } else {
            if (liveNeighbors == 3) {
                setAlive(true);
            }
        }
    }
}