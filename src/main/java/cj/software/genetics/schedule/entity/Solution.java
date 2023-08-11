package cj.software.genetics.schedule.entity;

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

    private Solution() {
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addWorker(Worker worker) {
        workers.add(worker);
    }

    public static class Builder {
        protected Solution instance;

        protected Builder() {
            instance = new Solution();
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