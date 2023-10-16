package cj.software.genetics.schedule.entity.setupfx;

import cj.software.genetics.schedule.entity.setup.Priority;
import cj.software.genetics.schedule.entity.setup.Tasks;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PriorityFx {
    private final SimpleIntegerProperty value;

    private final SimpleObjectProperty<ColorPair> colors;

    private final ObservableList<TasksFx> tasksList;

    public PriorityFx(PriorityFx source) {
        this.value = new SimpleIntegerProperty(source.getValue());
        ColorPair sourceColors = source.getColors();
        ColorPair colorPair = new ColorPair(sourceColors.getForeground(), sourceColors.getBackground());
        colors = new SimpleObjectProperty<>(colorPair);
        tasksList = FXCollections.observableArrayList();
        List<TasksFx> list = source.tasksList.stream().toList();
        tasksList.addAll(list);
    }

    public PriorityFx(int value, ColorPair colorPair, ObservableList<TasksFx> tasksList) {
        this.value = new SimpleIntegerProperty(value);
        this.colors = new SimpleObjectProperty<>(colorPair);
        this.tasksList = tasksList;
    }

    public PriorityFx(Priority source) {
        this.value = new SimpleIntegerProperty(source.getValue());
        this.colors = new SimpleObjectProperty<>(new ColorPair(source.getForegroundColor(), source.getBackgroundColor()));
        this.tasksList = FXCollections.observableArrayList();
        for (Tasks tasks : source.getTasks()) {
            TasksFx converted = new TasksFx(tasks);
            this.tasksList.add(converted);
        }
    }

    public Integer getValue() {
        return value.getValue();
    }

    public ColorPair getColors() {
        return colors.getValue();
    }

    public void setColors(ColorPair colors) {
        this.colors.set(colors);
    }

    public ObservableList<TasksFx> getTasksList() {
        return tasksList;
    }
}
