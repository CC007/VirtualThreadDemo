package com.github.cc007.aiassisted;

public record Grid(int width, int height, Cell[][] cells) {
    public static Grid of(int width, int height) {
        Cell[][] cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(false);
            }
        }
        return new Grid(width, height, cells);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(cells[i][j].isAlive() ? "O" : ".");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}