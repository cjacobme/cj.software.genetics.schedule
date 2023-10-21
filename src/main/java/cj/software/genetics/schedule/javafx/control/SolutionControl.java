package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.Priority;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.SortedMap;

public class SolutionControl extends Pane {
    private static final int LINE_GAP = 3;
    private static final double WORKER_LABEL_WIDTH = 50.0;
    private static final int ROW_HEIGHT = 35;

    private final Canvas canvas = new Canvas();

    private final ColorService colorService;

    private final List<Node> myChildren = new ArrayList<>();

    private Solution solution;

    private int scale = 15;

    private final StringProperty status = new SimpleStringProperty();

    public SolutionControl(ColorService colorService) {
        this.colorService = colorService;
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

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
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
            SortedMap<Priority, Worker> workers = workerChain.getWorkers();
            for (Map.Entry<Priority, Worker> entry : workers.entrySet()) {
                Priority priority = entry.getKey();
                Color foregroundColor = priority.getForeground();
                Color backgroundColor = priority.getBackground();
                String foreground = colorService.normalize(foregroundColor);
                String background = colorService.normalize(backgroundColor);
                Worker worker = entry.getValue();
                posX = drawWorker(worker, background, foreground, posX, posY, children);
            }
            posY += ROW_HEIGHT + LINE_GAP;
            index++;
        }
    }

    private double drawWorker(Worker worker, String background, String foreground, double posX, double posY, ObservableList<Node> children) {
        double result = posX;
        List<Task> tasks = worker.getTasks();
        for (Task task : tasks) {
            int identifier = task.getIdentifier();
            int duration = task.getDurationSeconds();
            String text = String.format("#%d (%d)", identifier, duration);
            Button button = new Button(text);
            double width = duration * (double) scale;
            String style = String.format("-fx-background-color:%s;-fx-text-fill:%s;", background, foreground);
            button.setLayoutX(result);
            button.setLayoutY(posY);
            button.setMinWidth(width);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
            button.setStyle(style);
            children.add(button);
            myChildren.add(button);
            button.setOnAction(event -> setStatus(task.toString()));
            result += width + 2.0;
        }
        return result;
    }
}
