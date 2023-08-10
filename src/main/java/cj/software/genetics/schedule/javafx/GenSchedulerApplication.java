package cj.software.genetics.schedule.javafx;

import javafx.application.Application;
import javafx.stage.Stage;

public class GenSchedulerApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Scheduling with a genetic algorithm");
        stage.setMaximized(true);
        stage.show();
    }
}
