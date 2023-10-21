package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.Priority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class Genetics {

    @Autowired
    private Converter converter;

    @Autowired
    private RandomService randomService;

    @Autowired
    private SolutionService solutionService;

    private final Logger logger = LogManager.getFormatterLogger();

    public Solution mate(int cycleCounter, int indexInCycle, Solution parent1, Solution parent2) {
        Set<Priority> priorities = determinePriorities(parent1);
        int numWorkerChains = determineNumWorkerChains(parent1);
        Map<Priority, List<Worker>> prioMap = new HashMap<>();
        for (Priority priority : priorities) {
            Map<Task, Coordinate> converted1 = converter.toMapTaskCoordinate(parent1, priority);
            Map<Task, Coordinate> converted2 = converter.toMapTaskCoordinate(parent2, priority);
            int numSlots = priority.getNumSlots();
            List<Worker> workers = createWorkers(numWorkerChains, numSlots);
            List<Task> tasks = converter.toTaskList(parent1, priority);
            int numTasks = tasks.size();
            int pos1 = randomService.nextRandom(numTasks);
            int pos2 = randomService.nextRandom(numTasks);
            int lower = Math.min(pos1, pos2);
            int upper = Math.max(pos1, pos2);
            dispatch(tasks, workers, converted1, lower, upper);
            dispatch(tasks, workers, converted2, upper, numTasks);
            dispatch(tasks, workers, converted2, 0, lower);
            prioMap.put(priority, workers);
        }

        WorkerChain[] workerChains = new WorkerChain[numWorkerChains];
        for (int iWorkerChain = 0; iWorkerChain < numWorkerChains; iWorkerChain++) {
            SortedMap<Priority, Worker> workers = toSortedMap(prioMap, iWorkerChain);
            workerChains[iWorkerChain] = WorkerChain.builder()
                    .withWorkers(workers)
                    .build();
        }

        Solution result = Solution.builder(cycleCounter, indexInCycle)
                .withWorkerChains(workerChains)
                .build();
        int duration = solutionService.calcDuration(result);
        result.setDurationInSeconds(duration);
        return result;
    }

    private SortedMap<Priority, Worker> toSortedMap(Map<Priority, List<Worker>> prioMap, int index) {
        SortedMap<Priority, Worker> result = new TreeMap<>();
        Set<Priority> keys = prioMap.keySet();
        for (Priority priority : keys) {
            List<Worker> workersOfPriority = prioMap.get(priority);
            Worker worker = workersOfPriority.get(index);
            result.put(priority, worker);
        }
        return result;
    }

    private Set<Priority> determinePriorities(Solution solution) {
        List<WorkerChain> workerChains1 = solution.getWorkerChains();
        WorkerChain workerChain = workerChains1.get(0);
        SortedMap<Priority, Worker> workers = workerChain.getWorkers();
        Set<Priority> result = workers.keySet();
        return result;
    }

    private int determineNumWorkerChains(Solution solution) {
        List<WorkerChain> workerChains1 = solution.getWorkerChains();
        int result = workerChains1.size();
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
            int max = worker.getMaxNumTasks();
            while (existing != null) {
                logger.info("Slot %d of Worker %d already occupied, try next one...",
                        slotIndex,
                        workerIndex);
                slotIndex++;
                if (slotIndex >= max) {
                    logger.info("reached end at %d, now start with 0...", max);
                    slotIndex = 0;
                }
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
        List<Priority> priorities = new ArrayList<>(determinePriorities(solution));
        int numPriorities = priorities.size();
        int selectedPriority = randomService.nextRandom(numPriorities);
        Priority priority = priorities.get(selectedPriority);
        List<Task> tasks = converter.toTaskList(solution, priority);
        int size = tasks.size();
        int index0 = randomService.nextRandom(size);
        Task task0 = tasks.get(index0);
        int index1 = randomService.nextRandom(size);
        Task task1 = tasks.get(index1);
        Map<Task, Coordinate> converted = converter.toMapTaskCoordinate(solution, priority);
        Coordinate coordinate0 = converted.get(task0);
        Coordinate coordinate1 = converted.get(task1);
        int workerindex0 = coordinate0.getWorkerIndex();
        int slotindex0 = coordinate0.getSlotIndex();
        int workerindex1 = coordinate1.getWorkerIndex();
        int slotindex1 = coordinate1.getSlotIndex();
        Worker worker0 = solution.getWorkerAt(priority, workerindex0);
        worker0.deleteTaskAt(slotindex0);
        worker0.setTaskAt(slotindex0, task1);
        Worker worker1 = solution.getWorkerAt(priority, workerindex1);
        worker1.deleteTaskAt(slotindex1);
        worker1.setTaskAt(slotindex1, task0);
    }
}
