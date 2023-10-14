package cj.software.genetics.schedule.entity.setupfx;

import cj.software.genetics.schedule.entity.setup.Tasks;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TasksFx {

    private final SimpleIntegerProperty duration;

    private final SimpleIntegerProperty count;

    public TasksFx(Tasks source) {
        this.duration = new SimpleIntegerProperty(source.getDurationSeconds());
        this.count = new SimpleIntegerProperty(source.getNumberTasks());
    }

    public void setDuration(int duration) {
        this.duration.setValue(duration);
    }

    public IntegerProperty getDurationProperty() {
        return this.duration;
    }

    public int getDuration() {
        return this.duration.getValue();
    }

    public void setCount(int count) {
        this.count.setValue(count);
    }

    public int getCount() {
        return this.count.getValue();
    }

    public IntegerProperty getCountProperty() {
        return this.count;
    }
}
