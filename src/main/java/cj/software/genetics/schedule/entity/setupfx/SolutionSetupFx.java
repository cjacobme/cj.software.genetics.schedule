package cj.software.genetics.schedule.entity.setupfx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SolutionSetupFx {

    private final IntegerProperty numSolutions;

    private final IntegerProperty numWorkers;

    private final IntegerProperty elitismCount;

    private final IntegerProperty tournamentSize;

    public SolutionSetupFx(int numSolutions, int numWorkers, int elitismCount, int tournamentSize) {
        this.numSolutions = new SimpleIntegerProperty(numSolutions);
        this.numWorkers = new SimpleIntegerProperty(numWorkers);
        this.elitismCount = new SimpleIntegerProperty(elitismCount);
        this.tournamentSize = new SimpleIntegerProperty(tournamentSize);
    }

    public SolutionSetupFx(SolutionSetupFx source) {
        this.numSolutions = new SimpleIntegerProperty(source.getNumSolutions());
        this.numWorkers = new SimpleIntegerProperty(source.getNumWorkers());
        this.elitismCount = new SimpleIntegerProperty(source.getElitismCount());
        this.tournamentSize = new SimpleIntegerProperty(source.getTournamentSize());
    }

    public int getNumSolutions() {
        return numSolutions.get();
    }

    public IntegerProperty numSolutionsProperty() {
        return numSolutions;
    }

    public void setNumSolutions(int numSolutions) {
        this.numSolutions.set(numSolutions);
    }

    public int getNumWorkers() {
        return numWorkers.get();
    }

    public IntegerProperty numWorkersProperty() {
        return numWorkers;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkers.set(numWorkers);
    }

    public int getElitismCount() {
        return elitismCount.get();
    }

    public IntegerProperty elitismCountProperty() {
        return elitismCount;
    }

    public void setElitismCount(int elitismCount) {
        this.elitismCount.set(elitismCount);
    }

    public int getTournamentSize() {
        return tournamentSize.get();
    }

    public IntegerProperty tournamentSizeProperty() {
        return tournamentSize;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize.set(tournamentSize);
    }
}
