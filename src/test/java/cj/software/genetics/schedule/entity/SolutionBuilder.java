package cj.software.genetics.schedule.entity;

import java.util.ArrayList;
import java.util.List;

public class SolutionBuilder extends Solution.Builder {
    public static List<Task> createTasks() {
        List<Task> result = new ArrayList<>();
        for (int iTask = 0; iTask < 6; iTask++) {
            int prio = iTask % 3;
            Task task = Task.builder().withIdentifier(iTask).withDurationSeconds(iTask * 10).withPriority(prio).build();
            result.add(task);
        }
        return result;
    }

    public SolutionBuilder() {
        super(47, 11);
        this.withWorkerChains(new WorkerChainBuilder().build());
    }
}
