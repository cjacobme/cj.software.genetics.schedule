package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Breeder {

    @Autowired
    private Converter converter;

    @Autowired
    private RandomService randomService;

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
            int slotIndex = coordinate.getSlotIndex();
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
}
