package util;

import client.Client;
import common.model.entity.User;
import common.util.response.Response;
import controller.screenController.BaseScreenController;
import controller.screenController.HomeScreenController;
import main.App;

public class ResponseHandler
{
    private static void trackResponse(Response response)
    {
        System.out.println("Server responded: "+response.getNotification()+"\n");
    }

    public static void processResponse(Response response)
    {
        trackResponse(response);

        switch (response.getNotification())
        {
            case LOGIN_ERROR:
                BaseScreenController controller = App.getAppHandler().getCurrentSceneController();
                controller.showErrorNotification("Неправильный email или пароль. Попробуйте еще раз.");
                break;
            case LOGIN_SUCCESS:
                App.setCurrentUser((User) response.getEntityData());
                System.out.println("Login succeed: "+App.getCurrentUser().toString());
                App.getAppHandler().changeScene("/view/HomeScreen.fxml");
                break;
            case REGISTER_ERROR:
                BaseScreenController controller1 = App.getAppHandler().getCurrentSceneController();
                controller1.showErrorNotification("Пользователь с таким email уже существует.");
                break;
            case REGISTER_SUCCESS:
                App.getAppHandler().changeScene("/view/LoginScreen.fxml");
                App.getAppHandler().getCurrentSceneController().showApproveNotification("Регистрация выполена успешно!");
                break;
            case GET_SUCCESS:
                ((HomeScreenController)App.getAppHandler().getCurrentSceneController()).getCurrentTabController().updateTable(response.getEntitiesData());
                break;
            case GET_ERROR:
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Данные отсутствуют.");
                break;
            case ADD_SUCCESS: case UPDATE_SUCCESS: case REMOVE_SUCCESS:
                App.getAppHandler().getCurrentSceneController().showApproveNotification("Данные успешно обновлены.");
                break;
            case ADD_ERROR: case UPDATE_ERROR:
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Данный элемент уже существует.");
                break;
            case REMOVE_ERROR:
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Данный эелемент числится в статистике.");
                break;
            case QUIT_SUCCESS: //quit success command
                Client.getInstance().close();
                break;
            case QUIT_ERROR:
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Ошибка при запросе отключения.");
                break;
            case UNKNOWN_COMMAND_ERROR:
                App.getAppHandler().getCurrentSceneController().showErrorNotification("Сервер не распознал посланную команду.");
                break;
        }
    }

    private ResponseHandler(){};
}
