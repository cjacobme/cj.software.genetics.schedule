package cj.software.genetics.schedule.entity.setup;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TasksTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Tasks.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Tasks.Builder builder = Tasks.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Tasks.class);

        Tasks instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationSeconds()).as("duration / seconds").isNull();
        softy.assertThat(instance.getNumberTasks()).as("tasks count").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer durationSeconds = -3;
        Integer numberTasks = -15;
        Tasks instance = Tasks.builder()
                .withDurationSeconds(durationSeconds)
                .withNumberTasks(numberTasks)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationSeconds()).as("duration / seconds").isEqualTo(durationSeconds);
        softy.assertThat(instance.getNumberTasks()).as("number of tasks").isEqualTo(numberTasks);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Tasks instance = new TasksBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Tasks>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void equalObjects() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void equalDurations() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().withNumberTasks(1341234).build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalDurationHashes() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().withNumberTasks(1341234).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalDurations() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().withDurationSeconds(1341234).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalDurationHashes() {
        Tasks instance1 = new TasksBuilder().build();
        Tasks instance2 = new TasksBuilder().withDurationSeconds(1341234).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void differentObject() {
        Tasks instance1 = new TasksBuilder().build();
        Object instance2 = 3.14;
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void stringPresentation() {
        Tasks instance = new TasksBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Tasks[duration=10,number=100]");
    }

    @Test
    void sort() {
        Tasks instance1 = new TasksBuilder().withDurationSeconds(1).build();
        Tasks instance42 = new TasksBuilder().withDurationSeconds(42).build();
        Tasks instance1995 = new TasksBuilder().withDurationSeconds(1995).build();
        List<Tasks> tasks = new ArrayList<>();
        tasks.add(instance42);
        tasks.add(instance1995);
        tasks.add(instance1);

        Collections.sort(tasks);

        assertThat(tasks).isEqualTo(List.of(instance1, instance42, instance1995));
    }
}