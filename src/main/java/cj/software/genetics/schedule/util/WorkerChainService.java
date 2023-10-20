package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.Priority;
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

    public int calcDuration(WorkerChain workerChain) {
        int result = 0;
        Worker[] workers = workerChain.getWorkers();
        for (Worker worker : workers) {
            int workerDuration = workerService.calcDuration(worker);
            result += workerDuration;
        }
        return result;
    }

    public WorkerChain createWorkerChain(SortedMap<Priority, List<Task>> priorities) {
        SortedMap<Integer, Worker> workers = new TreeMap<>();
        for (Map.Entry<Priority, List<Task>> priorityEntry : priorities.entrySet()) {
            Priority priority = priorityEntry.getKey();
            int priorityValue = priority.getValue();
            int numSlots = priority.getNumSlots();
            Worker worker = Worker.builder()
                    .withMaxNumTasks(numSlots)
                    .build();
            workers.put(priorityValue, worker);
        }
        WorkerChain result = WorkerChain.builder().withWorkers(workers).build();
        return result;
    }
}
