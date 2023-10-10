package cj.software.genetics.schedule.entity;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class WorkerChain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final SortedMap<Integer, Worker> workers = new TreeMap<>();

    @Min(1)
    private int maxNumTasks;

    private WorkerChain() {
    }

    public int getMaxNumTasks() {
        return maxNumTasks;
    }

    private void setMaxNumTasks(int maxNumTasks) {
        this.maxNumTasks = maxNumTasks;
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            Worker worker = Worker.builder().withMaxNumTasks(maxNumTasks).build();
            this.workers.put(iPrio, worker);
        }
    }

    public SortedMap<Integer, Worker> getWorkers() {
        return Collections.unmodifiableSortedMap(workers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setTaskAt(int position, Task task) {
        int priority = task.getPriority();
        Worker worker = workers.get(priority);
        worker.setTaskAt(position, task);
    }

    public Task getTaskAt(int priority, int position) {
        Worker worker = workers.get(priority);
        Task result = worker.getTaskAt(position);
        return result;
    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            result.addAll(workers.get(iPrio).getTasks());
        }
        return result;
    }

    public static class Builder {
        protected WorkerChain instance;

        protected Builder() {
            instance = new WorkerChain();
        }

        public WorkerChain build() {
            WorkerChain result = instance;
            instance = null;
            return result;
        }

        public Builder withMaxNumTasks(int maxNumTasks) {
            instance.setMaxNumTasks(maxNumTasks);
            return this;
        }
    }
}