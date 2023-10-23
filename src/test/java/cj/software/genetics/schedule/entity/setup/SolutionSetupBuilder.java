package cj.software.genetics.schedule.entity.setup;

public class SolutionSetupBuilder extends SolutionSetup.Builder {

    public SolutionSetupBuilder() {
        super.withNumSolutions(100)
                .withNumWorkers(5)
                .withElitismCount(2)
                .withTournamentSize(5);
    }
}
