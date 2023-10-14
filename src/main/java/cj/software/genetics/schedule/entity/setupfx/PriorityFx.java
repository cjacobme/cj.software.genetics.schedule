package cj.software.genetics.schedule.entity.setupfx;

import cj.software.genetics.schedule.entity.setup.Priority;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PriorityFx {
    private final SimpleIntegerProperty value;
    private final SimpleObjectProperty<ColorPair> colors;

    public PriorityFx(Priority source) {
        this.value = new SimpleIntegerProperty(source.getValue());
        this.colors = new SimpleObjectProperty<>(new ColorPair(source.getForegroundColor(), source.getBackgroundColor()));
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

}
