package cj.software.genetics.schedule.entity.setupfx;

import cj.software.genetics.schedule.entity.setup.Priority;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class PriorityFx {
    private final SimpleIntegerProperty value;
    private final SimpleObjectProperty<Color> foreground;
    private final SimpleObjectProperty<Color> background;

    public PriorityFx(Priority source) {
        this.value = new SimpleIntegerProperty(source.getValue());
        this.foreground = new SimpleObjectProperty<>(source.getForegroundColor());
        this.background = new SimpleObjectProperty<>(source.getBackgroundColor());
    }

    public Integer getValue() {
        return value.getValue();
    }

    public void setForeground(Color foreground) {
        this.foreground.setValue(foreground);
    }

    public Color getForeground() {
        return foreground.getValue();
    }

    public void setBackground(Color background) {
        this.background.setValue(background);
    }

    public Color getBackground() {
        return background.getValue();
    }
}
