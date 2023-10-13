package cj.software.genetics.schedule.entity;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WorkerChain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Worker[] workers = new Worker[3];

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
            this.workers[iPrio] = Worker.builder().withMaxNumTasks(maxNumTasks).build();
        }
    }

    public Worker[] getWorkers() {
        Worker[] result = Arrays.copyOf(this.workers, 3);
        return result;
    }

    public Worker getWorkerForPriority(int priority) {
        Worker result = this.workers[priority];
        return result;
    }

    public void setWorkerAt(int priority, Worker worker) {
        workers[priority] = worker;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setTaskAt(int position, Task task) {
        int priority = task.getPriority();
        Worker worker = workers[priority];
        worker.setTaskAt(position, task);
    }

    public Task getTaskAt(int priority, int position) {
        Worker worker = workers[priority];
        Task result = worker.getTaskAt(position);
        return result;
    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            result.addAll(workers[iPrio].getTasks());
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