package cj.software.genetics.schedule.entity.setupfx;

import javafx.scene.paint.Color;

public class ColorPair {
    private Color foreground;

    private Color background;

    public ColorPair(Color foreground, Color background) {
        this.foreground = foreground;
        this.background = background;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
}
