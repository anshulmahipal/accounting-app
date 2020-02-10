package controller.tabController;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import client.Client;
import common.model.BaseEntity;
import common.model.dto.Stats;
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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.App;
import util.ChoiceBoxManager;
import util.ResponseHandler;

public class SuppliesTabController extends BaseTabController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Stats, Integer> supplyIDCol;

    @FXML
    private TableColumn<Stats, String> contractorNameCol;

    @FXML
    private TableColumn<Stats, Timestamp> supplyDateCol;

    @FXML
    private TableColumn<Stats, Double> supplyTotalCol;

    @FXML
    private TableView<StatsContent> productsTable;

    @FXML
    private TableColumn<StatsContent, Integer> productIDCol;

    @FXML
    private TableColumn<StatsContent, String> productNameCol;

    @FXML
    private TableColumn<StatsContent, Integer> productAmountCol;

    @FXML
    private TableColumn<StatsContent, Double> productCostCol;

    @FXML
    private TableColumn<StatsContent, Double> productSummaryCol;

    @FXML
    private Label contractorLabel;

    @FXML
    private DatePicker minDate, maxDate;

    @FXML
    private Button clearFilters;

    @FXML
    private ChoiceBox<String> contractorChoiceBox;

    private ObjectProperty<Predicate<BaseEntity>> minDateFilter;
    private ObjectProperty<Predicate<BaseEntity>> maxDateFilter;
    private ObjectProperty<Predicate<BaseEntity>> contractorFilter;

    public SuppliesTabController()
    {
        updateCommand = Command.GET_SUPPLIES;
        minDateFilter = new SimpleObjectProperty<>();
        maxDateFilter = new SimpleObjectProperty<>();
        contractorFilter = new SimpleObjectProperty<>();
    }

    public void changeColNames()
    {
        supplyIDCol.setText("Код продажи");
        supplyDateCol.setText("Дата продажи");
        contractorNameCol.setText("Клиент");
        contractorLabel.setText("Клиент:");
    }

    public void changeUpdateCommand()
    {
        updateCommand = Command.GET_SALES;
    }

    private void setDateFormat()
    {
        supplyDateCol.setCellFactory(column->
                new TableCell<>()
                {
                    @Override
                    protected void updateItem(Timestamp timestamp, boolean b)
                    {
                        super.updateItem(timestamp, b);

                        setText(null);
                        setGraphic(null);

                        if(!b)
                        {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                            setText(formatter.format(timestamp));
                        }
                    }
                });
    }

    private void loadContractorList()
    {
        ChoiceBoxManager manager = new ChoiceBoxManager();

        ArrayList<String> contractors = manager.getContractorsOfType(contractorLabel.getText().replace(":",""));

        contractorChoiceBox.setItems(FXCollections.observableArrayList(contractors));
    }

    private void initFilterBinding()
    {
        contractorFilter.bind(Bindings.createObjectBinding(() ->
                        entity-> contractorChoiceBox.getSelectionModel().getSelectedIndex()<0 || ((Stats)entity).getContractorName().equalsIgnoreCase(contractorChoiceBox.getSelectionModel().getSelectedItem()),
                contractorChoiceBox.valueProperty()));


        minDateFilter.bind(Bindings.createObjectBinding(() ->
                entity->
                {
                    LocalDate filterDate = minDate.getValue();

                    if(filterDate == null)
                        return true;

                    String strDate = new SimpleDateFormat("yyyy-MM-dd").format(((Stats)entity).getStatsDate());
                    LocalDate entityDate = LocalDate.parse(strDate);

                    return entityDate.compareTo(filterDate)>=0;
                }, minDate.valueProperty()));

        maxDateFilter.bind(Bindings.createObjectBinding(() ->
                entity->
                {
                    LocalDate filterDate = maxDate.getValue();

                    if(filterDate == null || minDate.getValue()!=null && minDate.getValue().isAfter(filterDate))
                        return true;

                    String strDate = new SimpleDateFormat("yyyy-MM-dd").format(((Stats)entity).getStatsDate());
                    LocalDate entityDate = LocalDate.parse(strDate);

                    return entityDate.compareTo(filterDate)<=0;
                }, maxDate.valueProperty()));

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                contractorFilter.get().and(minDateFilter.get()).and(maxDateFilter.get()),contractorFilter,minDateFilter,maxDateFilter));

        clearFilters.setOnAction(actionEvent ->
        {
            minDate.setValue(null);
            maxDate.setValue(null);
            contractorChoiceBox.getSelectionModel().clearSelection();
        });
    }

    private void initFilterStyleBinding()
    {
        minDate.setOnAction(actionEvent ->
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            maxDate.setStyle(null);

            if(minDate.getValue() == null)
                return;
            if(maxDate.getValue()!=null && maxDate.getValue().isBefore(minDate.getValue()))
            {
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Неверное значение.");
                maxDate.setStyle("-fx-border-color: red");
            }
        });

        maxDate.setOnAction(actionEvent ->
        {
            App.getAppHandler().getCurrentSceneController().hideErrorNotification();
            maxDate.setStyle(null);

            if(maxDate.getValue() == null)
                return;
            if(minDate.getValue()!=null && minDate.getValue().isAfter(maxDate.getValue()))
            {
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Неверное значение.");
                maxDate.setStyle("-fx-border-color: red");
            }
        });
    }


    @FXML
    void initialize()//occurs when FXMLLoader.load() performed, so controller variable set after that
    {
        super.initialize();

        root.sceneProperty().addListener((observableValue, scene, t1) ->
        {
            if(t1!=null)
                loadContractorList();
        });

        supplyIDCol.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getStatsID()).asObject()); //type safe way, instead of propertyValueFactory
        contractorNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getContractorName()));
        supplyDateCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getStatsDate()));
        supplyTotalCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getStatsTotal()));

        productIDCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProduct().getId()).asObject());
        productNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        productAmountCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProductAmount()).asObject());
        productCostCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getProduct().getCost()));
        productSummaryCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getProductSummary()));

        //check what is it observable & etc.

        mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelectionEntity, newSelectionEntity) ->
        {
            if(newSelectionEntity == null)
                return;
            ObservableList<StatsContent> contents = FXCollections.observableArrayList(((Stats)newSelectionEntity).getContents());
            productsTable.setItems(contents);
        });

        setDateFormat();

        initFilterBinding();

        initFilterStyleBinding();
    }

    @Override
    public void sendGetTableRequest()
    {
        Request request = new Request(updateCommand,App.getCurrentUser());
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }
}
