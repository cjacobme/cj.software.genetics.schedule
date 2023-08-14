package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    public List<Task> createTasks(int startIndex, int durationInSeconds, int count) {
        List<Task> result = new ArrayList<>(count);
        int runningIndex = startIndex;
        for (int i = 0; i < count; i++) {
            Task task = Task.builder().withIdentifier(runningIndex).withDurationSeconds(durationInSeconds).build();
            result.add(task);
            runningIndex++;
        }
        return result;
    }
}
