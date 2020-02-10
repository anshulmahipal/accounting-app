package controller.tabController;

import client.Client;
import common.model.BaseEntity;
import common.util.request.Command;
import common.util.request.Request;
import common.util.response.Response;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.App;
import util.ResponseHandler;

import java.util.ArrayList;

public class BaseTabController
{
    @FXML
    protected TableView<BaseEntity> mainTable;

    ObservableList<BaseEntity> tableData;
    FilteredList<BaseEntity> filteredData;

    @FXML
    Pane root;

    Command updateCommand = null;

    BaseTabController()
    {
        tableData = FXCollections.observableArrayList();

        //connecting filteredList to tableData
        filteredData = new FilteredList<>(tableData,predicate->true);
    }

    @FXML
    void initialize()
    {
        root.sceneProperty().addListener(new ChangeListener<Scene>()
        {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene t1)
            {
                if(t1 !=null)
                    sendGetTableRequest();  //add title to stage depending on different scenes
            }
        });
    }

    public void updateTable(ArrayList<BaseEntity> entities)
    {
        tableData.setAll(entities);

        //tableData updated -> filteredList also
        mainTable.setItems(filteredData);
    }

    public void sendGetTableRequest()
    {
        if(updateCommand == null)
            return;

        Request request = new Request(updateCommand);
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    };

    public void sendAddToTableRequest(Command command, BaseEntity entity)
    {
        if (entity == null || command == null)
            return;

        Request request = new Request(command,entity);
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }

    public void senUpdateToTableRequest(BaseEntity entity)
    {
        Request request = new Request(Command.UPDATE,entity);
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }

    public void sendRemoveFromTableRequest(BaseEntity entity)
    {
        Request request = new Request(Command.REMOVE,entity);
        Response response = Client.getInstance().sendRequest(request);

        ResponseHandler.processResponse(response);
    }

    public void initAdminComponents(){};

    public void showDialog(Pane root)
    {
        Scene scene = new Scene(root);
        Stage dialog = new Stage();
        dialog.setScene(scene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(App.getAppHandler().getStage());

        dialog.showAndWait();
    }
}
