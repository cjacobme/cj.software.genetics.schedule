package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.SolutionSetup;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Service
public class SolutionService {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private RandomService randomService;

    @Autowired
    private WorkerChainService workerChainService;

    public Solution createInitialSolution(int indexInCycle, int numWorkers, int numSlotsPerWorker, List<Task> tasks) {
        throw new UnsupportedOperationException("to be deleted");
        /*
        List<WorkerChain> allWorkersChains = new ArrayList<>();
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            WorkerChain workerChain = WorkerChain.builder()
                    .withMaxNumTasks(numSlotsPerWorker)
                    .build();
            allWorkersChains.add(workerChain);
        }
        for (Task task : tasks) {
            int priority = task.getPriority();
            int selectedWorker = randomService.nextRandom(numWorkers);
            WorkerChain workerChain = allWorkersChains.get(selectedWorker);
            Worker worker = workerChain.getWorkerForPriority(priority);
            int selectedSlot = randomService.nextRandom(numSlotsPerWorker);
            Task occupied = worker.getTaskAt(selectedSlot);
            while (occupied != null) {
                logger.info("slot %d already occupied, try another one", selectedSlot);
                selectedSlot = randomService.nextRandom(numSlotsPerWorker);
                occupied = worker.getTaskAt(selectedSlot);
            }
            worker.setTaskAt(selectedSlot, task);
        }
        Solution result = Solution.builder(0, indexInCycle).withWorkerChains(allWorkersChains).build();
        int durationInSeconds = calcDuration(result);
        result.setDurationInSeconds(durationInSeconds);
        return result;

         */
    }

    public int calcDuration(Solution solution) {
        int result = 0;
        List<WorkerChain> workerChains = solution.getWorkerChains();
        for (WorkerChain workerChain : workerChains) {
            int workerDuration = workerChainService.calcDuration(workerChain);
            result = Math.max(result, workerDuration);
        }
        return result;
    }

    public List<Solution> createInitialPopulation(int numSolutions, int numWorkers, int numSlots, List<Task> tasks) {
        List<Solution> result = new ArrayList<>(numSolutions);
        for (int i = 0; i < numSlots; i++) {
            Solution solution = createInitialSolution(i, numWorkers, numSlots, tasks);
            result.add(solution);
        }
        sortDescendingFitnessValue(result);
        return result;
    }

    public List<Solution> createInitialPopulation(SolutionSetup solutionSetup, SortedMap<Priority, List<Task>> priorities) {
        int numSolutions = solutionSetup.getNumSolutions();
        int numWorkerChains = solutionSetup.getNumWorkers();
        List<Solution> result = new ArrayList<>(numSolutions);
        for (int iSolution = 0; iSolution < numSolutions; iSolution++) {
            WorkerChain[] workerChains = new WorkerChain[numWorkerChains];
            for (int iWorkerChain = 0; iWorkerChain < numWorkerChains; iWorkerChain++) {
                workerChains[iWorkerChain] = workerChainService.createWorkerChain(priorities);
            }
            dispatchTasks(workerChains, priorities);
            Solution solution = Solution.builder(0, iSolution).withWorkerChains(workerChains).build();
            result.add(solution);
            int durationInSeconds = calcDuration(solution);
            solution.setDurationInSeconds(durationInSeconds);
        }
        sortDescendingFitnessValue(result);
        return result;
    }

    private WorkerChain[] dispatchTasks(WorkerChain[] source, SortedMap<Priority, List<Task>> priorities) {
        WorkerChain[] result = source;
        int numWorkerChains = result.length;
        for (Map.Entry<Priority, List<Task>> entry : priorities.entrySet()) {
            Priority priority = entry.getKey();

            List<Task> tasksOfPriority = entry.getValue();
            for (Task task : tasksOfPriority) {
                int workerChainIndex = randomService.nextRandom(numWorkerChains);
                WorkerChain workerChain = result[workerChainIndex];
                Worker worker = workerChain.getWorkerForPriority(priority);
                int numSlots = worker.getMaxNumTasks();
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
        return result;
    }


    public void sortDescendingFitnessValue(List<Solution> solutions) {
        solutions.sort((o1, o2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(o2.getFitnessValue(), o1.getFitnessValue());
            int result = builder.build();
            return result;
        });
    }
}
