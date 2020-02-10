package controller.screenController;


import controller.tabController.BaseTabController;
import controller.tabController.MakeSuppliesTabController;
import controller.tabController.SuppliesTabController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.App;

import java.io.IOException;
import java.util.ArrayList;

public class HomeScreenController extends BaseScreenController
{
    @FXML
    private MenuBar menuBar;

    @FXML
    private Button storageTab;

    @FXML
    private Button makeSupplyTab;

    @FXML
    private Button makeSaleTab;

    @FXML
    private MenuItem supplyStatsTab;

    @FXML
    private MenuItem saleStatsTab;

    @FXML
    private Button contractorsTab;

    @FXML
    private Button productsTab;

    @FXML
    private Button profileTab;

    private BaseTabController currentTabController;

    private ArrayList<Node> errorIndicatedNodes;

    public HomeScreenController()
    {
        errorIndicatedNodes = new ArrayList<>();
    }

    public BaseTabController getCurrentTabController()
    {
        return currentTabController;
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

    public void setCurrentTabController(BaseTabController controller)
    {
        currentTabController = controller;
    }

    private void changeTab(String tabLocation)
    {
        changeTab(null,tabLocation);
    }

    private void changeTab(Object invoker, String tabLocation)
    {
        hideErrorNotification();
        clearErrorIndicators();

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(tabLocation));
            Pane tab = loader.load();//always loads new instance

            currentTabController = loader.getController();

            if(invoker == saleStatsTab)
            {
                ((SuppliesTabController)currentTabController).changeColNames();
                ((SuppliesTabController) currentTabController).changeUpdateCommand();
            }
            else if(invoker == makeSaleTab)
            {
                ((MakeSuppliesTabController)currentTabController).changeContractorLabel();
            }

            if(App.getCurrentUser().isAdmin())
                currentTabController.initAdminComponents();

            ((BorderPane)root).setCenter(tab);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }




    @FXML
    void initialize() //occurs when FXMLLoader.load() performed
    {
        changeTab("/view/ProfileTab.fxml");

        storageTab.setOnAction(actionEvent -> changeTab("/view/StorageTab.fxml"));
        makeSupplyTab.setOnAction(actionEvent -> changeTab("/view/MakeSupplyTab.fxml"));
        makeSaleTab.setOnAction(actionEvent -> changeTab(makeSaleTab,"/view/MakeSupplyTab.fxml"));
        supplyStatsTab.setOnAction(actionEvent -> changeTab("/view/SuppliesTab.fxml"));
        saleStatsTab.setOnAction(actionEvent -> changeTab(saleStatsTab,"/view/SuppliesTab.fxml"));
        contractorsTab.setOnAction(actionEvent -> changeTab("/view/ContractorsTab.fxml"));
        productsTab.setOnAction(actionEvent -> changeTab("/view/ProductsTab.fxml"));
        profileTab.setOnAction(actionEvent -> changeTab("/view/ProfileTab.fxml"));
        //profileTab action
    }

}


