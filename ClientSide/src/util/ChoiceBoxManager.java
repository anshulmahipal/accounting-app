package util;

import controller.screenController.HomeScreenController;
import controller.tabController.BaseTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import main.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChoiceBoxManager
{
    private static HashMap<String,String> contractors;

    private static ArrayList<String> productGroups;

    public ChoiceBoxManager()
    {
        contractors = new HashMap<>();
    }

    public static HashMap<String, String> getContractors()
    {
        if(contractors == null)
            contractors = new HashMap<>();
        return contractors;
    }

    public ArrayList<String> getContractorsOfType(String contractorType)
    {
        try
        {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContractorsTab.fxml"));

            Pane root = loader.load();
            BaseTabController controller = loader.getController();

            ((HomeScreenController) App.getAppHandler().getCurrentSceneController()).setCurrentTabController(controller);

            Scene scene = new Scene(root);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }


        ArrayList<String> contractorsList = new ArrayList<>();

        if(contractors.containsValue(contractorType))
        {
            for (Map.Entry<String, String> entry : contractors.entrySet())
            {
                if (entry.getValue().equalsIgnoreCase(contractorType))
                    contractorsList.add(entry.getKey());
            }
        }

        if(contractorsList.isEmpty())
            App.getAppHandler().getCurrentSceneController().showErrorNotification("Список контрагентов пуст. Обратитесь к администратору.");

        return contractorsList;
    }

    public static ArrayList<String> getProductGroupList()
    {
        if(productGroups == null)
        {
            productGroups = new ArrayList<>();
            productGroups.add("Сетевые фильтры");
            productGroups.add("Удлинители");
            productGroups.add("Аккумуляторы");
            productGroups.add("Розетки");
            productGroups.add("Кабеля");
        }

        return productGroups;
    }

}
