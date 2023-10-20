package cj.software.genetics.schedule.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class WorkerChain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final SortedMap<Integer, Worker> workers = new TreeMap<>();

    private WorkerChain() {
    }

    public Worker[] getWorkers() {
        Worker[] result = new Worker[workers.size()];
        int index = 0;
        for (SortedMap.Entry<Integer, Worker> entry : workers.entrySet()) {
            result[index] = entry.getValue();
            index++;
        }
        return result;
    }

    public Worker getWorkerForPriority(int priority) {
        Worker result = this.workers.get(priority);
        return result;
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
        for (SortedMap.Entry<Integer, Worker> entry : workers.entrySet()) {
            Worker worker = entry.getValue();
            result.addAll(worker.getTasks());
        }
        return result;
    }

    public static class Builder {
        protected WorkerChain instance;

        protected Builder() {
            instance = new WorkerChain();
        }

        public Builder withWorkers(SortedMap<Integer, Worker> workers) {
            instance.workers.clear();
            if (workers != null) {
                instance.workers.putAll(workers);
            }
            return this;
        }

        public WorkerChain build() {
            WorkerChain result = instance;
            instance = null;
            return result;
        }
    }
}