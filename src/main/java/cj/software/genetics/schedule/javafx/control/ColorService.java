package cj.software.genetics.schedule.javafx.control;

import cj.software.genetics.schedule.entity.setupfx.ColorPair;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Service;

@Service
public class ColorService {
    public String normalize(Color source) {
        String result;
        if (source != null) {
            result = source.toString();
            if (result.startsWith("0x")) {
                result = result.substring("0x".length());
            }
            if (!result.startsWith("#")) {
                result = "#" + result;
            }
        } else {
            result = null;
        }
        return result;
    }

    public String constructStyle(ColorPair colorPair) {
        Color background = colorPair.getBackground();
        Color foreground = colorPair.getForeground();
        String backgroundStr = normalize(background);
        String foregroundStr = normalize(foreground);
        String result = String.format("-fx-background-color:%s;-fx-text-fill:%s;", backgroundStr, foregroundStr);
        return result;
    }
}
