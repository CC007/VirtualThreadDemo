package com.github.cc007.aiassisted;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        int rectangleSize = 30;
        int updateIntervalNs = 500;
        int renderIntervalMs = 100;
        int width = 50;
        int height = 50;
        Random random = new Random();
        Grid grid = Grid.of(width, height);
        Cell[][] cells = grid.cells();
        List<Thread> cellThreads = new ArrayList<>();

        GridPane gridPane = new GridPane();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = i;
                int y = j;
                Cell cell = cells[i][j];
                Rectangle rectangle = new Rectangle(rectangleSize, rectangleSize, Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeType(StrokeType.INSIDE);
                gridPane.add(rectangle, j, i);
                Thread cellThread = Thread.ofVirtual().start(() -> {
                    try {
                        TimeUnit.MICROSECONDS.sleep(random.nextLong(updateIntervalNs));
                    } catch (InterruptedException e) {
                        return;
                    }
                    while (true) {
                        cell.updateState(cells, x, y, width, height);
                        try {
                            TimeUnit.MICROSECONDS.sleep(updateIntervalNs + random.nextLong(updateIntervalNs));
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                });
                cellThreads.add(cellThread);
                Thread renderThread = Thread.ofVirtual().start(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(random.nextLong(renderIntervalMs));
                    } catch (InterruptedException e) {
                        return;
                    }
                    while (true) {
                        Platform.runLater(() -> {
                            if (cell.isAlive()) {
                                rectangle.setFill(Color.BLACK);
                            } else {
                                rectangle.setFill(Color.WHITE);
                            }
                        });
                        try {
                            TimeUnit.MILLISECONDS.sleep(renderIntervalMs + random.nextLong(renderIntervalMs));
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                });
                cellThreads.add(renderThread);
            }
        }
        final double sceneWidth = width * rectangleSize;
        final double sceneHeight = height * rectangleSize;
        final double aspectRatio = sceneWidth / sceneHeight;
        final Scene scene = new Scene(gridPane, sceneWidth, sceneHeight);
        
        Scale scale = new Scale();
        scale.xProperty().bind(gridPane.widthProperty().divide(sceneWidth));
        scale.yProperty().bind(gridPane.heightProperty().divide(sceneHeight));
        gridPane.getTransforms().add(scale);
        
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(aspectRatio));
        primaryStage.minHeightProperty().bind(scene.widthProperty().divide(aspectRatio));
        primaryStage.show();
    }
}