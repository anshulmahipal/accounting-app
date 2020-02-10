package util;

import controller.screenController.BaseScreenController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class AppHandler
{
    private Stage stage;
    private Scene scene;
    private BaseScreenController currentSceneController;

    private HashMap<String, Pane> sceneMap;
    private HashMap<String, BaseScreenController> controllerMap;

    private Double stageBoundX, stageBoundY;

    public AppHandler(Stage stage, String sceneLocation)
    {
        this.stage = stage;
        currentSceneController = null;
        scene = null;

        sceneMap = new HashMap<>();
        controllerMap = new HashMap<>();

        changeScene(sceneLocation);
    }

    public void changeScene(String sceneLocation)
    {
        if(currentSceneController !=null)
            currentSceneController.close();

        try
        {
            Pane root = null;

            if(sceneMap.containsKey(sceneLocation))
            {
                root = sceneMap.get(sceneLocation);
                currentSceneController = controllerMap.get(sceneLocation);
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneLocation));
                root = loader.load();
                currentSceneController = loader.getController();
                sceneMap.put(sceneLocation,root);
                controllerMap.put(sceneLocation, currentSceneController);
            }

            if(scene == null)
            {
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);

                stageBoundX = stage.getWidth()-root.getWidth();
                stageBoundY = stage.getHeight()-root.getHeight();
            }
            else
            {
                scene.setRoot(root);
                changeSize(root.getPrefWidth(),root.getPrefHeight());
                stage.centerOnScreen();
            }

            root.requestFocus();
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    public BaseScreenController getCurrentSceneController()
    {
        return currentSceneController;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void changeSize(double width, double height)
    {
        stage.setWidth(width+stageBoundX);
        stage.setHeight(height+stageBoundY);
    }
}
