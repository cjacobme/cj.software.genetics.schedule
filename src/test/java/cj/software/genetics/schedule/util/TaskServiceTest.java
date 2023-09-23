package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

        when(randomService.nextRandom(3)).thenReturn(0, 2, 2);

        List<Task> tasks = taskService.createTasks(startIndex, durationInSeconds, count);

        assertThat(tasks).as("tasks").usingRecursiveComparison().isEqualTo(List.of(
                Task.builder().withDurationSeconds(10).withIdentifier(0).withPriority(0).build(),
                Task.builder().withDurationSeconds(10).withIdentifier(1).withPriority(2).build(),
                Task.builder().withDurationSeconds(10).withIdentifier(2).withPriority(2).build()));

        verify(randomService, times(3)).nextRandom(3);
    }

    @Test
    void generate20Seconds() {
        when(randomService.nextRandom(3)).thenReturn(2, 1);

        List<Task> tasks = taskService.createTasks(25, 20, 2);
        assertThat(tasks).as("tasks").usingRecursiveComparison().isEqualTo(List.of(
                Task.builder().withDurationSeconds(20).withIdentifier(25).withPriority(2).build(),
                Task.builder().withDurationSeconds(20).withIdentifier(26).withPriority(1).build()));

        verify(randomService, times(2)).nextRandom(3);
    }
}
