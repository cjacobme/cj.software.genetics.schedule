package cj.software.genetics.schedule.entity;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.PriorityBuilder;

import java.util.ArrayList;
import java.util.List;

public class SolutionBuilder extends Solution.Builder {
    public static List<Task> createTasks() {
        List<Task> result = new ArrayList<>();
        for (int iTask = 0; iTask < 6; iTask++) {
            int prio = iTask % 3;
            Priority priority = new PriorityBuilder().withValue(prio).build();
            Task task = Task.builder().withIdentifier(iTask).withDurationSeconds(iTask * 10).withPriority(priority).build();
            result.add(task);
        }
        return result;
    }

    public SolutionBuilder() {
        super(47, 11);
        WorkerChain workerChain = new WorkerChainBuilder().build();
        List<Task> tasks = createTasks();
        for (Task task : tasks) {
            workerChain.setTaskAt(task.getIdentifier(), task);
        }
        this.withWorkerChains(workerChain);
    }
}
