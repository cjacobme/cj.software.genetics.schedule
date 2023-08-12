package cj.software.genetics.schedule.entity;

public class CoordinateBuilder extends Coordinate.Builder {
    public CoordinateBuilder() {
        super.withWorkerIndex(12).withSlotIndex(5);
    }
}
