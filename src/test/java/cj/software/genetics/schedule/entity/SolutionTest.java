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
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;

class SolutionTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Solution.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Solution.Builder builder = Solution.builder(0, 0);
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Solution.class);

        Solution instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getWorkers()).as("workers").isEmpty();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Worker worker1 = mock(Worker.class);
        Worker worker2 = mock(Worker.class);
        Worker worker3 = mock(Worker.class);

        Solution instance = Solution.builder(10, 12)
                .withWorkers(worker1, worker2, worker3)
                .build();

        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getCycleCounter()).as("cycle counter").isEqualTo(10);
        softy.assertThat(instance.getIndexInCycle()).as("index in cycle").isEqualTo(12);
        softy.assertThat(instance.getWorkers()).as("workers").isEqualTo(List.of(worker1, worker2, worker3));
        softy.assertAll();
        Worker worker4 = mock(Worker.class);
        instance.addWorker(worker4);
        assertThat(instance.getWorkers()).as("workers").hasSize(4).contains(worker4);
    }

    @Test
    void constructFilledWithList() {
        Worker worker1 = mock(Worker.class);
        Worker worker2 = mock(Worker.class);
        Worker worker3 = mock(Worker.class);
        List<Worker> workers = List.of(worker2, worker3, worker1);

        Solution instance = Solution.builder(3, 4)
                .withWorkers(workers)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getCycleCounter()).as("cycle counter").isEqualTo(3);
        softy.assertThat(instance.getIndexInCycle()).as("index in cycle").isEqualTo(4);
        softy.assertThat(instance.getWorkers()).as("workers").isEqualTo(List.of(worker2, worker3, worker1));
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Solution instance = new SolutionBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Solution>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        Solution instance = new SolutionBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Solution[47,11]");
    }

    @Test
    void duration() {
        Solution instance = new SolutionBuilder().build();
        instance.setDurationInSeconds(10.0);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationInSeconds()).as("duration").isEqualTo(10.0, within(0.0001));
        softy.assertThat(instance.getFitnessValue()).as("fitness value").isEqualTo(0.1, within(0.0001));
        softy.assertAll();
    }
}