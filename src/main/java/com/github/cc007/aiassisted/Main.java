package com.github.cc007.aiassisted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        int interval = 1000;
        int width = 10;
        int height = 10;
        Random random = new Random();
        Grid grid = Grid.of(width, height);
        Cell[][] cells = grid.cells();
        List<Thread> cellThreads = new ArrayList<>();

        GridPane root = new GridPane();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = i;
                int y = j;
                Cell cell = cells[i][j];
                Rectangle rectangle = new Rectangle(30, 30, Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                root.add(rectangle, j, i);
                Thread cellThread = Thread.ofVirtual().start(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(random.nextLong(interval));
                    } catch (InterruptedException e) {
                        return;
                    }
                    while (true) {
                        cell.updateState(cells, x, y, width, height);
                        if (cell.isAlive()) {
                            rectangle.setFill(Color.BLACK);
                        } else {
                            rectangle.setFill(Color.WHITE);
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(interval / 2 + random.nextLong(interval));
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                });
                cellThreads.add(cellThread);
            }
        }

        primaryStage.setScene(new Scene(root, width * 30, height * 30));
        primaryStage.show();
    }
}