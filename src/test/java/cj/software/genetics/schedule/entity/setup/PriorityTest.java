package cj.software.genetics.schedule.entity.setup;

import javafx.scene.paint.Color;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PriorityTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Priority.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Priority.Builder builder = Priority.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Priority.class);

        Priority instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getValue()).as("value").isNull();
        softy.assertThat(instance.getForeground()).as("foreground").isNull();
        softy.assertThat(instance.getBackground()).as("background").isNull();
        softy.assertThat(instance.getTasks()).as("tasks").isEmpty();
        softy.assertThat(instance.getNumSlots()).as("number of slots").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer value = -13;
        Color foreground = Color.LIGHTGRAY;
        Color background = Color.BLANCHEDALMOND;
        Tasks tasksDefault = new TasksBuilder().build();
        Tasks tasksMinus13 = new TasksBuilder().withDurationSeconds(-13).build();
        Collection<Tasks> tasks = List.of(tasksDefault, tasksMinus13);
        Integer numSlots = -3;

        Priority instance = Priority.builder()
                .withValue(value)
                .withNumSlots(numSlots)
                .withForeground(foreground)
                .withBackground(background)
                .withTasks(tasks)
                .build();

        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getValue()).as("prio value").isEqualTo(value);
        softy.assertThat(instance.getNumSlots()).as("number of slots").isEqualTo(numSlots);
        softy.assertThat(instance.getForeground()).as("foreground").isEqualTo(Color.valueOf("d3d3d3ff"));
        softy.assertThat(instance.getBackground()).as("background").isEqualTo(Color.valueOf("ffebcdff"));
        softy.assertThat(instance.getTasks())
                .as("tasks")
                .extracting("durationSeconds", "numberTasks")
                .containsExactly(
                        tuple(-13, 100),
                        tuple(10, 100));
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Priority instance = new PriorityBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Priority>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
        Color background = instance.getBackground();
        Color foreground = instance.getForeground();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(background).as("background").isEqualTo(Color.RED);
        softy.assertThat(foreground).as("foreground").isEqualTo(Color.BLACK);
        softy.assertAll();
    }

    @Test
    void sort() {
        Priority instance0 = new PriorityBuilder().build();
        Priority instance10 = new PriorityBuilder().withValue(10).build();
        Priority instance7 = new PriorityBuilder().withValue(7).build();
        List<Priority> priorities = new ArrayList<>(List.of(instance0, instance10, instance7));

        Collections.sort(priorities);

        assertThat(priorities)
                .extracting("value")
                .containsExactly(0, 7, 10);
    }

    @Test
    void equalObjects() {
        Priority instance1 = new PriorityBuilder().build();
        Priority instance2 = new PriorityBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        Priority instance1 = new PriorityBuilder().build();
        Priority instance2 = new PriorityBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalValue() {
        Priority instance1 = new PriorityBuilder().build();
        Priority instance2 = new PriorityBuilder().withValue(-4711).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalValueHashes() {
        Priority instance1 = new PriorityBuilder().build();
        Priority instance2 = new PriorityBuilder().withValue(-4711).build();
        assertThat(instance1).isNotEqualTo(instance2);
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void differentObject() {
        Priority instance1 = new PriorityBuilder().build();
        Object instance2 = "hello world";
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void stringPresentation() {
        Priority instance = new PriorityBuilder().withValue(42).build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Priority[42]");
    }
}