package cj.software.genetics.schedule.entity.setup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class SolutionSetup implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer numSolutions;

    @NotNull
    @Min(1)
    private Integer numWorkers;

    @NotNull
    @Min(1)
    private Integer numSlots;

    @NotNull
    @Min(0)
    private Integer elitismCount;

    @NotNull
    @Min(1)
    private Integer tournamentSize;

    private SolutionSetup() {
    }

    public Integer getNumSolutions() {
        return numSolutions;
    }

    public Integer getNumWorkers() {
        return numWorkers;
    }

    public Integer getNumSlots() {
        return numSlots;
    }

    public Integer getElitismCount() {
        return elitismCount;
    }

    public Integer getTournamentSize() {
        return tournamentSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected SolutionSetup instance;

        protected Builder() {
            instance = new SolutionSetup();
        }

        public SolutionSetup build() {
            SolutionSetup result = instance;
            instance = null;
            return result;
        }

        public Builder withNumSolutions(Integer numSolutions) {
            instance.numSolutions = numSolutions;
            return this;
        }

        public Builder withNumWorkers(Integer numWorkers) {
            instance.numWorkers = numWorkers;
            return this;
        }

        public Builder withNumSlots(Integer numSlots) {
            instance.numSlots = numSlots;
            return this;
        }

        public Builder withElitismCount(Integer elitismCount) {
            instance.elitismCount = elitismCount;
            return this;
        }

        public Builder withTournamentSize(Integer tournamentSize) {
            instance.tournamentSize = tournamentSize;
            return this;
        }
    }
}