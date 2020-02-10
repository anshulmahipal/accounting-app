package controller.tabController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import common.model.BaseEntity;
import common.model.entity.Product;
import common.util.request.Command;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.util.converter.DoubleStringConverter;
import main.App;
import util.ChoiceBoxManager;


public class ProductsTabController extends BaseTabController
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, String> productGroupCol;

    @FXML
    private TableColumn<Product, Double> productCostCol;

    @FXML
    private Button addToSuppliesButton;

    @FXML
    private TextField nameFilterField;

    @FXML
    private Group adminButtonGroup;

    @FXML
    private Button addProductButton, removeProductButton, clearFilters;

    @FXML
    private ChoiceBox<String> productGroupChoice;

    private ObjectProperty<Predicate<BaseEntity>> nameFilter;
    private ObjectProperty<Predicate<BaseEntity>> productGroupFilter;


    public ProductsTabController()
    {
        updateCommand = Command.GET_PRODUCTS;
        nameFilter = new SimpleObjectProperty<>();
        productGroupFilter = new SimpleObjectProperty<>();
    }

    Button getAddToSuppliesButton()
    {
        return addToSuppliesButton;
    }

    @Override
    public void initAdminComponents()
    {
        adminButtonGroup.setVisible(true);

        mainTable.setEditable(true);
        productCostCol.setEditable(true);

        productCostCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()
        {
            @Override
            public Double fromString(String s)
            {
                try
                {
                    if(s.startsWith("-"))
                        throw new NumberFormatException();
                    return super.fromString(s);
                }
                catch (NumberFormatException exc)
                {
                    return -1.0;
                }
            }

            @Override
            public String toString(Double aDouble)
            {
                return super.toString(aDouble);
            }
        }));

        productCostCol.setOnEditCommit(cellEditEvent ->
        {
            Product product = cellEditEvent.getRowValue();

            if(cellEditEvent.getNewValue().equals(-1.0))
                product.setCost(cellEditEvent.getOldValue());
            else
            {
                product.setCost(cellEditEvent.getNewValue());
                senUpdateToTableRequest(product);
            }

            mainTable.refresh();
        });

        mainTable.getSelectionModel().selectedItemProperty().addListener((observableValue, baseEntity, t1) ->
        {
            if(t1!=null)
                removeProductButton.setDisable(false);
            else
                removeProductButton.setDisable(true);
            if(t1!= null && baseEntity!=null)
                App.getAppHandler().getCurrentSceneController().hideErrorNotification();
        });
    }

    private void initFilterBindings()
    {
        nameFilter.bind(Bindings.createObjectBinding(() ->
                        entity -> nameFilterField.getText().isEmpty() || ((Product)entity).getName().toLowerCase().startsWith(nameFilterField.getText().toLowerCase()),
                nameFilterField.textProperty()));

        productGroupFilter.bind(Bindings.createObjectBinding(() ->
                        entity -> productGroupChoice.getValue() == null || ((Product)entity).getGroup().equals(productGroupChoice.getValue()),
                productGroupChoice.valueProperty()));

        filteredData.predicateProperty().bind(Bindings.createObjectBinding(() ->
                productGroupFilter.get().and(nameFilter.get()), productGroupFilter, nameFilter));

        clearFilters.setOnAction(actionEvent ->
        {
            productGroupChoice.getSelectionModel().clearSelection();
            nameFilterField.clear();
        });
    }


    @FXML
    void initialize()
    {
        super.initialize();

        productGroupChoice.setItems(FXCollections.observableArrayList(ChoiceBoxManager.getProductGroupList()));

        productIDCol.setCellValueFactory(cellData->new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        productNameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getName()));
        productGroupCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getGroup()));
        productCostCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getCost()));

        initFilterBindings();
    }

    @FXML
    private void addProduct()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductDialog.fxml"));

            Pane root = loader.load();
            showDialog(root);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    };

    @FXML
    private void removeProduct()
    {
        Product product = (Product) mainTable.getSelectionModel().getSelectedItem();

        sendRemoveFromTableRequest(product);

        sendGetTableRequest();
    };
}
