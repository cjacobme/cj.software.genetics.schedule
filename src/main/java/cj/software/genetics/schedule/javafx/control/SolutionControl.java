package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolutionControl extends Pane {
    private static final int LINE_GAP = 3;
    private static final double WORKER_LABEL_WIDTH = 50.0;
    private static final int ROW_HEIGHT = 35;
    private static final Map<Integer, String> BACKGROUND_COLORS = Map.of(
            0, "#ff0000;",  // red
            1, "#ffff00",   // yellow
            2, "#008000"    // green
    );
    private static final Map<Integer, String> FOREGROUND_COLORS = Map.of(
            0, "#000000",   // black
            1, "#000000",   // black
            2, "#ffff00"    // yelllow
    );

    private final Canvas canvas = new Canvas();

    private final List<Node> myChildren = new ArrayList<>();

    private Solution solution;

    private int scale = 15;

    public SolutionControl() {
        getChildren().add(canvas);
        canvas.widthProperty().addListener(observable -> draw());
        canvas.heightProperty().addListener(observable -> draw());
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double paneWidth = canvas.getWidth();
        double paneHeight = canvas.getHeight();
        gc.clearRect(0, 0, paneWidth, paneHeight);
        Paint oldFill = gc.getFill();
        gc.setFill(Color.LIGHTGRAY);
        try {
            gc.fillRect(0, 0, paneWidth, paneHeight);
            if (solution != null) {
                drawSolution();
            }
        } finally {
            gc.setFill(oldFill);
        }
    }

    public void setSolution(Solution solution) {
        deleteOldChildren();
        this.solution = solution;
        draw();
    }

    private void deleteOldChildren() {
        ObservableList<Node> children = super.getChildren();
        for (Node child : myChildren) {
            children.remove(child);
        }
    }

    private void drawSolution() {
        double posY = 0;
        int index = 0;
        ObservableList<Node> children = super.getChildren();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        for (WorkerChain workerChain : workerChains) {
            String text = String.format("Worker %d", index);
            Label label = new Label(text);
            label.setLayoutX(10);
            label.setLayoutY(posY);
            label.setMaxWidth(WORKER_LABEL_WIDTH);
            label.setMinWidth(WORKER_LABEL_WIDTH);
            label.setPrefWidth(WORKER_LABEL_WIDTH);
            label.setMaxHeight(ROW_HEIGHT);
            label.setMinHeight(ROW_HEIGHT);
            label.setPrefHeight(ROW_HEIGHT);
            children.add(label);
            myChildren.add(label);
            double posX = WORKER_LABEL_WIDTH + 10;
            for (int iPrio = 0; iPrio < 3; iPrio++) {
                Worker worker = workerChain.getWorkerForPriority(iPrio);
                posX = drawWorker(worker, posX, posY, children);
            }
            posY += ROW_HEIGHT + LINE_GAP;
            index++;
        }
    }

    private double drawWorker(Worker worker, double posX, double posY, ObservableList<Node> children) {
        double result = posX;
        List<Task> tasks = worker.getTasks();
        for (Task task : tasks) {
            int identifier = task.getIdentifier();
            int duration = task.getDurationSeconds();
            String text = String.format("#%d (%d)", identifier, duration);
            Button button = new Button(text);
            double width = duration * (double) scale;
            int priority = task.getPriority();
            String background = BACKGROUND_COLORS.get(priority);
            String foreground = FOREGROUND_COLORS.get(priority);
            String style = String.format("-fx-background-color:%s;-fx-text-fill:%s;", background, foreground);
            button.setLayoutX(result);
            button.setLayoutY(posY);
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
            button.setStyle(style);
            children.add(button);
            myChildren.add(button);
            result += width;
        }
        return result;
    }
}
