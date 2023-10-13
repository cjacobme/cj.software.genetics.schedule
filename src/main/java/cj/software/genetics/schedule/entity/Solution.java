package cj.software.genetics.schedule.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<WorkerChain> workerChains = new ArrayList<>();

    private double fitnessValue = Double.MAX_VALUE;

    private int durationInSeconds = 0;

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

    public List<WorkerChain> getWorkerChains() {
        return workerChains;
    }

    public static Builder builder(int cycleCounter, int indexInCycle) {
        return new Builder(cycleCounter, indexInCycle);
    }

    public void addWorkerChain(WorkerChain workerChain) {
        workerChains.add(workerChain);
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        if (durationInSeconds != 0) {
            fitnessValue = 1.0 / durationInSeconds;
        } else {
            fitnessValue = Double.MAX_VALUE;
        }
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(cycleCounter)
                .append(indexInCycle);
        String result = builder.build();
        return result;
    }

    public Worker getWorkerAt(int priority, int workerindex) {
        WorkerChain workerChain = this.workerChains.get(workerindex);
        Worker result = workerChain.getWorkerForPriority(priority);
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

        public Builder withDurationInSeconds(int duration) {
            instance.setDurationInSeconds(duration);
            return this;
        }

        public Builder withWorkerChains(WorkerChain... workerChains) {
            instance.workerChains.clear();
            if (workerChains != null) {
                instance.workerChains.addAll(Arrays.asList(workerChains));
            }
            return this;
        }

        public Builder withWorkerChains(List<WorkerChain> workerChains) {
            instance.workerChains.clear();
            if (workerChains != null) {
                instance.workerChains.addAll(workerChains);
            }
            return this;
        }
    }
}