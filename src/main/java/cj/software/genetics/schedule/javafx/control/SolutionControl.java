package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
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

public class SolutionControl extends Pane {
    private static final int LINE_GAP = 3;
    private static final double WORKER_LABEL_WIDTH = 50.0;
    private static final int ROW_HEIGHT = 35;

    private final Canvas canvas = new Canvas();

    private final List<Node> myChildren = new ArrayList<>();

    private Solution solution;

    public SolutionControl() {
        getChildren().add(canvas);
        canvas.widthProperty().addListener(observable -> draw());
        canvas.heightProperty().addListener(observable -> draw());
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
        double posy = 0;
        int index = 0;
        ObservableList<Node> children = super.getChildren();
        List<Worker> workers = solution.getWorkers();
        for (Worker worker : workers) {
            String text = String.format("Worker %d", index);
            Label label = new Label(text);
            label.setLayoutX(10);
            label.setLayoutY(posy);
            label.setMaxWidth(WORKER_LABEL_WIDTH);
            label.setMinWidth(WORKER_LABEL_WIDTH);
            label.setPrefWidth(WORKER_LABEL_WIDTH);
            label.setMaxHeight(ROW_HEIGHT);
            label.setMinHeight(ROW_HEIGHT);
            label.setPrefHeight(ROW_HEIGHT);
            children.add(label);
            myChildren.add(label);
            drawWorker(worker, posy, children);
            posy += ROW_HEIGHT + LINE_GAP;
            index++;
        }
    }

    private void drawWorker(Worker worker, double posy, ObservableList<Node> children) {
        double posx = WORKER_LABEL_WIDTH + 10;
        List<Task> tasks = worker.getTasks();
        for (Task task : tasks) {
            int identifier = task.getIdentifier();
            int duration = task.getDurationSeconds();
            String text = String.format("#%d (%d)", identifier, duration);
            Button button = new Button(text);
            double width = duration * 15.0;
            button.setLayoutX(posx);
            button.setLayoutY(posy);
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
            children.add(button);
            myChildren.add(button);
            posx += width;
        }
    }
}
