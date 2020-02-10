package controller.screenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.Client;
import common.model.entity.User;
import util.Encryptor;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Response;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.util.Duration;
import main.App;
import util.ResponseHandler;
import util.Validator;

public class RegisterScreenController  extends BaseScreenController
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button cancelButton;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField patronymicField;

    @FXML
    private TextField workplaceField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField phonefField;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private PasswordField adminCodeField;


    private ArrayList<Node> errorIndicatedNodes;


    public RegisterScreenController() //called before initialize, when fxml loader perform loading
    {
        errorIndicatedNodes = new ArrayList<>();
    }

    @FXML
    void initialize()
    {
    }

    @Override
    public void indicateError(Node node)
    {
        node.setStyle("-fx-border-color: red; -fx-border-width: 1");
        errorIndicatedNodes.add(node);
    }

    public void clearErrorIndicators()
    {
        for (Node node:errorIndicatedNodes)
        {
            node.setStyle(null);
        }

        errorIndicatedNodes.clear();
    }

    @Override
    public void close()
    {
        super.close();

        if(adminCheckBox.isSelected())
        {
            adminCheckBox.setSelected(false);
            adminChecked();
        }

    }

    @FXML
    private void registerButtonAction()
    {
        Validator validator = new Validator(this);
        boolean isAdmin = false;

        System.out.println("register button pressed");
        super.hideErrorNotification();
        clearErrorIndicators();

        validator.verifyEmail(loginField);

        validator.verifyPassword(passwordField);
        validator.verifyPasswordEquality(passwordField,repeatPasswordField);

        TextInputControl[] components = {surnameField,nameField,patronymicField,cityField};

        for (int i = 0;i<components.length;i++)
            validator.verifyLettersOnly(components[i]);

        validator.verifyWorkplace(workplaceField);
        validator.verifyPhone(phonefField);

        if(adminCheckBox.isSelected())
            isAdmin = validator.verifyAdminCode(adminCodeField);

        if(super.isErrorState())
            return;

        User user = new User(loginField.getText(), Encryptor.encode(passwordField.getText()),isAdmin); //password hashing
        user.setSurname(surnameField.getText());
        user.setName(nameField.getText());
        user.setPatronymic(patronymicField.getText());
        user.setWorkplace(workplaceField.getText());
        user.setCity(cityField.getText());
        user.setPhone(phonefField.getText());

        Request request = new Request(Command.REGISTER,user);
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }

    @FXML
    private void cancelButtonAction()
    {
        App.getAppHandler().changeScene("/view/LoginScreen.fxml");
    }

    @FXML
    private void adminChecked()
    {
        double addSize = 30;

        adminCheckBox.setDisable(true);

        root.requestFocus();

        if(adminCheckBox.isSelected())
        {

            App.getAppHandler().changeSize(root.getWidth(),root.getHeight()+addSize);

            buttonTransition(registerButton,addSize);
            buttonTransition(cancelButton,addSize);

            adminCodeField.setVisible(true);
            adminCodeFieldTransition(0,1);

        }
        else
        {
            adminCodeFieldTransition(1,0);

            buttonTransition(registerButton,-addSize);
            buttonTransition(cancelButton,-addSize);
        }
    }

    private void buttonTransition(Button button, double y)  //move to animation handler class???
    {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5),button);
        transition.setByY(y);

        transition.setOnFinished(actionEvent ->
        {
            if(y<0)
            {
                App.getAppHandler().changeSize(root.getPrefWidth(), root.getPrefHeight());
                adminCheckBox.setDisable(false);
            }

        });
        transition.play();
    }

    private void adminCodeFieldTransition(double from, double to)
    {
        double duration = 0.0;
        if(from<to)
            duration = 1;
        else
            duration = 0.3;

        FadeTransition transition = new FadeTransition(Duration.seconds(duration),adminCodeField);
        transition.setFromValue(from);
        transition.setToValue(to);

        transition.setOnFinished(actionEvent ->
        {
            if (from < to)
                adminCheckBox.setDisable(false);
            else
                adminCodeField.setVisible(false);
        });
        transition.play();
    }



}

