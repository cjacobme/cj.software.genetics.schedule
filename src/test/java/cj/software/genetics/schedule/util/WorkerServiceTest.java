package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Task;
import cj.software.genetics.schedule.entity.TaskBuilder;
import cj.software.genetics.schedule.entity.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WorkerService.class)
class WorkerServiceTest {
    @Autowired
    private WorkerService workerService;

    @Test
    void noTasks() {
        Worker worker = buildWorker();
        double duration = workerService.calcDuration(worker);
        assertThat(duration).isZero();
    }

    @Test
    void duration20() {
        Task task0 = new TaskBuilder().build();
        Worker worker = buildWorker(task0);
        double duration = workerService.calcDuration(worker);
        assertThat(duration).isEqualTo(20.0, withPrecision(0.0001));
    }

    @Test
    void duration4() {
        Task task0 = new TaskBuilder().withDurationSeconds(1).build();
        Task task1 = new TaskBuilder().withDurationSeconds(1).build();
        Task task2 = new TaskBuilder().withDurationSeconds(1).build();
        Task task3 = new TaskBuilder().withDurationSeconds(1).build();
        Worker worker = buildWorker(task0, task1, task2, task3);
        double duration = workerService.calcDuration(worker);
        assertThat(duration).isEqualTo(4.0, withPrecision(0.0001));
    }

    private Worker buildWorker(Task... tasks) {
        Worker result;
        if (tasks != null) {
            int numSlots = tasks.length;
            result = Worker.builder().withMaxNumTasks(numSlots).build();
            for (int i = 0; i < numSlots; i++) {
                result.setTaskAt(i, tasks[i]);
            }
        } else {
            result = Worker.builder().build();
        }
        return result;
    }
}
