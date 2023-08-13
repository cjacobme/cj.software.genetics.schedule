package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.BreedingStepEvent;
import org.springframework.context.ApplicationListener;

public class TestEventListener implements ApplicationListener<BreedingStepEvent> {
    private int counter;

    @Override
    public void onApplicationEvent(BreedingStepEvent event) {
        counter = event.getCounter();
    }

    public int getCounter() {
        return counter;
    }

    public void resetCounter() {
        counter = 0;
    }
}
