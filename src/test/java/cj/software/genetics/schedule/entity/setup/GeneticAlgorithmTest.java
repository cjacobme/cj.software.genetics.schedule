package cj.software.genetics.schedule.entity.setup;

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

class GeneticAlgorithmTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = GeneticAlgorithm.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        GeneticAlgorithm.Builder builder = GeneticAlgorithm.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                GeneticAlgorithm.class);

        GeneticAlgorithm instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getPriorities()).as("priorities").isEmpty();
        softy.assertThat(instance.getSolutionSetup()).as("solution setup").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        SolutionSetup solutionSetup = SolutionSetup.builder().build();
        Collection<Priority> priorities = List.of(
                new PriorityBuilder().build(),
                new PriorityBuilder().withValue(1).build()
        );
        GeneticAlgorithm instance = GeneticAlgorithm.builder()
                .withSolutionSetup(solutionSetup)
                .withPriorities(priorities)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSolutionSetup()).as("solution setup").isSameAs(solutionSetup);
        softy.assertThat(instance.getPriorities()).as("priorities").containsExactlyElementsOf(priorities);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        GeneticAlgorithm instance = new GeneticAlgorithmBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<GeneticAlgorithm>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void addPriority() {
        GeneticAlgorithm instance = new GeneticAlgorithmBuilder().build();
        Priority priority = Priority.builder().withValue(15).build();

        instance.add(priority);

        assertThat(instance.getPriorities()).as("priorities").hasSize(4);

    }
}