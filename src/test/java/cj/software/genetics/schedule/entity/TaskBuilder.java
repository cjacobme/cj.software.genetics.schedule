package cj.software.genetics.schedule.entity;

import cj.software.genetics.schedule.entity.setup.PriorityBuilder;

public class TaskBuilder extends Task.Builder {
    public TaskBuilder() {
        super.withIdentifier(13)
                .withDurationSeconds(20)
                .withPriority(new PriorityBuilder().build());
    }
}
