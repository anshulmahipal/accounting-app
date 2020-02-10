package controller.screenController;

import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import common.model.entity.User;
import util.Encryptor;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.App;
import util.ResponseHandler;
import util.Validator;

public class LoginScreenController extends BaseScreenController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink signUpButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void initialize()
    {
    }


    @FXML
    private void loginButtonAction()
    {
        Validator validator = new Validator(this);

        System.out.println("Login button pressed!");

        validator.verifyEmail(loginField);
        validator.verifyPassword(passwordField);

        if(super.isErrorState())
            return;

        Request request = new Request(Command.LOGIN,new User(loginField.getText(), Encryptor.encode(passwordField.getText()))); //add validator and hashing
        Response response = Client.getInstance().sendRequest(request);
        ResponseHandler.processResponse(response);
    }

    @FXML
    private void  signUpButtonAction()
    {
        App.getAppHandler().changeScene("/view/RegisterScreen.fxml");
    }

    @FXML
    private void loginChanged()
    {
       if(super.isErrorState() && !super.getErrorMessage().contains("8"))
           super.hideErrorNotification();
    }

    @FXML
    private void passwordChanged()
    {
        if(super.isErrorState() && !super.getErrorMessage().contains("Недопустимый email"))
            super.hideErrorNotification();
    }

}

