package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Converter {
    public Map<Task, Coordinate> toMapTaskCoordinate(Solution solution) {
        Map<Task, Coordinate> result = new HashMap<>();
        List<Worker> workers = solution.getWorkers();
        int numWorkers = workers.size();
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            Worker worker = workers.get(iWorker);
            Map<Task, Integer> slots = worker.getTasksWithSlots();
            for (Map.Entry<Task, Integer> entry : slots.entrySet()) {
                Task task = entry.getKey();
                int slotIndex = entry.getValue();
                Coordinate coordinate = Coordinate.builder().withWorkerIndex(iWorker).withSlotIndex(slotIndex).build();
                result.put(task, coordinate);
            }
        }
        return result;
    }

    public List<Task> toTaskList(Solution solution) {
        List<Task> result = new ArrayList<>();
        for (Worker worker : solution.getWorkers()) {
            List<Task> workerTasks = worker.getTasks();
            result.addAll(workerTasks);
        }
        result.sort((task1, task2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(task1.getIdentifier(), task2.getIdentifier());
            int result1 = builder.build();
            return result1;
        });
        return result;
    }
}
