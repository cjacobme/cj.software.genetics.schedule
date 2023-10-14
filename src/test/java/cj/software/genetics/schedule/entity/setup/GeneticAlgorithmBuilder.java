package cj.software.genetics.schedule.entity.setup;

import javafx.scene.paint.Color;

import java.util.List;

public class GeneticAlgorithmBuilder extends GeneticAlgorithm.Builder {
    public static Priority priority1() {
        Priority result = Priority.builder()
                .withValue(1)
                .withBackground(Color.YELLOW)
                .withForeground(Color.BLACK)
                .withTasks(List.of(
                        Tasks.builder().withNumberTasks(120).withDurationSeconds(30).build(),
                        Tasks.builder().withNumberTasks(25).withDurationSeconds(60).build()
                ))
                .build();
        return result;
    }

    public static Priority priority2() {
        Priority result = Priority.builder()
                .withValue(2)
                .withBackground(Color.GREEN)
                .withForeground(Color.YELLOW)
                .withTasks(List.of(
                        Tasks.builder().withNumberTasks(50).withDurationSeconds(15).build(),
                        Tasks.builder().withNumberTasks(75).withDurationSeconds(6).build(),
                        Tasks.builder().withNumberTasks(5).withDurationSeconds(120).build()
                ))
                .build();
        return result;
    }


    public GeneticAlgorithmBuilder() {
        super
                .withSolutionSetup(new SolutionSetupBuilder().build())
                .withPriorities(List.of(
                        new PriorityBuilder().build(),
                        priority1(),
                        priority2()
                ));
    }
}
