package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Genetics {

    @Autowired
    private Converter converter;

    @Autowired
    private RandomService randomService;

    private final Logger logger = LogManager.getFormatterLogger();

    public Solution mate(Solution parent1, Solution parent2, int numWorkers, int numSlots) {
        Map<Task, Coordinate> converted1 = converter.toMapTaskCoordinate(parent1);
        Map<Task, Coordinate> converted2 = converter.toMapTaskCoordinate(parent2);
        List<Worker> workers = createWorkers(numWorkers, numSlots);
        List<Task> tasks = converter.toTaskList(parent1);
        int numTasks = tasks.size();
        int pos1 = randomService.nextRandom(numTasks);
        int pos2 = randomService.nextRandom(numTasks);
        int lower = Math.min(pos1, pos2);
        int upper = Math.max(pos1, pos2);
        dispatch(tasks, workers, converted1, lower, upper);
        dispatch(tasks, workers, converted2, upper, numTasks);
        dispatch(tasks, workers, converted2, 0, lower);
        Solution result = Solution.builder().withWorkers(workers).build();
        return result;
    }

    private void dispatch(List<Task> tasks, List<Worker> workers, Map<Task, Coordinate> converted, int lower, int upper) {
        for (int iTask = lower; iTask < upper; iTask++) {
            Task task = tasks.get(iTask);
            Coordinate coordinate = converted.get(task);
            int workerIndex = coordinate.getWorkerIndex();
            Worker worker = workers.get(workerIndex);
            int slotIndex = coordinate.getSlotIndex();
            Task existing = worker.getTaskAt(slotIndex);
            while (existing != null) {
                logger.info("Slot %d of Worker %d already occupied, try next one...",
                        slotIndex,
                        workerIndex);
                slotIndex++;
                existing = worker.getTaskAt(slotIndex);
            }
            workers.get(workerIndex).setTaskAt(slotIndex, task);
        }
    }

    private List<Worker> createWorkers(int numWorkers, int numSlots) {
        List<Worker> result = new ArrayList<>(numWorkers);
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            Worker worker = Worker.builder().withMaxNumTasks(numSlots).build();
            result.add(worker);
        }
        return result;
    }

    public void mutate(Solution solution) {
        List<Task> tasks = converter.toTaskList(solution);
        int size = tasks.size();
        int index0 = randomService.nextRandom(size);
        Task task0 = tasks.get(index0);
        int index1 = randomService.nextRandom(size);
        Task task1 = tasks.get(index1);
        Map<Task, Coordinate> converted = converter.toMapTaskCoordinate(solution);
        Coordinate coordinate0 = converted.get(task0);
        Coordinate coordinate1 = converted.get(task1);
        int workerindex0 = coordinate0.getWorkerIndex();
        int slotindex0 = coordinate0.getSlotIndex();
        int workerindex1 = coordinate1.getWorkerIndex();
        int slotindex1 = coordinate1.getSlotIndex();
        List<Worker> workers = solution.getWorkers();
        Worker worker0 = workers.get(workerindex0);
        worker0.deleteTaskAt(slotindex0);
        worker0.setTaskAt(slotindex0, task1);
        Worker worker1 = workers.get(workerindex1);
        worker1.deleteTaskAt(slotindex1);
        worker1.setTaskAt(slotindex1, task0);
    }
}
