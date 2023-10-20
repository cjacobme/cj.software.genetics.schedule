package cj.software.genetics.schedule.entity.setupfx;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.Tasks;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PriorityFx {
    private final IntegerProperty value;

    private final IntegerProperty numSlots;

    private final ObjectProperty<ColorPair> colors;

    private final ObservableList<TasksFx> tasksList;

    public PriorityFx(PriorityFx source) {
        this.value = new ReadOnlyIntegerWrapper(source.getValue());
        this.numSlots = new SimpleIntegerProperty(source.getNumSlots());
        ColorPair sourceColors = source.getColors();
        ColorPair colorPair = new ColorPair(sourceColors.getForeground(), sourceColors.getBackground());
        colors = new SimpleObjectProperty<>(colorPair);
        tasksList = FXCollections.observableArrayList();
        List<TasksFx> list = source.tasksList.stream().toList();
        tasksList.addAll(list);
    }

    public PriorityFx(int value, int numSlots, ColorPair colorPair, ObservableList<TasksFx> tasksList) {
        this.value = new SimpleIntegerProperty(value);
        this.numSlots = new SimpleIntegerProperty(numSlots);
        this.colors = new SimpleObjectProperty<>(colorPair);
        this.tasksList = tasksList;
    }

    public PriorityFx(Priority source) {
        this.value = new SimpleIntegerProperty(source.getValue());
        this.numSlots = new SimpleIntegerProperty(source.getNumSlots());
        this.colors = new SimpleObjectProperty<>(new ColorPair(source.getForeground(), source.getBackground()));
        this.tasksList = FXCollections.observableArrayList();
        for (Tasks tasks : source.getTasks()) {
            TasksFx converted = new TasksFx(tasks);
            this.tasksList.add(converted);
        }
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
