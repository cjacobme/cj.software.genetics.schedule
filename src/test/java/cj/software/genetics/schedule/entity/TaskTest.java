package cj.software.genetics.schedule.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Task.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Task.Builder builder = Task.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Task.class);

        Task instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getIdentifier()).as("identifier").isZero();
        softy.assertThat(instance.getDurationSeconds()).as("duration in seconds").isZero();
        softy.assertThat(instance.getPriority()).as("priority").isZero();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        int identifier = 4711;
        int durationSeconds = 15;
        int priority = 1;
        Task instance = Task.builder()
                .withIdentifier(identifier)
                .withDurationSeconds(durationSeconds)
                .withPriority(priority)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getIdentifier()).as("identifier").isEqualTo(identifier);
        softy.assertThat(instance.getDurationSeconds()).as("duration in seconds").isEqualTo(durationSeconds);
        softy.assertThat(instance.getPriority()).as("priority").isEqualTo(priority);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Task instance = new TaskBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Task>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void equalTasks() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalWithDifferentDurations() {
        Task instance1 = new TaskBuilder().withDurationSeconds(1).build();
        Task instance2 = new TaskBuilder().withDurationSeconds(2).build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void unequalDifferentIdentifiers() {
        Task instance1 = new TaskBuilder().withIdentifier(1).build();
        Task instance2 = new TaskBuilder().withIdentifier(2).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void equalHashesWithDifferentDurations() {
        Task instance1 = new TaskBuilder().withDurationSeconds(1).build();
        Task instance2 = new TaskBuilder().withDurationSeconds(2).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalHashesDifferentIdentifiers() {
        Task instance1 = new TaskBuilder().withIdentifier(1).build();
        Task instance2 = new TaskBuilder().withIdentifier(2).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void unequalOtherObject() {
        Task instance1 = new TaskBuilder().build();
        String object2 = "Hello";
        assertThat(instance1).isNotEqualTo(object2);
    }

    @Test
    void stringPresentation() {
        Task task = new TaskBuilder().build();
        String asString = task.toString();
        assertThat(asString).as("String presentation").isEqualTo("Task[identifier=13,duration=20,prio=3]");
    }
}