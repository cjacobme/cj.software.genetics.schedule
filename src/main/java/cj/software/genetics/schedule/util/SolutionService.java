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

    public Solution createInitialSoluation(int numWorkers, int numSlotsPerWorker, List<Task> tasks) {
        List<Worker> allWorkers = new ArrayList<>();
        Solution result = Solution.builder().build();
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            Worker worker = Worker.builder()
                    .withMaxNumTasks(numSlotsPerWorker)
                    .build();
            allWorkers.add(worker);
            result.addWorker(worker);
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
        return result;
    }
}
