package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.SolutionBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Breeder.class)
class BreederTest {

    @Autowired
    private Breeder breeder;

    @MockBean
    private RandomService randomService;

    @MockBean
    private SolutionService solutionService;

    @MockBean
    private Genetics genetics;

    @Test
    void metadata() {
        Service service = Breeder.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    private Solution mockSolution(double fitnessValue) {
        Solution result = new SolutionBuilder().build();
        result.setFitnessValue(fitnessValue);
        when(solutionService.calcFitnessValue(result)).thenReturn(fitnessValue);
        return result;
    }

    private List<Solution> createPopulation(double... fitnessValues) {
        List<Solution> result = new ArrayList<>();
        for (double fitnessValue : fitnessValues) {
            Solution solution = mockSolution(fitnessValue);
            result.add(solution);
        }
        return result;
    }

    /**
     * tests first scenario. The population is made of 5 {@link cj.software.genetics.schedule.entity.Solution} objects.
     * The elitism count is 1, so the best is selected for the new population. The tournament size is 2.
     */
    @Test
    void scenario1() {
        List<Solution> population = createPopulation(2.0, 10.0, 2.1, 2.4, 2.7);
        List<Solution> offsprings = createPopulation(1.0, 1.1, 1.2, 1.3);
        int[][] shuffles = new int[][]{
                {0, 1, 2, 3, 4},
                {4, 3, 2, 1, 0},
                {1, 0, 2, 3, 4},
                {2, 3, 1, 4, 0}
        };
        int elitismCount = 1;
        int tournamentSize = 2;
        int numWorkers = 3;
        int numSlots = 100;

        when(randomService.shuffledUpTo(5)).thenReturn(shuffles[0], shuffles[1], shuffles[2], shuffles[3]);
        when(genetics.mate(population.get(4), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(0));
        when(genetics.mate(population.get(3), population.get(2), numWorkers, numSlots)).thenReturn(offsprings.get(1));
        when(genetics.mate(population.get(2), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(2));
        when(genetics.mate(population.get(0), population.get(3), numWorkers, numSlots)).thenReturn(offsprings.get(3));

        List<Solution> nextGeneration = breeder.step(population, elitismCount, tournamentSize, numWorkers, numSlots);

        verify(genetics).mate(population.get(4), population.get(1), numWorkers, numSlots);
        verify(genetics).mate(population.get(3), population.get(2), numWorkers, numSlots);
        verify(genetics).mate(population.get(2), population.get(1), numWorkers, numSlots);
        verify(genetics).mate(population.get(0), population.get(3), numWorkers, numSlots);
        verify(randomService, times(4)).shuffledUpTo(5);
        verify(genetics, times(4)).mate(any(Solution.class), any(Solution.class), eq(numWorkers), eq(numSlots));
        assertThat(nextGeneration).hasSize(5);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(nextGeneration.get(0)).isSameAs(population.get(1));
        softy.assertThat(nextGeneration.get(1)).isSameAs(offsprings.get(0));
        softy.assertThat(nextGeneration.get(2)).isSameAs(offsprings.get(1));
        softy.assertThat(nextGeneration.get(3)).isSameAs(offsprings.get(2));
        softy.assertThat(nextGeneration.get(4)).isSameAs(offsprings.get(3));
        softy.assertAll();
    }
}
