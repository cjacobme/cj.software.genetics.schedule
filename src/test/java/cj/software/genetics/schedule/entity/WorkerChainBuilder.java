package cj.software.genetics.schedule.entity;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.PriorityBuilder;
import cj.software.genetics.schedule.entity.setup.Tasks;
import cj.software.genetics.schedule.entity.setup.TasksBuilder;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class WorkerChainBuilder extends WorkerChain.Builder {

    public static SortedMap<Priority, Worker> createDefaultPriorities() {
        SortedMap<Priority, Worker> result = new TreeMap<>();

        Priority prio0 = new PriorityBuilder().build();
        Worker worker0 = new WorkerBuilder().build();
        result.put(prio0, worker0);

        Worker worker1 = Worker.builder()
                .withMaxNumTasks(258)
                .build();
        Priority prio1 = Priority.builder()
                .withValue(1)
                .withNumSlots(258)
                .withForeground(Color.DARKGRAY)
                .withBackground(Color.LIGHTGRAY)
                .withTasks(List.of(
                        new TasksBuilder().build(),
                        Tasks.builder().withDurationSeconds(120).withNumberTasks(15).build()))
                .build();
        result.put(prio1, worker1);

        Worker worker2 = Worker.builder()
                .withMaxNumTasks(122)
                .build();
        Priority prio2 = Priority.builder()
                .withValue(2)
                .withNumSlots(350)
                .withForeground(Color.BLACK)
                .withBackground(Color.ORANGE)
                .build();
        result.put(prio2, worker2);

        return result;
    }

    public WorkerChainBuilder() {
        super.withWorkers(createDefaultPriorities());
    }
}
