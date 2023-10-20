package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

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

    public SortedMap<Priority, List<Task>> createTasks(GeneticAlgorithm geneticAlgorithm) {
        int startIndex = 0;
        SortedMap<Priority, List<Task>> result = new TreeMap<>();
        SortedSet<Priority> priorities = geneticAlgorithm.getPriorities();
        for (Priority priority : priorities) {
            List<Task> ofPriority = createTasks(priority, startIndex);
            result.put(priority, ofPriority);
            int size = ofPriority.size();
            Task last = ofPriority.get(size - 1);
            startIndex = last.getIdentifier() + 1;
        }
        return result;
    }

    public List<Task> createTasks(Priority priority, int startIndex) {
        int priorityValue = priority.getValue();
        SortedSet<Tasks> tasksSet = priority.getTasks();
        int runningIndex = startIndex;
        List<Task> result = new ArrayList<>();
        for (Tasks tasks : tasksSet) {
            int numberTasks = tasks.getNumberTasks();
            int duration = tasks.getDurationSeconds();
            for (int iTask = 0; iTask < numberTasks; iTask++) {
                Task task = Task.builder()
                        .withIdentifier(runningIndex)
                        .withDurationSeconds(duration)
                        .withPriority(priorityValue)
                        .build();
                result.add(task);
                runningIndex++;
            }
        }
        return result;
    }
}
