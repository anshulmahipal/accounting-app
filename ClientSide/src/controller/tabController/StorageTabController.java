package controller.tabController;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import client.Client;
import common.model.BaseEntity;
import common.model.dto.StatsContent;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Response;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.App;
import util.ChoiceBoxManager;
import util.ResponseHandler;


public class StorageTabController extends BaseTabController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<StatsContent, Integer> productIDCol;

    @FXML
    private TableColumn<StatsContent, String> productNameCol;

    @FXML
    private TableColumn<StatsContent, String> productGroupCol;

    @FXML
    private TableColumn<StatsContent, Double> productCostCol;

    @FXML
    private TableColumn<StatsContent, Integer> productAmountCol;

    @FXML
    private Button addToSalesButton, clearFilters;

    @FXML
    private TextField nameFilterField;

    @FXML
    private TextField minAmount, maxAmount;

    @FXML
    private ChoiceBox<String> productGroupChoice;

    //because of filtered data contains BaseEntityt (for binding to it)
    private ObjectProperty<Predicate<BaseEntity>> nameFilter;
    private ObjectProperty<Predicate<BaseEntity>> minAmountFilter;
    private ObjectProperty<Predicate<BaseEntity>> maxAmountFilter;
    private ObjectProperty<Predicate<BaseEntity>> productGroupFilter;


    public StorageTabController()
    {
        updateCommand = Command.GET_STORAGE;

        nameFilter = new SimpleObjectProperty<>();
        minAmountFilter = new SimpleObjectProperty<>();
        maxAmountFilter = new SimpleObjectProperty<>();
        productGroupFilter = new SimpleObjectProperty<>();
    }

    Button getAddToSalesButton()
    {
        return addToSalesButton;
    }

    private void initFilterBinding()
    {
        nameFilter.bind(Bindings.createObjectBinding(() ->
                        entity -> nameFilterField.getText().isEmpty() || ((StatsContent)entity).getProduct().getName().toLowerCase().startsWith(nameFilterField.getText().toLowerCase()),
                nameFilterField.textProperty()));

        minAmountFilter.bind(Bindings.createObjectBinding(() ->
                        entity ->
                        {
                            if(minAmount.getText().matches("\\D+"))
                                return true;
                            return minAmount.getText().isEmpty() || ((StatsContent) entity).getProductAmount() >= Integer.valueOf(minAmount.getText());
                        },
                minAmount.textProperty()));

        maxAmountFilter.bind(Bindings.createObjectBinding(() ->
                        entity ->
                        {
                            if(maxAmount.getText().matches("\\D+") || maxAmount.getText().isEmpty())
                                return true;
                            if(!minAmount.getText().isEmpty() && Integer.valueOf(minAmount.getText())>Integer.valueOf(maxAmount.getText()))
                                return true;
                            return ((StatsContent)entity).getProductAmount()<=Integer.valueOf(maxAmount.getText());
                        },
                maxAmount.textProperty()));

        productGroupFilter.bind(Bindings.createObjectBinding(() ->
                entity -> productGroupChoice.getValue() == null || ((StatsContent)entity).getProduct().getGroup().equals(productGroupChoice.getValue()),
                productGroupChoice.valueProperty()));

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                productGroupFilter.get().and(nameFilter.get()).and(minAmountFilter.get()).and(maxAmountFilter.get()),productGroupFilter,nameFilter,minAmountFilter,maxAmountFilter));

        clearFilters.setOnAction(actionEvent ->
        {
            productGroupChoice.getSelectionModel().clearSelection();
            nameFilterField.clear();
            minAmount.clear();
            maxAmount.clear();
        });
    }

    private void initFilterStyleBinding()
    {
        minAmount.textProperty().addListener((observableValue, s, t1) ->
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            maxAmount.setStyle(null);

            if(t1.matches("\\D+"))
                minAmount.setText(s);
            else if (t1.matches("\\d+") && !maxAmount.getText().isEmpty() && Integer.valueOf(maxAmount.getText())<Integer.valueOf(t1))
            {
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Неверное значение.");
                maxAmount.setStyle("-fx-border-color: red");
            }
        });

        maxAmount.textProperty().addListener((observableValue, s, t1) ->
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            maxAmount.setStyle(null);

            if(t1.matches("\\D+"))
                maxAmount.setText(s);
            else if (t1.matches("\\d+") && !minAmount.getText().isEmpty() && Integer.valueOf(minAmount.getText())>Integer.valueOf(t1))
            {
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Неверное значение.");
                maxAmount.setStyle("-fx-border-color: red");
            }
        });
    }


    @FXML
    void initialize()
    {
        super.initialize();

        productGroupChoice.setItems(FXCollections.observableArrayList(ChoiceBoxManager.getProductGroupList()));

        mainTable.setPlaceholder(new Label("Упс. На складе нет товаров =("));

        productIDCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProduct().getId()).asObject());
        productNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        productGroupCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getProduct().getGroup()));
        productCostCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getProduct().getCost()));
        productAmountCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProductAmount()).asObject());

        initFilterBinding();

        initFilterStyleBinding();
    }

    @Override
    public void sendGetTableRequest()
    {
        Request request = new Request(updateCommand, App.getCurrentUser());
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }
}
