package cj.software.genetics.schedule.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("NewProblem.fxml")
public class NewProblemController implements Initializable {

    @FXML
    private Spinner<Integer> spNumWorkers;

    @FXML
    private Spinner<Integer> spNumSlots;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spNumWorkers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 20, 4));
        spNumSlots.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 10000, 100));
    }

    public int getNumWorkers() {
        return spNumWorkers.getValue();
    }

    public int getNumSlots() {
        return spNumSlots.getValue();
    }
}
