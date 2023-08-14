package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolutionService {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private RandomService randomService;

    @Autowired
    private WorkerService workerService;

    public Solution createInitialSoluation(int indexInCycle, int numWorkers, int numSlotsPerWorker, List<Task> tasks) {
        List<Worker> allWorkers = new ArrayList<>();
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            Worker worker = Worker.builder()
                    .withMaxNumTasks(numSlotsPerWorker)
                    .build();
            allWorkers.add(worker);
        }
        for (Task task : tasks) {
            int selectedWorker = randomService.nextRandom(numWorkers);
            Worker worker = allWorkers.get(selectedWorker);
            int selectedSlot = randomService.nextRandom(numSlotsPerWorker);
            Task occupied = worker.getTaskAt(selectedSlot);
            while (occupied != null) {
                logger.info("slot %d already occupied, try another one", selectedSlot);
                selectedSlot = randomService.nextRandom(numSlotsPerWorker);
                occupied = worker.getTaskAt(selectedSlot);
            }
            worker.setTaskAt(selectedSlot, task);
        }
        Solution result = Solution.builder(0, indexInCycle).withWorkers(allWorkers).build();
        return result;
    }

    public double calcDuration(Solution solution) {
        double result = 0.0;
        List<Worker> workers = solution.getWorkers();
        for (Worker worker : workers) {
            double workerDuration = workerService.calcDuration(worker);
            result = Math.max(result, workerDuration);
        }
        return result;
    }
}
