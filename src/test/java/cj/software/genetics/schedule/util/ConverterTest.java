package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Coordinate;
import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.SolutionBuilder;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.PriorityBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Converter.class)
class ConverterTest {

    @Autowired
    private Converter converter;

    @Test
    void metadata() {
        Service service = Converter.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void toTaskList() {
        Solution solution = new SolutionBuilder().build();
        Priority priority = new PriorityBuilder().withValue(1).build();
        List<Task> taskList = converter.toTaskList(solution, priority);
        List<Task> fromSolutionBuilder = SolutionBuilder.createTasks();
        List<Task> expected = List.of(fromSolutionBuilder.get(2), fromSolutionBuilder.get(5));
        assertThat(taskList).usingRecursiveAssertion().isEqualTo(expected);
    }

    @Test
    void solutionToMap1() {
        Solution solution = new SolutionBuilder().build();
        Priority priority = new PriorityBuilder().build();
        List<Task> tasks = SolutionBuilder.createTasks();
        Map<Task, Coordinate> expected = Map.of(
                tasks.get(0), Coordinate.builder().withWorkerIndex(0).withSlotIndex(0).build(),
                tasks.get(3), Coordinate.builder().withWorkerIndex(0).withSlotIndex(2).build());
        solutionToMap(solution, expected, priority);
    }

    @Test
    void solutionToMap2() {
        fail("to be refined");
        // TODO refine
        /*
        int numTasks = 10;
        Task[] tasks = new Task[numTasks];
        for (int iTask = 0; iTask < numTasks; iTask++) {
            tasks[iTask] = Task.builder().withDurationSeconds(iTask * 10 + 10).withIdentifier(iTask).withPriority(iTask % 3).build();
        }
        int numWorkers = 4;
        WorkerChain[] workers = new WorkerChain[numWorkers];
        for (int iWorker = 0; iWorker < numWorkers; iWorker++) {
            workers[iWorker] = new WorkerChainBuilder().build();
        }
        int iWorker = 0;
        for (int iTask = 0; iTask < numTasks; iTask++) {
            workers[iWorker].setTaskAt(iTask, tasks[iTask]);
            iWorker++;
            if (iWorker >= numWorkers) {
                iWorker = 0;
            }
        }
        Solution solution = Solution.builder(0, 1).withWorkerChains(workers).build();
        Map<Task, Coordinate> expected = Map.of(
                tasks[1], Coordinate.builder().withWorkerIndex(1).withSlotIndex(1).build(),
                tasks[4], Coordinate.builder().withWorkerIndex(0).withSlotIndex(4).build(),
                tasks[7], Coordinate.builder().withWorkerIndex(3).withSlotIndex(7).build());
        solutionToMap(solution, expected, 1);

         */
    }

    private void solutionToMap(Solution solution, Map<Task, Coordinate> expected, Priority prio) {
        Map<Task, Coordinate> actual = converter.toMapTaskCoordinate(solution, prio);
        SoftAssertions softy = new SoftAssertions();
        for (Map.Entry<Task, Coordinate> actEntry : actual.entrySet()) {
            Task task = actEntry.getKey();
            Coordinate actCoordinate = actEntry.getValue();
            Coordinate expCoordinate = expected.get(task);
            softy.assertThat(actCoordinate).as("coordinate for %s", task).isEqualTo(expCoordinate);
        }
        softy.assertAll();
        softy = new SoftAssertions();
        for (Map.Entry<Task, Coordinate> expEntry : expected.entrySet()) {
            Task task = expEntry.getKey();
            Coordinate expCoordinate = expEntry.getValue();
            Coordinate actCoordinate = actual.get(task);
            softy.assertThat(actCoordinate).as("coordinate for %s", task).isEqualTo(expCoordinate);
        }
        softy.assertAll();
        assertThat(actual).isEqualTo(expected);
    }
}
