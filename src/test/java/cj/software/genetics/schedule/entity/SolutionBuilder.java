package cj.software.genetics.schedule.entity;

import java.util.ArrayList;
import java.util.List;

public class SolutionBuilder extends Solution.Builder {

    public static List<Task> createTasks() {
        List<Task> result = new ArrayList<>();
        for (int iTask = 0; iTask < 6; iTask++) {
            Task task = Task.builder().withIdentifier(iTask).withDurationSeconds(iTask * 10).build();
            result.add(task);
        }
        return result;
    }

    private static List<Worker> createWorkers() {
        List<Worker> result = new ArrayList<>();
        List<Task> tasks = createTasks();
        for (int iWorker = 0; iWorker < 2; iWorker++) {
            Worker worker = Worker.builder().withMaxNumTasks(6).build();
            result.add(worker);
        }
        Worker worker0 = result.get(0);
        Worker worker1 = result.get(1);
        worker0.setTaskAt(0, tasks.get(0));
        worker0.setTaskAt(1, tasks.get(1));
        worker1.setTaskAt(3, tasks.get(2));
        worker1.setTaskAt(0, tasks.get(4));
        worker0.setTaskAt(2, tasks.get(3));
        worker0.setTaskAt(3, tasks.get(5));
        return result;
    }

    public SolutionBuilder() {
        super.withWorkers(createWorkers());
    }
}
