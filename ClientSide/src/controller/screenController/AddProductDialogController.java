package controller.screenController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.model.entity.Product;
import common.util.request.Command;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.App;
import util.ChoiceBoxManager;
import util.Validator;

public class AddProductDialogController extends BaseScreenController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField productNameField;

    @FXML
    private ChoiceBox<String> productGroupChoice;

    @FXML
    private TextField productCostField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private ArrayList<Node> errorIndicatedNodes;

    public AddProductDialogController()
    {
        errorIndicatedNodes = new ArrayList<>();
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

    @FXML
    void initialize()
    {
        root.requestFocus();

        productGroupChoice.setItems(FXCollections.observableArrayList(ChoiceBoxManager.getProductGroupList()));

        productGroupChoice.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) ->
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

        validator.verifyProductName(productNameField);
        validator.verifyDouble(productCostField);

        if(super.isErrorState())
            return;

        Product product = new Product(productNameField.getText(),Double.valueOf(productCostField.getText()),productGroupChoice.getValue());

        ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().sendAddToTableRequest(Command.ADD,product);
        ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().sendGetTableRequest();

        cancelAction();

    }

    @FXML
    private void cancelAction()
    {
        root.getScene().getWindow().hide();
    }
}

