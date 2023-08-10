package cj.software.genetics.schedule.entity;

public class WorkerBuilder extends Worker.Builder {
    public WorkerBuilder() {
        super.withMaxNumTasks(100);
    }
}
