package cj.software.genetics.schedule.entity.setup;

public class TasksBuilder extends Tasks.Builder {
    public TasksBuilder() {
        super.withDurationSeconds(10).withNumberTasks(100);
    }
}
