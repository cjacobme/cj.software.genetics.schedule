package cj.software.genetics.schedule.entity;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class BreedingStepEvent extends ApplicationEvent {

    private final int counter;

    public BreedingStepEvent(int counter, List<Solution> solutions) {
        super(solutions);
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
