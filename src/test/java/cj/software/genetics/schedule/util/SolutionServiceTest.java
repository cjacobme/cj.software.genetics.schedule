package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.Worker;
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

        // invoke
        Solution solution = solutionService.createInitialSoluation(numWorkers, numSlots, tasks);

        // checks
        assertThat(solution).as("solutions").isNotNull();
        List<Worker> workers = solution.getWorkers();
        assertThat(workers).as("list of workers").hasSize(2);
        Worker worker0 = workers.get(0);
        Worker worker1 = workers.get(1);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(worker0).as("worker[0]").isNotNull();
        softy.assertThat(worker1).as("worker[1]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = worker0.getTasks();
        List<Task> tasksOf1 = worker1.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(worker0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(numSlots);
        softy.assertThat(worker1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(numSlots);
        softy.assertThat(tasksOf0).as("tasks of worker 0").isEqualTo(List.of(task1, task3));
        softy.assertThat(tasksOf1).as("tasks of worker 1").isEqualTo(List.of(task2));
        softy.assertAll();

        verify(randomService, times(3)).nextRandom(2);
        verify(randomService, times(3)).nextRandom(3);
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
        Solution solution = solutionService.createInitialSoluation(3, 4, tasks);

        // checks
        List<Worker> workers = solution.getWorkers();
        assertThat(workers).as("list of workers").hasSize(3);
        Worker worker0 = workers.get(0);
        Worker worker1 = workers.get(1);
        Worker worker2 = workers.get(2);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(worker0).as("worker[0]").isNotNull();
        softy.assertThat(worker1).as("worker[1]").isNotNull();
        softy.assertThat(worker2).as("worker[2]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = worker0.getTasks();
        List<Task> tasksOf1 = worker1.getTasks();
        List<Task> tasksOf2 = worker2.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(worker0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(4);
        softy.assertThat(worker1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(4);
        softy.assertThat(worker1.getMaxNumTasks()).as("max num tasks[2]").isEqualTo(4);
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
        Solution solution = solutionService.createInitialSoluation(numWorkers, numSlots, tasks);

        // checks
        assertThat(solution).as("solutions").isNotNull();
        List<Worker> workers = solution.getWorkers();
        assertThat(workers).as("list of workers").hasSize(2);
        Worker worker0 = workers.get(0);
        Worker worker1 = workers.get(1);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(worker0).as("worker[0]").isNotNull();
        softy.assertThat(worker1).as("worker[1]").isNotNull();
        softy.assertAll();
        List<Task> tasksOf0 = worker0.getTasks();
        List<Task> tasksOf1 = worker1.getTasks();
        softy = new SoftAssertions();
        softy.assertThat(worker0.getMaxNumTasks()).as("max num tasks[0]").isEqualTo(numSlots);
        softy.assertThat(worker1.getMaxNumTasks()).as("max num tasks[1]").isEqualTo(numSlots);
        softy.assertThat(tasksOf0).as("tasks of worker 0").isEqualTo(List.of(task1, task3));
        softy.assertThat(tasksOf1).as("tasks of worker 1").isEqualTo(List.of(task2));
        softy.assertAll();

        verify(randomService, times(3)).nextRandom(2);
        verify(randomService, times(4)).nextRandom(3);
    }
}
