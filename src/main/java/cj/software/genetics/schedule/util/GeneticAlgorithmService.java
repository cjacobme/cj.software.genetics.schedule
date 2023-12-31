package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.SolutionSetup;
import cj.software.genetics.schedule.entity.setup.Tasks;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class GeneticAlgorithmService {
    @Validated
    public GeneticAlgorithm createDefault() {
        SolutionSetup solutionSetup = SolutionSetup.builder()
                .withNumSolutions(100)
                .withNumWorkers(5)
                .withElitismCount(4)
                .withTournamentSize(10)
                .build();
        Priority prio0 = Priority.builder()
                .withValue(0)
                .withBackground(Color.RED)
                .withForeground(Color.BLACK)
                .withNumSlots(500)
                .withTasks(List.of(
                        Tasks.builder().withNumberTasks(17).withDurationSeconds(10).build(),
                        Tasks.builder().withNumberTasks(13).withDurationSeconds(20).build(),
                        Tasks.builder().withNumberTasks(10).withDurationSeconds(50).build()
                )).build();
        Priority prio1 = Priority.builder()
                .withValue(1)
                .withNumSlots(100)
                .withBackground(Color.YELLOW)
                .withForeground(Color.BLACK)
                .withTasks(List.of(
                        Tasks.builder().withNumberTasks(10).withDurationSeconds(10).build(),
                        Tasks.builder().withNumberTasks(20).withDurationSeconds(30).build()
                )).build();
        Priority prio2 = Priority.builder()
                .withValue(2)
                .withNumSlots(150)
                .withBackground(Color.GREEN)
                .withForeground(Color.YELLOW)
                .withTasks(List.of(
                        Tasks.builder().withNumberTasks(10).withDurationSeconds(15).build()
                )).build();
        GeneticAlgorithm result = GeneticAlgorithm.builder()
                .withSolutionSetup(solutionSetup)
                .withPriorities(List.of(prio0, prio1, prio2))
                .build();
        return result;
    }
}
