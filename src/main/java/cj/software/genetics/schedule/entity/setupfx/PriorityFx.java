package cj.software.genetics.schedule.entity.setupfx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

public class PriorityFx {
    private final IntegerProperty value;

    private final IntegerProperty numSlots;

    private final ObjectProperty<ColorPair> colors;

    private final ObservableList<TasksFx> tasksList;

    public PriorityFx(int value, int numSlots, ColorPair colorPair, ObservableList<TasksFx> tasksList) {
        this.value = new SimpleIntegerProperty(value);
        this.numSlots = new SimpleIntegerProperty(numSlots);
        this.colors = new SimpleObjectProperty<>(colorPair);
        this.tasksList = tasksList;
    }

    public Integer getValue() {
        return value.getValue();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public ColorPair getColors() {
        return colors.getValue();
    }

    public void setColors(ColorPair colors) {
        this.colors.set(colors);
    }

    public ObjectProperty<ColorPair> colorsProperty() {
        return this.colors;
    }

    public Integer getNumSlots() {
        return numSlots.getValue();
    }

    public void setNumSlots(Integer numSlots) {
        this.numSlots.setValue(numSlots);
    }

    public IntegerProperty numSlotsProperty() {
        return numSlots;
    }

    public ObservableList<TasksFx> getTasksList() {
        return tasksList;
    }
}
