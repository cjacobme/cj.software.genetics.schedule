package cj.software.genetics.schedule.entity.setup;

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

class SolutionSetupTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = SolutionSetup.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        SolutionSetup.Builder builder = SolutionSetup.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                SolutionSetup.class);

        SolutionSetup instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSolutions()).as("number of solutions").isNull();
        softy.assertThat(instance.getNumWorkers()).as("number of workers").isNull();
        softy.assertThat(instance.getNumSlots()).as("number of slots").isNull();
        softy.assertThat(instance.getElitismCount()).as("elitism count").isNull();
        softy.assertThat(instance.getTournamentSize()).as("tournament size").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer numSolutions = -1;
        Integer numWorkers = -2;
        Integer numSlots = -3;
        Integer elitismCount = -4;
        Integer tournamentSize = -5;

        SolutionSetup instance = SolutionSetup.builder()
                .withNumSolutions(numSolutions)
                .withNumWorkers(numWorkers)
                .withNumSlots(numSlots)
                .withElitismCount(elitismCount)
                .withTournamentSize(tournamentSize)
                .build();

        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSolutions()).as("number of solutions").isEqualTo(numSolutions);
        softy.assertThat(instance.getNumWorkers()).as("number of workers").isEqualTo(numWorkers);
        softy.assertThat(instance.getNumSlots()).as("number of slots").isEqualTo(numSlots);
        softy.assertThat(instance.getElitismCount()).as("elitism count").isEqualTo(elitismCount);
        softy.assertThat(instance.getTournamentSize()).as("tournament size").isEqualTo(tournamentSize);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        SolutionSetup instance = new SolutionSetupBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SolutionSetup>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}