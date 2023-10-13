package cj.software.genetics.schedule.entity.setup;

import javafx.scene.paint.Color;

import java.util.List;

public class PriorityBuilder extends Priority.Builder {
    public PriorityBuilder() {
        super
                .withValue(0)
                .withForeground(Color.BLACK)
                .withBackground(Color.RED)
                .withTasks(List.of(
                        new TasksBuilder().build(),
                        new TasksBuilder().withDurationSeconds(50).withNumberTasks(20).build()));
    }
}
