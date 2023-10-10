package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Converter {
    public Map<Task, Coordinate> toMapTaskCoordinate(Solution solution, int priority) {
        Map<Task, Coordinate> result = new HashMap<>();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        int numWorkerChains = workerChains.size();
        for (int iWorkerChain = 0; iWorkerChain < numWorkerChains; iWorkerChain++) {
            WorkerChain workerChain = workerChains.get(iWorkerChain);
            Worker worker = workerChain.getWorkerForPriority(priority);
            Map<Task, Integer> slots = worker.getTasksWithSlots();
            for (Map.Entry<Task, Integer> entry : slots.entrySet()) {
                Task task = entry.getKey();
                int slotIndex = entry.getValue();
                Coordinate coordinate = Coordinate.builder().withWorkerIndex(iWorkerChain).withSlotIndex(slotIndex).build();
                result.put(task, coordinate);
            }
        }
        return result;
    }

    public List<Task> toTaskList(Solution solution, int priority) {
        List<Task> result = new ArrayList<>();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        for (WorkerChain workerChain : workerChains) {
            Worker worker = workerChain.getWorkerForPriority(priority);
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
