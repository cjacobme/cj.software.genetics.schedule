package cj.software.genetics.schedule.entity;

public class WorkerChainBuilder extends WorkerChain.Builder {
    public WorkerChainBuilder() {
        super.withMaxNumTasks(100);
    }
}
