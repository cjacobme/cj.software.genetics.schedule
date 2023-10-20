package cj.software.genetics.schedule.entity.setupfx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class GeneticAlgorithmFx {
    private final ObjectProperty<SolutionSetupFx> solutionsSetup;

    private final ObservableList<PriorityFx> priorities;

    public GeneticAlgorithmFx(SolutionSetupFx solutionSetupFx, ObservableList<PriorityFx> priorities) {
        this.solutionsSetup = new SimpleObjectProperty<>(solutionSetupFx);
        this.priorities = priorities;
    }

    public SolutionSetupFx getSolutionsSetup() {
        return solutionsSetup.get();
    }

    public ObjectProperty<SolutionSetupFx> solutionsSetupProperty() {
        return solutionsSetup;
    }

    public ObservableList<PriorityFx> getPriorities() {
        return priorities;
    }
}
