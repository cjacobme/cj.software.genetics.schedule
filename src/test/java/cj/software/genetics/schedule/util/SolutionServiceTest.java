package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.SolutionBuilder;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.WorkerChain;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SolutionService.class)
class SolutionServiceTest {
    @Autowired
    private SolutionService solutionService;

    @MockBean
    private RandomService randomService;

    @MockBean
    private WorkerChainService workerChainService;

    @Test
    void tasks3workers2WoConflicts() {
        // create objects
        Task task1 = Task.builder().withIdentifier(1).withDurationSeconds(10).build();
        Task task2 = Task.builder().withIdentifier(2).withDurationSeconds(20).build();
        Task task3 = Task.builder().withIdentifier(3).withDurationSeconds(30).build();
        List<Task> tasks = List.of(task1, task2, task3);
        int numSlots = 3;
        int numWorkers = 2;

        // configure mocks
        when(randomService.nextRandom(2)).thenReturn(0, 1, 0);
        when(randomService.nextRandom(3)).thenReturn(0, 0, 1);
        when(workerChainService.calcDuration(any(WorkerChain.class))).thenReturn(3, 2);

        // invoke
        Solution solution = solutionService.createInitialSolution(1, numWorkers, numSlots, tasks);

        // checks
        assertThat(solution).as("solutions").isNotNull();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        assertThat(workerChains).as("list of workers").hasSize(2);
        WorkerChain workerChain0 = workerChains.get(0);
        WorkerChain workerChain1 = workerChains.get(1);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(workerChain0).as("worker[0]").isNotNull();
        softy.assertThat(workerChain1).as("worker[1]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = workerChain0.getTasks();
        List<Task> tasksOf1 = workerChain1.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(workerChain0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(numSlots);
        softy.assertThat(workerChain1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(numSlots);
        softy.assertThat(tasksOf0).as("tasks of worker 0").isEqualTo(List.of(task1, task3));
        softy.assertThat(tasksOf1).as("tasks of worker 1").isEqualTo(List.of(task2));
        softy.assertThat(solution.getDurationInSeconds()).as("duration in seconds").isEqualTo(3);
        softy.assertAll();

        verify(randomService, times(3)).nextRandom(2);
        verify(randomService, times(3)).nextRandom(3);
        verify(workerChainService, times(2)).calcDuration(any(WorkerChain.class));
    }

    @Test
    void tasks4workers3WoConflict() {
        // create objects
        Task task0 = Task.builder().withIdentifier(10).withDurationSeconds(10).build();
        Task task1 = Task.builder().withIdentifier(11).withDurationSeconds(10).build();
        Task task2 = Task.builder().withIdentifier(12).withDurationSeconds(10).build();
        Task task3 = Task.builder().withIdentifier(13).withDurationSeconds(10).build();
        List<Task> tasks = List.of(task0, task1, task2, task3);

        // configure mocks
        when(randomService.nextRandom(3)).thenReturn(0);
        when(randomService.nextRandom(4)).thenReturn(3, 2, 1, 0);

        // invoke
        Solution solution = solutionService.createInitialSolution(5, 3, 4, tasks);

        // checks
        List<WorkerChain> workerChains = solution.getWorkerChains();
        assertThat(workerChains).as("list of workers").hasSize(3);
        WorkerChain workerChain0 = workerChains.get(0);
        WorkerChain workerChain1 = workerChains.get(1);
        WorkerChain workerChain2 = workerChains.get(2);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(workerChain0).as("worker[0]").isNotNull();
        softy.assertThat(workerChain1).as("worker[1]").isNotNull();
        softy.assertThat(workerChain2).as("worker[2]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = workerChain0.getTasks();
        List<Task> tasksOf1 = workerChain1.getTasks();
        List<Task> tasksOf2 = workerChain2.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(solution.getCycleCounter()).as("cycle counter").isZero();
        softy.assertThat(solution.getIndexInCycle()).as("index in cycle").isEqualTo(5);
        softy.assertThat(workerChain0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(4);
        softy.assertThat(workerChain1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(4);
        softy.assertThat(workerChain1.getMaxNumTasks()).as("max num tasks[2]").isEqualTo(4);
        softy.assertThat(tasksOf0).as("tasks of worker 0").isEqualTo(List.of(task3, task2, task1, task0));
        softy.assertThat(tasksOf1).as("tasks of worker 1").isEmpty();
        softy.assertThat(tasksOf2).as("tasks of worker 2").isEmpty();
        softy.assertAll();

        verify(randomService, times(4)).nextRandom(3);
        verify(randomService, times(4)).nextRandom(4);
    }

    @Test
    void tasks3workers2WithConflicts() {
        // create objects
        Task task1 = Task.builder().withIdentifier(1).withDurationSeconds(10).build();
        Task task2 = Task.builder().withIdentifier(2).withDurationSeconds(20).build();
        Task task3 = Task.builder().withIdentifier(3).withDurationSeconds(30).build();
        List<Task> tasks = List.of(task1, task2, task3);
        int numSlots = 3;
        int numWorkers = 2;

        // configure mocks
        when(randomService.nextRandom(2)).thenReturn(0, 1, 0);
        when(randomService.nextRandom(3)).thenReturn(0, 0, 0, 2);

        // invoke
        Solution solution = solutionService.createInitialSolution(12, numWorkers, numSlots, tasks);

        // checks
        assertThat(solution).as("solutions").isNotNull();
        List<WorkerChain> workerChains = solution.getWorkerChains();
        assertThat(workerChains).as("list of workers").hasSize(2);
        WorkerChain workerChain0 = workerChains.get(0);
        WorkerChain workerChain1 = workerChains.get(1);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(workerChain0).as("worker[0]").isNotNull();
        softy.assertThat(workerChain1).as("worker[1]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = workerChain0.getTasks();
        List<Task> tasksOf1 = workerChain1.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(workerChain0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(numSlots);
        softy.assertThat(workerChain1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(numSlots);
        softy.assertThat(tasksOf0).as("tasks of worker 0").isEqualTo(List.of(task1, task3));
        softy.assertThat(tasksOf1).as("tasks of worker 1").isEqualTo(List.of(task2));
        softy.assertAll();

        verify(randomService, times(3)).nextRandom(2);
        verify(randomService, times(4)).nextRandom(3);
    }

    @Test
    void duration4711() {
        Solution solution = new SolutionBuilder().build();

        when(workerChainService.calcDuration(any(WorkerChain.class))).thenReturn(0, 4711);

        int duration = solutionService.calcDuration(solution);

        assertThat(duration).isEqualTo(4711);
    }

    @Test
    void duration42() {
        Solution solution = new SolutionBuilder().build();

        when(workerChainService.calcDuration(any(WorkerChain.class))).thenReturn(42, 0);

        int duration = solutionService.calcDuration(solution);

        assertThat(duration).isEqualTo(42);
    }
}
