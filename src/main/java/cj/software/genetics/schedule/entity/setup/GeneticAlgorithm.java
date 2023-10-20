package cj.software.genetics.schedule.entity.setup;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class GeneticAlgorithm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private final SortedSet<@Valid Priority> priorities = new TreeSet<>();

    @NotNull
    @Valid
    private SolutionSetup solutionSetup;

    private int cycleCounter;

    private GeneticAlgorithm() {
    }

    public SortedSet<Priority> getPriorities() {
        return Collections.unmodifiableSortedSet(priorities);
    }

    public SolutionSetup getSolutionSetup() {
        return solutionSetup;
    }

    public void add(Priority priority) {
        priorities.add(priority);
    }

    public int getCycleCounter() {
        return cycleCounter;
    }

    public int incCycleCounter() {
        cycleCounter++;
        return cycleCounter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected GeneticAlgorithm instance;

        protected Builder() {
            instance = new GeneticAlgorithm();
        }

        public GeneticAlgorithm build() {
            GeneticAlgorithm result = instance;
            instance = null;
            return result;
        }

        public Builder withSolutionSetup(SolutionSetup solutionSetup) {
            instance.solutionSetup = solutionSetup;
            return this;
        }

        public Builder withPriorities(Collection<Priority> priorities) {
            instance.priorities.clear();
            if (priorities != null) {
                instance.priorities.addAll(priorities);
            }
            return this;
        }
    }
}