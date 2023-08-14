package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Genetics.class)
class GeneticsTest {

    @Autowired
    private Genetics genetics;

    @MockBean
    private Converter converter;

    @MockBean
    private RandomService randomService;

    @Test
    void metadata() {
        Service service = Genetics.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    private Solution createSolution1(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        Worker worker2 = new WorkerBuilder().build();
        worker0.setTaskAt(1, tasks.get(0));
        worker0.setTaskAt(0, tasks.get(1));
        worker2.setTaskAt(3, tasks.get(2));
        worker2.setTaskAt(1, tasks.get(3));
        worker1.setTaskAt(2, tasks.get(4));
        Solution result = Solution.builder(10, 11).withWorkers(worker0, worker1, worker2).build();
        return result;
    }

    private Map<Task, Coordinate> createConverted1(List<Task> tasks) {
        Map<Task, Coordinate> result = Map.of(
                tasks.get(0), Coordinate.builder().withWorkerIndex(0).withSlotIndex(1).build(),
                tasks.get(1), Coordinate.builder().withWorkerIndex(0).withSlotIndex(0).build(),
                tasks.get(2), Coordinate.builder().withWorkerIndex(2).withSlotIndex(3).build(),
                tasks.get(3), Coordinate.builder().withWorkerIndex(2).withSlotIndex(1).build(),
                tasks.get(4), Coordinate.builder().withWorkerIndex(1).withSlotIndex(2).build());
        return result;
    }

    private Solution createSolution2(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        Worker worker2 = new WorkerBuilder().build();
        worker1.setTaskAt(3, tasks.get(0));
        worker0.setTaskAt(1, tasks.get(1));
        worker0.setTaskAt(4, tasks.get(2));
        worker2.setTaskAt(2, tasks.get(3));
        worker1.setTaskAt(0, tasks.get(4));
        Solution result = Solution.builder(12, 13).withWorkers(worker0, worker1, worker2).build();
        return result;
    }

    private Solution createExpected(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        Worker worker2 = new WorkerBuilder().build();
        worker1.setTaskAt(3, tasks.get(0));
        worker0.setTaskAt(0, tasks.get(1));
        worker2.setTaskAt(3, tasks.get(2));
        worker2.setTaskAt(1, tasks.get(3));
        worker1.setTaskAt(0, tasks.get(4));
        Solution result = Solution.builder(16, 17).withWorkers(worker0, worker1, worker2).build();
        return result;
    }

    private Map<Task, Coordinate> createConverted2(List<Task> tasks) {
        Map<Task, Coordinate> result = Map.of(
                tasks.get(0), Coordinate.builder().withWorkerIndex(1).withSlotIndex(3).build(),
                tasks.get(1), Coordinate.builder().withWorkerIndex(0).withSlotIndex(1).build(),
                tasks.get(2), Coordinate.builder().withWorkerIndex(0).withSlotIndex(4).build(),
                tasks.get(3), Coordinate.builder().withWorkerIndex(2).withSlotIndex(2).build(),
                tasks.get(4), Coordinate.builder().withWorkerIndex(1).withSlotIndex(0).build());
        return result;
    }

    @Test
    void geneExchange() {
        // create objects
        List<Task> tasks = createTasks(5);
        Solution parent1 = createSolution1(tasks);
        Solution parent2 = createSolution2(tasks);
        Map<Task, Coordinate> converted1 = createConverted1(tasks);
        Map<Task, Coordinate> converted2 = createConverted2(tasks);
        Solution expected = createExpected(tasks);
        int numWorkers = 3;
        int numSlots = 100;

        // configure mocks
        when(converter.toMapTaskCoordinate(parent1)).thenReturn(converted1);
        when(converter.toMapTaskCoordinate(parent2)).thenReturn(converted2);
        when(randomService.nextRandom(5)).thenReturn(3).thenReturn(1);
        when(converter.toTaskList(parent1)).thenReturn(tasks);

        // invoke
        Solution offspring = genetics.mate(16, 17, parent1, parent2, numWorkers, numSlots);

        // checks
        verify(converter).toMapTaskCoordinate(parent1);
        verify(converter).toMapTaskCoordinate(parent2);
        verify(converter).toTaskList(parent1);
        verify(randomService, times(2)).nextRandom(5);
        assertThat(offspring).as("offspring").usingRecursiveComparison().isEqualTo(expected);
    }

    private List<Task> createTasks(int numTasks) {
        List<Task> result = new ArrayList<>(numTasks);
        for (int iTask = 0; iTask < numTasks; iTask++) {
            Task task = Task.builder().withIdentifier(iTask).withDurationSeconds(10 + 10 * iTask).build();
            result.add(task);
        }
        return result;
    }

    @Test
    void withOccupiedPosition() {
        // create objects
        List<Task> tasks = createTasks(3);
        Solution parent1 = createOccSolution1(tasks);
        Solution parent2 = createOccSolution2(tasks);
        Map<Task, Coordinate> converted1 = createOccConverted1(tasks);
        Map<Task, Coordinate> converted2 = createOccConverted2(tasks);
        Solution expected = createOccExpected(tasks);
        int numWorkers = 2;
        int numSlots = 100;

        // configure mocks
        when(converter.toMapTaskCoordinate(parent1)).thenReturn(converted1);
        when(converter.toMapTaskCoordinate(parent2)).thenReturn(converted2);
        when(randomService.nextRandom(3)).thenReturn(0).thenReturn(2);
        when(converter.toTaskList(parent1)).thenReturn(tasks);

        // invoke
        Solution offspring = genetics.mate(18, 19, parent1, parent2, numWorkers, numSlots);

        // checks
        verify(converter).toMapTaskCoordinate(parent1);
        verify(converter).toMapTaskCoordinate(parent2);
        verify(converter).toTaskList(parent1);
        verify(randomService, times(2)).nextRandom(3);
        assertThat(offspring).as("offspring").usingRecursiveComparison().isEqualTo(expected);
    }

    private Solution createOccSolution1(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        worker0.setTaskAt(0, tasks.get(0));
        worker1.setTaskAt(1, tasks.get(1));
        worker0.setTaskAt(2, tasks.get(2));
        Solution result = Solution.builder(20, 21).withWorkers(worker0, worker1).build();
        return result;
    }

    private Map<Task, Coordinate> createOccConverted1(List<Task> tasks) {
        Map<Task, Coordinate> result = Map.of(
                tasks.get(0), Coordinate.builder().withWorkerIndex(0).withSlotIndex(0).build(),
                tasks.get(1), Coordinate.builder().withWorkerIndex(1).withSlotIndex(1).build(),
                tasks.get(2), Coordinate.builder().withWorkerIndex(0).withSlotIndex(2).build());
        return result;
    }

    private Solution createOccSolution2(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        worker1.setTaskAt(3, tasks.get(0));
        worker0.setTaskAt(1, tasks.get(1));
        worker0.setTaskAt(0, tasks.get(2));
        Solution result = Solution.builder(22, 23).withWorkers(worker0, worker1).build();
        return result;
    }

    private Map<Task, Coordinate> createOccConverted2(List<Task> tasks) {
        Map<Task, Coordinate> result = Map.of(
                tasks.get(0), Coordinate.builder().withWorkerIndex(1).withSlotIndex(3).build(),
                tasks.get(1), Coordinate.builder().withWorkerIndex(0).withSlotIndex(1).build(),
                tasks.get(2), Coordinate.builder().withWorkerIndex(0).withSlotIndex(0).build());
        return result;
    }

    private Solution createOccExpected(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        worker0.setTaskAt(0, tasks.get(0));
        worker1.setTaskAt(1, tasks.get(1));
        worker0.setTaskAt(1, tasks.get(2));
        Solution result = Solution.builder(18, 19).withWorkers(worker0, worker1).build();
        return result;
    }

    @Test
    void mutate() {
        List<Task> tasks = createTasks(3);
        Solution solution = createOccSolution2(tasks);
        Map<Task, Coordinate> converted = createOccConverted2(tasks);
        Solution expected = createMutated(tasks);

        when(converter.toMapTaskCoordinate(solution)).thenReturn(converted);
        when(converter.toTaskList(solution)).thenReturn(tasks);
        when(randomService.nextRandom(3)).thenReturn(0).thenReturn(1);

        genetics.mutate(solution);

        assertThat(solution).as("mutated").usingRecursiveComparison().isEqualTo(expected);
    }

    private Solution createMutated(List<Task> tasks) {
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().build();
        worker0.setTaskAt(1, tasks.get(0));
        worker1.setTaskAt(3, tasks.get(1));
        worker0.setTaskAt(0, tasks.get(2));
        Solution result = Solution.builder(22, 23).withWorkers(worker0, worker1).build();
        return result;
    }
}
