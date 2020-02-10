package controller.tabController;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import common.model.BaseEntity;
import common.model.dto.Stats;
import common.model.dto.StatsContent;
import common.model.entity.Product;
import common.util.request.Command;
import controller.screenController.HomeScreenController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import main.App;
import util.ChoiceBoxManager;

public class MakeSuppliesTabController extends BaseTabController
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
    private TableColumn<StatsContent, Double> productSummaryCol;

    @FXML
    private TextField totalCostField;

    @FXML
    private ChoiceBox<String> contractorChoiceBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Label contractorTypeLabel;

    private int maxProductAmount;


    private void updateTotalCostField()
    {
        double total = 0;

        for (BaseEntity entity:mainTable.getItems())
        {
            total += ((StatsContent)entity).getProductSummary();
        }

        totalCostField.setText(String.valueOf(total));
    }

    private void enableProductAmountEditing()
    {
        mainTable.setEditable(true);
        productAmountCol.setEditable(true);
        productAmountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()
        {
            @Override
            public Integer fromString(String s)
            {
                try
                {
                    if(s.startsWith("-"))
                        throw new NumberFormatException();
                    return super.fromString(s);
                }
                catch (NumberFormatException exc)
                {
                    return -1;
                }
            }

            @Override
            public String toString(Integer integer)
            {
                return super.toString(integer);
            }
        }));

        productAmountCol.setOnEditCommit(cellEditEvent->
        {
            StatsContent content = cellEditEvent.getRowValue();

            if(cellEditEvent.getNewValue()>maxProductAmount && maxProductAmount!=-1)
            {
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Превышено макимальное количество товара на складе.");
                content.setProductAmount(cellEditEvent.getOldValue());
            }
            else if(cellEditEvent.getNewValue() == -1)
                content.setProductAmount(cellEditEvent.getOldValue());
            else
                content.setProductAmount(cellEditEvent.getNewValue());

            mainTable.refresh();

            updateTotalCostField();
        });
    }

    private void initButtonBindings()
    {
        mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) ->
        {
            if(newSelection!=null)
                deleteProductButton.setDisable(false);
            else
                deleteProductButton.setDisable(true);
        });


        contractorChoiceBox.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observableValue, s, t1) ->
        {
            if(t1!=null && !mainTable.getItems().isEmpty())
                confirmButton.setDisable(false);
            else
                confirmButton.setDisable(true);
        });

        mainTable.getItems().addListener((ListChangeListener<BaseEntity>) change ->
        {
            updateTotalCostField();

            if(mainTable.getItems().isEmpty())
            {
                confirmButton.setDisable(true);
                return;
            }

            if(contractorChoiceBox.getValue()!=null )
                confirmButton.setDisable(false);
        });
    }

    private void loadContractorsList()
    {
        ChoiceBoxManager manager = new ChoiceBoxManager();

        ArrayList<String> contractors = manager.getContractorsOfType(contractorTypeLabel.getText().replace(":",""));

        contractorChoiceBox.setItems(FXCollections.observableArrayList(contractors));
    }

    public void changeContractorLabel()
    {
        contractorTypeLabel.setText("Клиент:");
    }



    @FXML
    void initialize()
    {
        totalCostField.setText("0.0");

        root.sceneProperty().addListener((observableValue, scene, t1) ->
        {
            if(t1!=null)
                loadContractorsList();
        });

        productIDCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProduct().getId()).asObject());
        productNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        productGroupCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getProduct().getGroup()));
        productCostCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getProduct().getCost()));
        productAmountCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getProductAmount()).asObject());
        productSummaryCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getProductSummary()));

        enableProductAmountEditing();

        initButtonBindings();

    }

    private void showDialog(String tabLocation, String title)
    {
        try
        {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(tabLocation));

            Pane dialogRoot = loader.load();
            BaseTabController controller = loader.getController();

            //addressing update requests for table to current modal window
            ((HomeScreenController) App.getAppHandler().getCurrentSceneController()).setCurrentTabController(controller);

            if(controller instanceof ProductsTabController)
            {
                ((ProductsTabController)controller).getAddToSuppliesButton().setVisible(true);

                controller.mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) ->
                {
                    if(newSelection!=null)
                        ((ProductsTabController) controller).getAddToSuppliesButton().setDisable(false);
                    else
                        ((ProductsTabController) controller).getAddToSuppliesButton().setDisable(true);
                });

                ((ProductsTabController) controller).getAddToSuppliesButton().setOnAction(actionEvent ->
                {
                    Product product = (Product) controller.mainTable.getSelectionModel().getSelectedItem();

                    StatsContent content = new StatsContent(product,1,product.getCost());
                    maxProductAmount=-1;

                    mainTable.getItems().add(content);

                    dialog.close();

                });
            }
            else
            {
                ((StorageTabController)controller).getAddToSalesButton().setVisible(true);

                controller.mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldSelection, newSelection) ->
                {
                    if(newSelection!=null)
                        ((StorageTabController)controller).getAddToSalesButton().setDisable(false);
                    else
                        ((StorageTabController)controller).getAddToSalesButton().setDisable(true);
                });

                ((StorageTabController)controller).getAddToSalesButton().setOnAction(actionEvent ->
                {
                    StatsContent content = (StatsContent) controller.mainTable.getSelectionModel().getSelectedItem();
                    maxProductAmount = content.getProductAmount();

                    double saleCost = content.getProduct().getCost();
                    saleCost+=saleCost*(double)App.getCurrentUser().getAdding()/100;
                    saleCost+=saleCost*(double) App.getCurrentUser().getTax()/100;

                    content.getProduct().setCost(saleCost);
                    content.setProductAmount(1);

                    mainTable.getItems().add(content);

                    dialog.close();
                });
            }

            Scene scene = new Scene(dialogRoot);
            dialog.setTitle(title);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(App.getAppHandler().getStage());
            dialog.setScene(scene);  //will call updateRequest of table

            dialog.showAndWait();

        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    @FXML
    private void addProduct()
    {
        if(contractorTypeLabel.getText().equals("Поставщик:"))
            showDialog("/view/ProductsTab.fxml","Товары поставщиков");
        else
            showDialog("/view/StorageTab.fxml","Товары на складе");
    }

    @FXML
    private void deleteProduct()
    {
        mainTable.getItems().remove(mainTable.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void confirmAction()
    {
        ArrayList<StatsContent> contents = new ArrayList<>();

        for (BaseEntity entity:mainTable.getItems())
        {
            contents.add((StatsContent) entity);
        }

        double totalCost = Double.valueOf(totalCostField.getText());
        String contractorName = contractorChoiceBox.getSelectionModel().getSelectedItem();

        Stats stats = new Stats(App.getCurrentUser().getId(),new Timestamp(new Date().getTime()),totalCost,contractorName,contents);

        if(contractorTypeLabel.getText().equals("Поставщик:"))
            sendAddToTableRequest(Command.ADD_TO_SUPPLIES,stats);
        else
            sendAddToTableRequest(Command.ADD_TO_SALES,stats);

        mainTable.getItems().clear();
    }
}

