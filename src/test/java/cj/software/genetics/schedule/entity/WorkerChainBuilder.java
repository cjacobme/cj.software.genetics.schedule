package cj.software.genetics.schedule.entity;

import java.util.SortedMap;
import java.util.TreeMap;

public class WorkerChainBuilder extends WorkerChain.Builder {

    public static SortedMap<Integer, Worker> createDefaultPriorities() {
        SortedMap<Integer, Worker> result = new TreeMap<>();

        Worker worker0 = new WorkerBuilder().build();
        result.put(0, worker0);

        Worker worker1 = Worker.builder()
                .withMaxNumTasks(258)
                .build();
        result.put(1, worker1);

        return result;
    }

    public WorkerChainBuilder() {
        super.withWorkers(createDefaultPriorities());
    }
}
