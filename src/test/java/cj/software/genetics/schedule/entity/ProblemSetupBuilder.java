package cj.software.genetics.schedule.entity;

public class ProblemSetupBuilder extends ProblemSetup.Builder {
    public ProblemSetupBuilder() {
        super.withNumSolutions(100)
                .withNumWorkers(3)
                .withNumSlots(100)
                .withNumTasks10(20)
                .withNumTasks20(5)
                .withNumTasks50(5)
                .withNumTasks100(2)
                .withNumTasks1000(1)
                .withElitismCount(2)
                .withTournamentSize(5);
    }
}
