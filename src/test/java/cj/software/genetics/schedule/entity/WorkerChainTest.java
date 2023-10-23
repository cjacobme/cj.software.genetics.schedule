package cj.software.genetics.schedule.entity;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.PriorityBuilder;
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
import java.util.TreeMap;

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
        assertThat(instance.getWorkers()).as("workers").isEmpty();
    }

    @Test
    void constructFilled() {
        int[] maxNumTasks = new int[]{100, 200, 300};
        Priority prio0 = new PriorityBuilder().build();
        Priority prio1 = new PriorityBuilder().withValue(1).build();
        Priority prio2 = new PriorityBuilder().withValue(2).build();
        Worker worker0 = new WorkerBuilder().build();
        Worker worker1 = new WorkerBuilder().withMaxNumTasks(200).build();
        Worker worker2 = new WorkerBuilder().withMaxNumTasks(300).build();
        SortedMap<Priority, Worker> map = new TreeMap<>();
        map.put(prio0, worker0);
        map.put(prio1, worker1);
        map.put(prio2, worker2);
        WorkerChain instance = WorkerChain.builder()
                .withWorkers(map)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        Worker[] workers = instance.getWorkersAsArray();
        assertThat(workers).as("workers").hasSize(3);
        SoftAssertions softy = new SoftAssertions();
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            Worker worker = workers[iPrio];
            int workerNumTasks = worker.getMaxNumTasks();
            softy.assertThat(workerNumTasks).as("num tasks in worker %d", iPrio).isEqualTo(maxNumTasks[iPrio]);
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
        int position = 0;
        WorkerChain instance = new WorkerChainBuilder().build();
        Priority priority = instance.getWorkers().firstKey();
        Task task = new TaskBuilder().withPriority(priority).build();
        instance.setTaskAt(position, task);
        Worker[] workers = instance.getWorkersAsArray();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(workers[0].getTasks()).as("tasks prio 0").containsExactly(task);
        softy.assertThat(workers[1].getTasks()).as("tasks prio 1").isEmpty();
        softy.assertThat(workers[2].getTasks()).as("tasks prio 2").isEmpty();
        softy.assertAll();

        Task occupied = instance.getTaskAt(priority, position);
        assertThat(occupied).as("occupied").isSameAs(task);
    }

    @Test
    void iterate() {
        Priority prio0 = new PriorityBuilder().build();
        Priority prio1 = new PriorityBuilder().withValue(1).build();
        Priority prio2 = new PriorityBuilder().withValue(2).build();
        WorkerChain instance = new WorkerChainBuilder().build();
        Task task1 = new TaskBuilder().withIdentifier(1).withPriority(prio2).build();
        Task task65 = new TaskBuilder().withIdentifier(65).withPriority(prio0).build();
        Task task32 = new TaskBuilder().withIdentifier(32).withPriority(prio0).build();
        Task task73 = new TaskBuilder().withIdentifier(73).withPriority(prio1).build();
        Task task2 = new TaskBuilder().withIdentifier(2).withPriority(prio2).build();

        instance.setTaskAt(12, task65);
        instance.setTaskAt(20, task32);
        instance.setTaskAt(5, task73);
        instance.setTaskAt(5, task1);
        instance.setTaskAt(1, task2);

        List<Task> tasks = instance.getTasks();
        assertThat(tasks).as("tasks").isEqualTo(List.of(task65, task32, task73, task2, task1));
    }
}