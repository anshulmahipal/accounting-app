package controller.tabController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import common.model.BaseEntity;
import common.model.entity.Contractor;
import common.util.request.Command;
import controller.screenController.ContractorDialogController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.App;
import util.ChoiceBoxManager;
import util.Validator;

public class ContractorsTabController extends BaseTabController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Contractor, String> contractorTypeCol;

    @FXML
    private TableColumn<Contractor, String> contractorNameCol;

    @FXML
    private TableColumn<Contractor, String> contractorPhoneCol;

    @FXML
    private TableColumn<Contractor, String> contractorRequisitesCol;

    @FXML
    private TableColumn<Contractor, String> contractorAdressCol;

    @FXML
    private ToggleGroup BGroup;

    @FXML
    private Group adminButtonGroup;

    @FXML
    private Button addContractorButton, removeContractorButton, editContractorButton;


    public ContractorsTabController()
    {
        updateCommand = Command.GET_CONTRACTORS;
    }

    @Override
    public void initAdminComponents()
    {
        adminButtonGroup.setVisible(true);

        mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, entity, t1) ->
        {
            if(t1!=null)
            {
                removeContractorButton.setDisable(false);
                editContractorButton.setDisable(false);
            }
            else
            {
                removeContractorButton.setDisable(true);
                editContractorButton.setDisable(true);
            }
            if(t1!=null && entity!=null)
                App.getAppHandler().getCurrentSceneController().hideErrorNotification();
        });
    }

    @FXML
    void initialize()
    {
        super.initialize();

        contractorTypeCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getType()));
        contractorNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getName()));
        contractorPhoneCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getPhone()));
        contractorRequisitesCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getRequisites()));
        contractorAdressCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getAdress()));

        BGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) ->
        {

            switch (((RadioButton)t1).getText())
            {
                case "Поставщики":
                    filteredData.setPredicate(entity -> ((Contractor) entity).getType().equalsIgnoreCase("Поставщик"));
                    break;
                case "Клиенты":
                    filteredData.setPredicate(entity -> ((Contractor) entity).getType().equalsIgnoreCase("Клиент"));
                    break;
                default:
                    filteredData.setPredicate(entity -> true);
                    break;
            }
        });


    }

    @Override
    public void updateTable(ArrayList<BaseEntity> entities)
    {
        super.updateTable(entities);

        for (BaseEntity entity:mainTable.getItems())
        {
           ChoiceBoxManager.getContractors().put(((Contractor)entity).getName(),((Contractor)entity).getType());
        }
    }

    @FXML
    private void addContractor()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContractorDialog.fxml"));

            Pane root = loader.load();
            showDialog(root);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    @FXML
    private void editContractor()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContractorDialog.fxml"));

            Pane root = loader.load();
            ContractorDialogController controller = loader.getController();

            controller.initParameters((Contractor) mainTable.getSelectionModel().getSelectedItem());

            showDialog(root);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    @FXML
    private void removeContractor()
    {
        Contractor contractor = (Contractor) mainTable.getSelectionModel().getSelectedItem();

        sendRemoveFromTableRequest(contractor);
        sendGetTableRequest();
    }
}

