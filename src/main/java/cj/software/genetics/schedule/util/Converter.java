package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.springframework.stereotype.Service;

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
}
