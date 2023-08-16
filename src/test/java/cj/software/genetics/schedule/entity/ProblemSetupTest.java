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

class ProblemSetupTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = ProblemSetup.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        ProblemSetup.Builder builder = ProblemSetup.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                ProblemSetup.class);

        ProblemSetup instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSolutions()).as("number of solutions").isZero();
        softy.assertThat(instance.getNumWorkers()).as("number of workers").isZero();
        softy.assertThat(instance.getNumSlots()).as("number of slots").isZero();
        softy.assertThat(instance.getNumTasks10()).as("number of tasks 10 s").isZero();
        softy.assertThat(instance.getNumTasks20()).as("number of tasks 20 s").isZero();
        softy.assertThat(instance.getNumTasks50()).as("number of tasks 50 s").isZero();
        softy.assertThat(instance.getNumTasks100()).as("number of tasks 100 s").isZero();
        softy.assertThat(instance.getElitismCount()).as("elitism count").isZero();
        softy.assertThat(instance.getTournamentSize()).as("tournament size").isZero();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        int numSolutions = -1;
        int numWorkers = 1;
        int numSlots = 2;
        int numTasks10 = 3;
        int numTasks20 = 4;
        int numTasks50 = 5;
        int numTasks100 = 6;
        int elitismCount = 7;
        int tournamenSize = 8;
        ProblemSetup instance = ProblemSetup.builder()
                .withNumSolutions(numSolutions)
                .withNumWorkers(numWorkers)
                .withNumSlots(numSlots)
                .withNumTasks10(numTasks10)
                .withNumTasks20(numTasks20)
                .withNumTasks50(numTasks50)
                .withNumTasks100(numTasks100)
                .withElitismCount(elitismCount)
                .withTournamentSize(tournamenSize)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSolutions()).isEqualTo(numSolutions);
        softy.assertThat(instance.getNumWorkers()).isEqualTo(numWorkers);
        softy.assertThat(instance.getNumSlots()).isEqualTo(numSlots);
        softy.assertThat(instance.getNumTasks10()).isEqualTo(numTasks10);
        softy.assertThat(instance.getNumTasks20()).isEqualTo(numTasks20);
        softy.assertThat(instance.getNumTasks50()).isEqualTo(numTasks50);
        softy.assertThat(instance.getNumTasks100()).isEqualTo(numTasks100);
        softy.assertThat(instance.getElitismCount()).isEqualTo(elitismCount);
        softy.assertThat(instance.getTournamentSize()).isEqualTo(tournamenSize);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        ProblemSetup instance = new ProblemSetupBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ProblemSetup>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}