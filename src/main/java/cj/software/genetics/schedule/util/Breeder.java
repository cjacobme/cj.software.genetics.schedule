package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.BreedingStepEvent;
import cj.software.genetics.schedule.entity.CycleCounter;
import cj.software.genetics.schedule.entity.Solution;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Breeder {

    @Autowired
    private RandomService randomService;

    @Autowired
    private Genetics genetics;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private SolutionService solutionService;

    public List<Solution> step(
            int cycleCounter,
            List<Solution> solutions,
            int elitismCount,
            int tournamentSize,
            int numWorkers,
            int numSlots) {
        List<Solution> population = new ArrayList<>(solutions);
        sort(population);
        int size = population.size();
        List<Solution> result = new ArrayList<>(size);
        for (int i = 0; i < elitismCount; i++) {
            result.add(population.get(i));
        }
        for (int i = elitismCount; i < size; i++) {
            Solution parent1 = population.get(i);
            Solution parent2 = arbitraryParent(population, size, tournamentSize);
            Solution offspring = genetics.mate(cycleCounter, i - elitismCount, parent1, parent2, numWorkers, numSlots);
            result.add(offspring);
        }
        solutionService.sortDescendingDuration(result);
        return result;
    }

    public List<Solution> multipleSteps(
            CycleCounter cycleCounter,
            int numSteps,
            List<Solution> solutions,
            int elitismCount,
            int tournamentSize,
            int numWorkers,
            int numSlots) {
        List<Solution> result = solutions;
        for (int step = 0; step < numSteps; step++) {
            int cycleVlaue = cycleCounter.incCycleCounter();
            result = step(cycleVlaue, result, elitismCount, tournamentSize, numWorkers, numSlots);
            BreedingStepEvent event = new BreedingStepEvent(cycleVlaue, result);
            publisher.publishEvent(event);
        }
        return result;
    }

    void sort(List<Solution> population) {
        population.sort((o1, o2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(o2.getFitnessValue(), o1.getFitnessValue());
            int result = builder.build();
            return result;
        });
    }

    Solution arbitraryParent(List<Solution> population, int size, int tournamentSize) {
        int[] indicesShuffled = randomService.shuffledUpTo(size);
        List<Solution> temporary = new ArrayList<>(tournamentSize);
        for (int i = 0; i < tournamentSize; i++) {
            int index = indicesShuffled[i];
            Solution solution = population.get(index);
            temporary.add(solution);
        }
        sort(temporary);
        Solution result = temporary.get(0);
        return result;
    }
}
