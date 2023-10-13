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
import java.util.Collection;
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
        softy.assertThat(instance.getForegroundColor()).as("foreground color").isNull();
        softy.assertThat(instance.getBackgroundColor()).as("background color").isNull();
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

        Priority instance = Priority.builder()
                .withValue(value)
                .withForeground(foreground)
                .withBackground(background)
                .withTasks(tasks)
                .build();

        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getValue()).as("prio value").isEqualTo(value);
        softy.assertThat(instance.getForeground()).as("foreground").isEqualTo("0xd3d3d3ff");
        softy.assertThat(instance.getBackground()).as("background").isEqualTo("0xffebcdff");
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
        Color background = instance.getBackgroundColor();
        Color foreground = instance.getForegroundColor();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(background).as("background").isEqualTo(Color.RED);
        softy.assertThat(foreground).as("foreground").isEqualTo(Color.BLACK);
        softy.assertAll();
    }
}