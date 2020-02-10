package controller.screenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.model.entity.Contractor;
import common.util.request.Command;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.App;
import util.Validator;

public class ContractorDialogController extends BaseScreenController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> contractorTypeChoice;

    @FXML
    private TextField contractorNameField;

    @FXML
    private TextField contractorPhoneField;

    @FXML
    private TextField contractorRequisitesField;

    @FXML
    private TextField contractorAddressField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private int contractorID;

    private ArrayList<Node> errorIndicatedNodes;

    public ContractorDialogController()
    {
        errorIndicatedNodes = new ArrayList<>();
        contractorID = -1;
    }

    @Override
    public void indicateError(Node node)
    {
        node.setStyle("-fx-border-color: red");
        errorIndicatedNodes.add(node);

    }

    @Override
    public void clearErrorIndicators()
    {
        for(Node node:errorIndicatedNodes)
        {
            node.setStyle(null);
        }

        errorIndicatedNodes.clear();
    }

    public void initParameters(Contractor contractor)
    {
        if(contractor == null)
        {
            contractorID = -1;
        }
        else
        {
            contractorID = contractor.getId();

            contractorTypeChoice.getItems().add(contractor.getType());
            contractorTypeChoice.getSelectionModel().select(0);
            contractorTypeChoice.setDisable(true);

            contractorNameField.setText(contractor.getName());
            contractorPhoneField.setText(contractor.getPhone());
            contractorRequisitesField.setText(contractor.getRequisites());
            contractorAddressField.setText(contractor.getAdress());
        }
    }

    public void initParameters()
    {
        initParameters(null);
    }


    @FXML
    void initialize()
    {
        contractorTypeChoice.getItems().add("Поставщик");
        contractorTypeChoice.getItems().add("Клиент");

        contractorTypeChoice.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) ->
        {
            if(t1!=null)
                confirmButton.setDisable(false);
            else
                confirmButton.setDisable(true);
        });
    }

    @FXML
    private void confirmAction()
    {
        hideErrorNotification();
        clearErrorIndicators();

        Validator validator = new Validator(this);

        validator.verifyContractorName(contractorNameField);
        validator.verifyContractorPhone(contractorPhoneField);
        validator.verifyRequisites(contractorRequisitesField);
        validator.verifyAddress(contractorAddressField);

        if(super.isErrorState())
            return;

        contractorNameField.getText();

        Contractor contractor = new Contractor(contractorNameField.getText(),contractorAddressField.getText(),
                contractorPhoneField.getText(),contractorRequisitesField.getText(),contractorTypeChoice.getValue());

        if(contractorID!=-1)
        {
            contractor.setId(contractorID);
            ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().senUpdateToTableRequest(contractor);
        }
        else
            ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().sendAddToTableRequest(Command.ADD,contractor);

        ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().sendGetTableRequest();

        cancelAction();
    }

    @FXML
    private void cancelAction()
    {
        root.getScene().getWindow().hide();
    }
}
