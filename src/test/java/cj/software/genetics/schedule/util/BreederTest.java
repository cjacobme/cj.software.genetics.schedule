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
@SpringBootTest(classes = Breeder.class)
class BreederTest {

    @Autowired
    private Breeder breeder;

    @MockBean
    private Converter converter;

    @MockBean
    private RandomService randomService;

    @Test
    void metadata() {
        Service service = Breeder.class.getAnnotation(Service.class);
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
        Solution result = Solution.builder().withWorkers(worker0, worker1, worker2).build();
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
        Solution result = Solution.builder().withWorkers(worker0, worker1, worker2).build();
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
        Solution result = Solution.builder().withWorkers(worker0, worker1, worker2).build();
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
    void geneExchange1() {
        // create objects
        int numTasks = 5;
        List<Task> tasks = new ArrayList<>(numTasks);
        for (int iTask = 0; iTask < numTasks; iTask++) {
            Task task = Task.builder().withIdentifier(iTask).withDurationSeconds(10 + 10 * iTask).build();
            tasks.add(task);
        }
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
        Solution offspring = breeder.mate(parent1, parent2, numWorkers, numSlots);

        // checks
        verify(converter).toMapTaskCoordinate(parent1);
        verify(converter).toMapTaskCoordinate(parent2);
        verify(converter).toTaskList(parent1);
        verify(randomService, times(2)).nextRandom(5);
        assertThat(offspring).as("offspring").usingRecursiveComparison().isEqualTo(expected);
    }
}
