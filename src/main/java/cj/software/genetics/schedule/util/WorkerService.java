package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {
    @SuppressWarnings("squid:S3518")    // zero division is not possible
    public double calcDuration(Worker worker) {
        double result = 0.0;
        List<Task> tasks = worker.getTasks();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                int taskTime = task.getDurationSeconds();
                result += taskTime;
            }
        }
        return result;
    }
}
