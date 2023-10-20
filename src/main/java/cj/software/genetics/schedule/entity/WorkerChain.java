package cj.software.genetics.schedule.entity;

import cj.software.genetics.schedule.entity.setup.Priority;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class WorkerChain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final SortedMap<Priority, Worker> workers = new TreeMap<>();

    private WorkerChain() {
    }

    public Worker[] getWorkersAsArray() {
        Worker[] result = new Worker[workers.size()];
        int index = 0;
        for (Map.Entry<Priority, Worker> entry : workers.entrySet()) {
            result[index] = entry.getValue();
            index++;
        }
        return result;
    }

    public SortedMap<Priority, Worker> getWorkers() {
        return Collections.unmodifiableSortedMap(workers);
    }

    public Worker getWorkerForPriority(int priorityValue) {
        Priority priority = Priority.builder().withValue(priorityValue).build();
        Worker result = getWorkerForPriority(priority);
        return result;
    }

    public Worker getWorkerForPriority(Priority priority) {
        Worker result = this.workers.get(priority);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setTaskAt(int position, Task task) {
        int priorityValue = task.getPriority();
        Priority priority = Priority.builder().withValue(priorityValue).build();
        Worker worker = workers.get(priority);
        worker.setTaskAt(position, task);
    }

    public Task getTaskAt(Priority priority, int position) {
        Worker worker = workers.get(priority);
        Task result = worker.getTaskAt(position);
        return result;
    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        for (Map.Entry<Priority, Worker> entry : workers.entrySet()) {
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

        public Builder withWorkers(SortedMap<Priority, Worker> workers) {
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