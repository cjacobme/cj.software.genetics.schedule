package cj.software.genetics.schedule.entity;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a Worker has a series of {@link Task} objects which it consumes one after the other. If the problem to be solved
 * has for example 100 Tasks, each worker gets 100 slots for them. So, the slots can be empty.
 */
public class Worker implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Task[] tasks = new Task[]{};

    @Min(1)
    private int maxNumTasks;

    private Worker() {
    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task != null) {
                result.add(task);
            }
        }
        return result;
    }

    public int getMaxNumTasks() {
        return maxNumTasks;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void setTaskAt(int position, Task task) {
        if (tasks[position] != null) {
            throw new IllegalArgumentException(String.format("Slot %d already occupied", position));
        }
        tasks[position] = task;
    }

    public Task getTaskAt(int position) {
        return tasks[position];
    }

    public Map<Task, Integer> getTasksWithSlots() {
        Map<Task, Integer> result = new HashMap<>();
        if (tasks != null) {
            int length = tasks.length;
            for (int i = 0; i < length; i++) {
                Task task = tasks[i];
                if (task != null) {
                    result.put(task, i);
                }
            }
        }
        return result;
    }

    public static class Builder {
        protected Worker instance;

        protected Builder() {
            instance = new Worker();
        }

        public Worker build() {
            Worker result = instance;
            instance = null;
            return result;
        }

        public Builder withMaxNumTasks(int numTasks) {
            instance.maxNumTasks = numTasks;
            instance.tasks = new Task[numTasks];
            return this;
        }
    }
}