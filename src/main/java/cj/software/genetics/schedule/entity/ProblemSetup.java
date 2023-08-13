package cj.software.genetics.schedule.entity;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;

public class ProblemSetup implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Min(2)
    private int numWorkers;

    @Min(10)
    private int numSlots;

    private int numTasks10;

    private int numTasks20;

    private int numTasks50;

    private int numTasks100;

    private ProblemSetup() {
    }

    public int getNumWorkers() {
        return numWorkers;
    }

    public int getNumSlots() {
        return numSlots;
    }

    public int getNumTasks10() {
        return numTasks10;
    }

    public int getNumTasks20() {
        return numTasks20;
    }

    public int getNumTasks50() {
        return numTasks50;
    }

    public int getNumTasks100() {
        return numTasks100;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected ProblemSetup instance;

        protected Builder() {
            instance = new ProblemSetup();
        }

        public ProblemSetup build() {
            ProblemSetup result = instance;
            instance = null;
            return result;
        }

        public Builder withNumWorkers(int numWorkers) {
            instance.numWorkers = numWorkers;
            return this;
        }

        public Builder withNumSlots(int numSlots) {
            instance.numSlots = numSlots;
            return this;
        }

        public Builder withNumTasks10(int numTasks10) {
            instance.numTasks10 = numTasks10;
            return this;
        }

        public Builder withNumTasks20(int numTasks20) {
            instance.numTasks20 = numTasks20;
            return this;
        }

        public Builder withNumTasks50(int numTasks50) {
            instance.numTasks50 = numTasks50;
            return this;
        }

        public Builder withNumTasks100(int numTasks100) {
            instance.numTasks100 = numTasks100;
            return this;
        }
    }
}