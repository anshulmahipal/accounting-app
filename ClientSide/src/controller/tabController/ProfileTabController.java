package controller.tabController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.Client;
import common.model.entity.User;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import util.Encryptor;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Notification;
import common.util.response.Response;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import main.App;
import util.ResponseHandler;
import util.Validator;

public class ProfileTabController extends BaseTabController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Group settingsGroup;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField repeatNewPassword;

    @FXML
    private PasswordField currentPassword;

    @FXML
    private TextField adding;

    @FXML
    private TextField tax;

    @FXML
    private Group generalGroup, deleteGroup;

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
    private Button editGeneralButton, editSettingsButton, cancelButton;

    @FXML
    private Button generalButton;

    @FXML
    private Button settingsButton;

    private void revertGeneral()
    {
        surnameField.setText(App.getCurrentUser().getSurname());
        nameField.setText(App.getCurrentUser().getName());
        patronymicField.setText(App.getCurrentUser().getPatronymic());
        workplaceField.setText(App.getCurrentUser().getWorkplace());
        cityField.setText(App.getCurrentUser().getCity());
        phonefField.setText(App.getCurrentUser().getPhone());
    }

    private void revertSettings()
    {
        loginField.setText(App.getCurrentUser().getEmail());
        adding.setText(String.valueOf(App.getCurrentUser().getAdding()));
        tax.setText(String.valueOf(App.getCurrentUser().getTax()));

        currentPassword.clear();
        newPassword.clear();
        repeatNewPassword.clear();
    }

    @FXML
    void generalAction()
    {
        cancelAction();

        settingsGroup.setVisible(false);
        deleteGroup.setVisible(false);

        generalGroup.setVisible(true);

        editSettingsButton.setVisible(false);
        editGeneralButton.setVisible(true);
    }

    @FXML
    void settingsAction()
    {
        cancelAction();

        generalGroup.setVisible(false);
        settingsGroup.setVisible(true);
        deleteGroup.setVisible(true);

        editGeneralButton.setVisible(false);
        editSettingsButton.setVisible(true);
    }

    @FXML
    void initialize()
    {

        revertSettings();

        revertGeneral();

        root.sceneProperty().addListener((observableValue, scene, t1) ->
        {
            if(t1!=null)
                generalAction();
        });
    }

    @FXML
    private void editGeneralAction()
    {
        if(editGeneralButton.getText().equals("Изменить"))
        {
            editGeneralButton.setText("Принять");
            cancelButton.setVisible(true);

            generalGroup.setDisable(false);
        }
        else
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            App.getAppHandler().getCurrentSceneController().clearErrorIndicators();

            Validator validator = new Validator(App.getAppHandler().getCurrentSceneController());

            TextInputControl[] components = {surnameField,nameField,patronymicField,cityField};

            for (int i = 0;i<components.length;i++)
                validator.verifyLettersOnly(components[i]);

            validator.verifyWorkplace(workplaceField);
            validator.verifyPhone(phonefField);

            if(App.getAppHandler().getCurrentSceneController().isErrorState())
                return;

            User user = App.getCurrentUser();

            user.setSurname(surnameField.getText());
            user.setName(nameField.getText());
            user.setPatronymic(patronymicField.getText());
            user.setCity(cityField.getText());
            user.setWorkplace(workplaceField.getText());
            user.setPhone(phonefField.getText());

            Request request = new Request(Command.UPDATE,user);
            Response response = Client.getInstance().sendRequest(request);

            ResponseHandler.processResponse(response);

            cancelAction();
        }
    }

    @FXML
    private void editSettingsAction()
    {
        if(editSettingsButton.getText().equals("Изменить"))
        {
            editSettingsButton.setText("Принять");
            cancelButton.setVisible(true);

            settingsGroup.setDisable(false);
        }
        else
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            App.getAppHandler().getCurrentSceneController().clearErrorIndicators();

            Validator validator = new Validator(App.getAppHandler().getCurrentSceneController());

            validator.verifyEmail(loginField);
            validator.verifyPasswordChange(currentPassword,newPassword,repeatNewPassword);
            validator.verifyPercents(adding);
            validator.verifyPercents(tax);

            if(App.getAppHandler().getCurrentSceneController().isErrorState())
                return;

            User tempUser = new User(App.getCurrentUser());

            tempUser.setEmail(loginField.getText());
            tempUser.setAdding(Integer.valueOf(adding.getText()));
            tempUser.setTax(Integer.valueOf(tax.getText()));
            if(newPassword.getText().isEmpty())
                tempUser.setPassword(App.getCurrentUser().getPassword());
            else
                tempUser.setPassword(Encryptor.encode(newPassword.getText()));

            Request request = new Request(Command.UPDATE,tempUser);
            Response response = Client.getInstance().sendRequest(request);

            ResponseHandler.processResponse(response);

            if(response.getNotification() == Notification.UPDATE_SUCCESS)
                App.setCurrentUser(tempUser);
            else
            {
                App.getAppHandler().getCurrentSceneController().indicateError(loginField);
                return;
            }

            cancelAction();
        }
    }

    @FXML
    private void cancelAction()
    {
        App.getAppHandler().getCurrentSceneController().hideErrorNotification();
        App.getAppHandler().getCurrentSceneController().clearErrorIndicators();

        if(generalGroup.isVisible())
        {
            revertGeneral();

            generalGroup.setDisable(true);
            editGeneralButton.setText("Изменить");

            cancelButton.setVisible(false);

        }
        else
        {
            revertSettings();

            settingsGroup.setDisable(true);
            editSettingsButton.setText("Изменить");

            cancelButton.setVisible(false);
        }
    }

    @FXML
    private void logoutAction()
    {
        App.getAppHandler().changeScene("/view/LoginScreen.fxml");
    }

    @FXML
    private void deleteAccountAction()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Вы уверены?",ButtonType.YES,ButtonType.NO);
        alert.setTitle("Удалить аккаунт");

        Optional<ButtonType> choice = alert.showAndWait();


        if(choice.isEmpty() || choice.get() == ButtonType.NO)
            return;

        Request request = new Request(Command.REMOVE,App.getCurrentUser());
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);

        if(response.getNotification() == Notification.REMOVE_SUCCESS)
            logoutAction();
    }

    @FXML
    private void dropStatsAction()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Вы уверены?",ButtonType.YES,ButtonType.NO);
        alert.setTitle("Сбросить статистику");

        Optional<ButtonType> choice = alert.showAndWait();


        if(choice.isEmpty() || choice.get() == ButtonType.NO)
            return;

        Request request = new Request(Command.REMOVE_STATS,App.getCurrentUser());
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }
}
