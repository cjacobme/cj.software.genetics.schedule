package cj.software.genetics.schedule.javafx;

import cj.software.genetics.schedule.entity.setupfx.PriorityFx;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

public class EditPriorityDetailsDialog extends Dialog<PriorityFx> {
    public EditPriorityDetailsDialog(
            ConfigurableApplicationContext context,
            Window owner,
            PriorityFx data) {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        FxControllerAndView<EditPriorityDetailsController, DialogPane> controllerAndView =
                fxWeaver.load(EditPriorityDetailsController.class);
        Optional<DialogPane> optionalDialogPane = controllerAndView.getView();
        if (optionalDialogPane.isPresent()) {
            final EditPriorityDetailsController controller = controllerAndView.getController();
            controller.setData(data);
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            DialogPane dialogPane = optionalDialogPane.get();
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> {
                PriorityFx result;
                ButtonBar.ButtonData buttonData = buttonType.getButtonData();
                if (buttonData.isDefaultButton()) {
                    result = controller.getModifications();
                } else {
                    result = controller.getOriginal();
                }
                return result;
            });
        }
    }
}
