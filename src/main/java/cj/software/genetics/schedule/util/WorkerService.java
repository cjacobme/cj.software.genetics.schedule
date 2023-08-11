package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {
    @SuppressWarnings("squid:S3518")    // zero division is not possible
    public double calcFitnessValue(Worker worker) {
        double result;
        List<Task> tasks = worker.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            int totalTime = 0;
            for (Task task : tasks) {
                int taskTime = task.getDurationSeconds();
                totalTime += taskTime;
            }
            result = 1.0 / totalTime;
        } else {
            result = 0.0;
        }
        return result;
    }
}
