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
@SpringBootTest(classes = {Breeder.class, TestEventListener.class})
class BreederTest {

    @Autowired
    private Breeder breeder;

    @Autowired
    private TestEventListener listener;

    @MockBean
    private RandomService randomService;

    @MockBean
    private Genetics genetics;

    @MockBean
    private SolutionService solutionService;

    @Test
    void metadata() {
        Service service = Breeder.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    private Solution mockSolution(int duration) {
        Solution result = new SolutionBuilder().build();
        result.setDurationInSeconds(duration);
        return result;
    }

    private List<Solution> createPopulation(int... durations) {
        List<Solution> result = new ArrayList<>();
        for (int duration : durations) {
            Solution solution = mockSolution(duration);
            result.add(solution);
        }
        return result;
    }

    /**
     * tests first scenario. The population is made of 5 {@link cj.software.genetics.schedule.entity.Solution} objects.
     * The elitism count is 1, so the best is selected for the new population. The tournament size is 2.
     */
    @Test
    void singleStep() {
        List<Solution> population = createPopulation(50, 1, 47, 42, 37);
        List<Solution> offsprings = createPopulation(10, 9, 8, 7);
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
        int cycleCounter = 15;

        when(randomService.shuffledUpTo(5)).thenReturn(shuffles[0], shuffles[1], shuffles[2], shuffles[3]);
        when(genetics.mate(cycleCounter, 0, population.get(4), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(0));
        when(genetics.mate(cycleCounter, 1, population.get(3), population.get(2), numWorkers, numSlots)).thenReturn(offsprings.get(1));
        when(genetics.mate(cycleCounter, 2, population.get(2), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(2));
        when(genetics.mate(cycleCounter, 3, population.get(0), population.get(3), numWorkers, numSlots)).thenReturn(offsprings.get(3));

        List<Solution> nextGeneration = breeder.step(cycleCounter, population, elitismCount, tournamentSize, numWorkers, numSlots);

        verify(genetics).mate(cycleCounter, 0, population.get(4), population.get(1), numWorkers, numSlots);
        verify(genetics).mate(cycleCounter, 1, population.get(3), population.get(2), numWorkers, numSlots);
        verify(genetics).mate(cycleCounter, 2, population.get(2), population.get(1), numWorkers, numSlots);
        verify(genetics).mate(cycleCounter, 3, population.get(0), population.get(3), numWorkers, numSlots);
        verify(randomService, times(4)).shuffledUpTo(5);
        verify(genetics, times(4)).mate(anyInt(), anyInt(), any(Solution.class), any(Solution.class), eq(numWorkers), eq(numSlots));
        verify(solutionService).sortDescendingDuration(anyList());
        assertThat(nextGeneration).hasSize(5);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(nextGeneration.get(0)).isSameAs(population.get(1));
        softy.assertThat(nextGeneration.get(1)).isSameAs(offsprings.get(0));
        softy.assertThat(nextGeneration.get(2)).isSameAs(offsprings.get(1));
        softy.assertThat(nextGeneration.get(3)).isSameAs(offsprings.get(2));
        softy.assertThat(nextGeneration.get(4)).isSameAs(offsprings.get(3));
        softy.assertAll();
    }

    @Test
    void multipleSteps() {
        listener.resetCounter();
        List<Solution> population = createPopulation(50, 1, 47, 42, 37);
        List<Solution> offsprings = createPopulation(10, 9, 8, 7);
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
        int numSteps = 7;
        int cycleCounter = 3;

        when(randomService.shuffledUpTo(5)).thenReturn(shuffles[0], shuffles[1], shuffles[2], shuffles[3]);
        when(genetics.mate(cycleCounter, 0, population.get(4), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(0));
        when(genetics.mate(cycleCounter, 1, population.get(3), population.get(2), numWorkers, numSlots)).thenReturn(offsprings.get(1));
        when(genetics.mate(cycleCounter, 2, population.get(2), population.get(1), numWorkers, numSlots)).thenReturn(offsprings.get(2));
        when(genetics.mate(cycleCounter, 3, population.get(0), population.get(3), numWorkers, numSlots)).thenReturn(offsprings.get(3));

        List<Solution> nextGeneration = breeder.multipleSteps(
                cycleCounter,
                numSteps,
                population,
                elitismCount,
                tournamentSize,
                numWorkers,
                numSlots);
        assertThat(nextGeneration).isNotNull();
        assertThat(listener.getCounter()).isEqualTo(6);
        verify(randomService, times(28)).shuffledUpTo(5);
    }
}
