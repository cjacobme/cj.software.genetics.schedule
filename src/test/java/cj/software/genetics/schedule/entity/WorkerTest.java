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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class WorkerTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Worker.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Worker.Builder builder = Worker.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Worker.class);

        Worker instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getTasks()).as("tasks").isEmpty();
        softy.assertThat(instance.getMaxNumTasks()).as("max num tasks").isZero();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        int numTasks = 20;
        Worker instance = Worker.builder()
                .withMaxNumTasks(numTasks)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getTasks()).as("tasks").isEmpty();
        softy.assertThat(instance.getMaxNumTasks()).as("max num tasks").isEqualTo(20);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Worker instance = new WorkerBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Worker>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void addTasksAt() {
        Worker instance = new WorkerBuilder().build();
        Task task = new TaskBuilder().build();
        instance.setTaskAt(0, task);
        assertThat(instance.getTasks()).as("tasks array").containsExactly(task);

        Task occupied = instance.getTaskAt(0);
        assertThat(occupied).isSameAs(task);

    }

    @Test
    void whenOccupiedThenThrowException() {
        Worker instance = new WorkerBuilder().build();
        Task task = new TaskBuilder().build();
        instance.setTaskAt(13, task);
        try {
            instance.setTaskAt(13, task);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException exception) {
            String msg = exception.getMessage();
            assertThat(msg).as("exception message").isEqualTo("Slot 13 already occupied");
        }
    }

    @Test
    void tooSmallIndexThrowsException() {
        Worker instance = new WorkerBuilder().build();
        Task task = new TaskBuilder().build();
        try {
            instance.setTaskAt(-1, task);
            fail("expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException exception) {
            String message = exception.getMessage();
            assertThat(message).isEqualTo("Index -1 out of bounds for length 100");
        }
    }

    @Test
    void tooGreatIndexThrowsException() {
        Worker instance = new WorkerBuilder().build();
        Task task = new TaskBuilder().build();
        try {
            instance.setTaskAt(100, task);
            fail("expected exception not thrown");
        } catch (ArrayIndexOutOfBoundsException exception) {
            String message = exception.getMessage();
            assertThat(message).isEqualTo("Index 100 out of bounds for length 100");
        }
    }

    @Test
    void iterateOnSome() {
        Worker instance = new WorkerBuilder().build();
        Task task1 = new TaskBuilder().withIdentifier(1).build();
        Task task13 = new TaskBuilder().withIdentifier(13).build();
        Task task65 = new TaskBuilder().withIdentifier(65).build();
        instance.setTaskAt(15, task65);
        instance.setTaskAt(20, task1);
        instance.setTaskAt(44, task13);

        List<Task> tasks = instance.getTasks();

        assertThat(tasks).as("tasks list").isEqualTo(List.of(task65, task1, task13));
    }
}