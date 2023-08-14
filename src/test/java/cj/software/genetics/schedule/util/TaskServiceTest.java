package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskService.class)
class TaskServiceTest {
    @Autowired
    private TaskService taskService;

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
        List<Task> tasks = taskService.createTasks(startIndex, durationInSeconds, count);
        assertThat(tasks).as("tasks").usingRecursiveAssertion().isEqualTo(List.of(
                Task.builder().withDurationSeconds(10).withIdentifier(0).build(),
                Task.builder().withDurationSeconds(10).withIdentifier(1).build(),
                Task.builder().withDurationSeconds(10).withIdentifier(2).build()));
    }

    @Test
    void generate20Seconds() {
        List<Task> tasks = taskService.createTasks(25, 20, 2);
        assertThat(tasks).as("tasks").usingRecursiveAssertion().isEqualTo(List.of(
                Task.builder().withDurationSeconds(20).withIdentifier(25).build(),
                Task.builder().withDurationSeconds(20).withIdentifier(26).build()));
    }
}
