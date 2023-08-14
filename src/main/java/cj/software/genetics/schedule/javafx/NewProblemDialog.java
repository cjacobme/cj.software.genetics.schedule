package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.ProblemSetup;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class NewProblemDialog extends Dialog<ProblemSetup> {
    public NewProblemDialog(ConfigurableApplicationContext applicationContext, Window owner) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        FxControllerAndView<NewProblemController, DialogPane> controllerAndView =
                fxWeaver.load(NewProblemController.class);
        Optional<DialogPane> optDialog = controllerAndView.getView();
        if (optDialog.isPresent()) {
            final NewProblemController newProblemController = controllerAndView.getController();
            DialogPane dialogPane = optDialog.get();
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                ProblemSetup result;
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                if (buttonData.equals(ButtonBar.ButtonData.OK_DONE)) {
                    int numSolutinos = newProblemController.getNumSolutions();
                    int numWorkers = newProblemController.getNumWorkers();
                    int numSlots = newProblemController.getNumSlots();
                    result = ProblemSetup.builder()
                            .withNumSolutions(numSolutinos)
                            .withNumWorkers(numWorkers)
                            .withNumSlots(numSlots)
                            .withNumTasks10(20) // TODO get value from UI
                            .withNumTasks20(8)
                            .withNumTasks50(8)
                            .withNumTasks100(3)
                            .build();
                } else {
                    result = null;
                }
                return result;
            });
        }
    }
}