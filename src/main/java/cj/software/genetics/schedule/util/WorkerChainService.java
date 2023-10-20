package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WorkerChainService {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private RandomService randomService;

    private final Logger logger = LogManager.getFormatterLogger();

    public int calcDuration(WorkerChain workerChain) {
        int result = 0;
        SortedMap<Priority, Worker> workers = workerChain.getWorkers();
        for (Worker worker : workers.values()) {
            int workerDuration = workerService.calcDuration(worker);
            result += workerDuration;
        }
        return result;
    }

    public WorkerChain createWorkerChain(SortedMap<Priority, List<Task>> priorities) {
        SortedMap<Priority, Worker> workers = new TreeMap<>();
        for (Map.Entry<Priority, List<Task>> priorityEntry : priorities.entrySet()) {
            Priority priority = priorityEntry.getKey();
            int numSlots = priority.getNumSlots();
            Worker worker = Worker.builder()
                    .withMaxNumTasks(numSlots)
                    .build();
            workers.put(priority, worker);
            List<Task> tasksOfPriority = priorityEntry.getValue();
            for (Task task : tasksOfPriority) {
                int selectedSlot = randomService.nextRandom(numSlots);
                Task occupied = worker.getTaskAt(selectedSlot);
                while (occupied != null) {
                    logger.info("slot %d already occupied, try another one", selectedSlot);
                    selectedSlot = randomService.nextRandom(numSlots);
                    occupied = worker.getTaskAt(selectedSlot);
                }
                worker.setTaskAt(selectedSlot, task);
            }
        }
        WorkerChain result = WorkerChain.builder().withWorkers(workers).build();
        return result;
    }
}
