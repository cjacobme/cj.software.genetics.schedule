package cj.software.genetics.schedule.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerChainTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = WorkerChain.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        WorkerChain.Builder builder = WorkerChain.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                WorkerChain.class);

        WorkerChain instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getWorkers()).as("workers").isEmpty();
        softy.assertThat(instance.getMaxNumTasks()).as("max num tasks").isZero();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        int maxNumTasks = 123;
        WorkerChain instance = WorkerChain.builder()
                .withMaxNumTasks(maxNumTasks)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getMaxNumTasks()).as("max num tasks").isEqualTo(maxNumTasks);
        SortedMap<Integer, Worker> workers = instance.getWorkers();
        assertThat(workers).as("workers").hasSize(3);
        SoftAssertions softy = new SoftAssertions();
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            Worker worker = workers.get(iPrio);
            int workerNumTasks = worker.getMaxNumTasks();
            softy.assertThat(workerNumTasks).as("num tasks in worker %d", iPrio).isEqualTo(maxNumTasks);
        }
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        WorkerChain instance = new WorkerChainBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<WorkerChain>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void setTaskAt() {
        int priority = 0;
        int position = 0;
        Task task = new TaskBuilder().withPriority(priority).build();
        WorkerChain instance = new WorkerChainBuilder().build();
        instance.setTaskAt(position, task);
        SortedMap<Integer, Worker> workers = instance.getWorkers();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(workers.get(0).getTasks()).as("tasks prio 0").containsExactly(task);
        softy.assertThat(workers.get(1).getTasks()).as("tasks prio 1").isEmpty();
        softy.assertThat(workers.get(2).getTasks()).as("tasks prio 2").isEmpty();
        softy.assertAll();

        Task occupied = instance.getTaskAt(priority, position);
        assertThat(occupied).as("occupied").isSameAs(task);
    }

    @Test
    void iterate() {
        WorkerChain instance = new WorkerChainBuilder().build();
        Task task1 = new TaskBuilder().withIdentifier(1).withPriority(2).build();
        Task task65 = new TaskBuilder().withIdentifier(65).withPriority(0).build();
        Task task32 = new TaskBuilder().withIdentifier(32).withPriority(0).build();
        Task task73 = new TaskBuilder().withIdentifier(73).withPriority(1).build();
        Task task2 = new TaskBuilder().withIdentifier(2).withPriority(2).build();

        instance.setTaskAt(12, task65);
        instance.setTaskAt(20, task32);
        instance.setTaskAt(5, task73);
        instance.setTaskAt(5, task1);
        instance.setTaskAt(1, task2);

        List<Task> tasks = instance.getTasks();
        assertThat(tasks).as("tasks").isEqualTo(List.of(task65, task32, task73, task2, task1));
    }
}