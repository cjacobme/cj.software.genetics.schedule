package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private RandomService randomService;

    public List<Task> createTasks(int startIndex, int durationInSeconds, int count) {
        List<Task> result = new ArrayList<>(count);
        int runningIndex = startIndex;
        for (int i = 0; i < count; i++) {
            int priority = randomService.nextRandom(3);
            Task task = Task.builder()
                    .withIdentifier(runningIndex)
                    .withDurationSeconds(durationInSeconds)
                    .withPriority(priority)
                    .build();
            result.add(task);
            runningIndex++;
        }
        return result;
    }
}
