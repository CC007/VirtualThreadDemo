package com.github.cc007.aiassisted;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int width = 10;
        int height = 10;
        Grid grid = new Grid(width, height);
        Cell[][] cells = grid.getCells();
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
            }
        }

        for (Thread cellThread : cellThreads) {
            cellThread.join();
        }
    }
}