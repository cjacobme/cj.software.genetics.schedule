package cj.software.genetics.schedule.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Solution implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Worker> workers = new ArrayList<>();

    private double fitnessValue;

    private int cycleCounter;

    private int indexInCycle;

    private Solution() {
    }

    public int getCycleCounter() {
        return cycleCounter;
    }

    public int getIndexInCycle() {
        return indexInCycle;
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    public static Builder builder(int cycleCounter, int indexInCycle) {
        return new Builder(cycleCounter, indexInCycle);
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(cycleCounter)
                .append(indexInCycle);
        String result = builder.build();
        return result;
    }

    public static class Builder {
        protected Solution instance;

        protected Builder(int cycleCounter, int indexInCycle) {
            instance = new Solution();
            instance.cycleCounter = cycleCounter;
            instance.indexInCycle = indexInCycle;
        }

        public Solution build() {
            Solution result = instance;
            instance = null;
            return result;
        }

        public Builder withWorkers(Worker... workers) {
            instance.workers.clear();
            if (workers != null) {
                instance.workers.addAll(Arrays.asList(workers));
            }
            return this;
        }

        public Builder withWorkers(List<Worker> workers) {
            instance.workers.clear();
            instance.workers.addAll(workers);
            return this;
        }
    }
}