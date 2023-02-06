package com.github.cc007.aiassisted;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        Grid grid = Grid.of(width, height);
        Cell[][] cells = grid.cells();
        List<Thread> cellThreads = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = i;
                int y = j;
                Cell cell = cells[i][j];
                Thread cellThread = Thread.ofVirtual().start(() -> {
                    while (true) {
                        cell.updateState(cells, x, y, width, height);
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                });
                cellThreads.add(cellThread);
                // ...
            }
        }
        // Wait for all cell threads to finish
        for (Thread cellThread : cellThreads) {
            try {
                cellThread.join();
            } catch (InterruptedException e) {
                // ...
            }
        }
    }
}