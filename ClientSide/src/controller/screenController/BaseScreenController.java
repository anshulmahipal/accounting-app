package controller.screenController;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class BaseScreenController
{

    @FXML
    private Pane errorPane;

    @FXML
    private Pane approvePane;

    @FXML
    protected Pane root;

    private String errorMessage;

    public boolean isErrorState()
    {
        return errorMessage != null;
    }

    String getErrorMessage()
    {
        return errorMessage;
    }


    public void hideErrorNotification()
    {
        if(errorPane == null)
            return;

        errorMessage = null;

        errorPane.setVisible(false);
    }

    public void showErrorNotification(String text)
    {
        if(errorPane == null)
            return;

        if(errorMessage == null)
            errorMessage = text;

        ((Text)errorPane.getChildren().get(0)).setText(errorMessage);
        errorPane.setVisible(true);
    }

    public void showApproveNotification(String text)
    {
        if(approvePane == null)
            return;

        ((Text)approvePane.getChildren().get(0)).setText(text);
        approvePane.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3)));
        timeline.setOnFinished(actionEvent -> approvePane.setVisible(false));

        timeline.play();
    }

    public void indicateError(Node node) {};

    public void clearErrorIndicators(){};

    public void close()
    {
        hideErrorNotification();
        clearErrorIndicators();

        for (Node node:root.getChildren())
        {
            if(node instanceof TextInputControl)
                ((TextInputControl) node).clear();
        }
    };  //operations to be done before changing root of the scene
}
