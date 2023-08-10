package cj.software.genetics.schedule;

import cj.software.genetics.schedule.javafx.GenSchedulerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

@SpringBootApplication
public class GeneticScheduleSpringBootApplication {
    public static void main(String[] args) {
        Application.launch(GenSchedulerApplication.class, args);
    }
}
