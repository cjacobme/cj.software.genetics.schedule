package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.setup.GeneticAlgorithm;
import cj.software.genetics.schedule.entity.setup.GeneticAlgorithmBuilder;
import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.PriorityBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskService.class)
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @MockBean
    private RandomService randomService;

    @Test
    void metadata() {
        Service service = TaskService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void generate10Seconds() {
        int startIndex = 0;
        int durationInSeconds = 10;
        int count = 3;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithmBuilder().build();

        SortedMap<Priority, List<Task>> prioMap = taskService.createTasks(geneticAlgorithm);

        SortedMap<Priority, List<Task>> expected = createExpectedForDefault();
        assertThat(prioMap).as("prio map").usingRecursiveComparison().isEqualTo(expected);
    }

    private SortedMap<Priority, List<Task>> createExpectedForDefault() {
        SortedMap<Priority, List<Task>> result = new TreeMap<>();
        int startIndex = 0;

        Priority prio0 = new PriorityBuilder().build();
        List<Task> tasks0 = createTasks(10, 100, startIndex, prio0);
        startIndex += 100;
        tasks0.addAll(createTasks(50, 20, startIndex, prio0));
        startIndex += 20;
        result.put(prio0, tasks0);

        Priority prio1 = GeneticAlgorithmBuilder.priority1();
        List<Task> tasks1 = createTasks(30, 120, startIndex, prio1);
        startIndex += 120;
        tasks1.addAll(createTasks(60, 25, startIndex, prio1));
        startIndex += 25;
        result.put(prio1, tasks1);

        Priority prio2 = GeneticAlgorithmBuilder.priority2();
        List<Task> tasks2 = createTasks(6, 75, startIndex, prio2);
        startIndex += 75;
        tasks2.addAll(createTasks(15, 50, startIndex, prio2));
        startIndex += 50;
        tasks2.addAll(createTasks(120, 5, startIndex, prio2));
        result.put(prio2, tasks2);

        return result;
    }

    private List<Task> createTasks(int duration, int count, int startIndex, Priority priority) {
        List<Task> result = new ArrayList<>(count);
        int identifier = startIndex;
        for (int i = 0; i < count; i++) {
            Task task = Task.builder().withDurationSeconds(duration).withIdentifier(identifier).withPriority(priority).build();
            result.add(task);
            identifier++;
        }
        return result;
    }

}
