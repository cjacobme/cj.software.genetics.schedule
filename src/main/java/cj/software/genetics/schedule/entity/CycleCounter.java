package cj.software.genetics.schedule.entity;

public interface CycleCounter {
    /**
     * returns the current value of the cycle counter
     *
     * @return current value
     */
    int getCycleCounter();

    /**
     * increments the current cycle counter
     *
     * @return the incremented value
     */
    int incCycleCounter();
}
