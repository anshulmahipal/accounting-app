package main;

import client.Client;
import common.model.entity.User;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Response;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.AppHandler;
import util.ResponseHandler;

public class App extends Application
{
    private static AppHandler appHandler;
    private static User currentUser;
    private static final String adminCode = "11qwe23"; //make editable through properties

    public static AppHandler getAppHandler()
    {
        return appHandler;
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser)
    {
        App.currentUser = currentUser;
    }

    public static String getAdminCode()
    {
        return adminCode;
    }

    @Override
    public void init() throws Exception
    {
        Client.getInstance(); //connecting to server at application start
    }


    @Override
    public void start(Stage primaryStage)
    {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent windowEvent)
            {
                Response response = Client.getInstance().sendRequest(new Request(Command.QUIT));
                ResponseHandler.processResponse(response);
            }
        });

        appHandler = new AppHandler(primaryStage, "/view/LoginScreen.fxml");
    }
}
