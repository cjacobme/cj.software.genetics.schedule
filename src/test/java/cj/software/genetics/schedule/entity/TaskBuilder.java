package cj.software.genetics.schedule.entity;

public class TaskBuilder extends Task.Builder{
    public TaskBuilder() {
        super.withIdentifier(13).withDurationSeconds(20);
    }
}
