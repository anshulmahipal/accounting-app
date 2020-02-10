package util;

import common.model.dto.Stats;
import common.model.entity.User;
import common.util.request.Request;
import common.util.response.Notification;
import common.util.response.Response;
import model.domain.*;


public class RequestHandler
{
    private static void trackRequest(Request request, int port)
    {
        System.out.println("Client with port number = "+port+" requested: "+request.getCommand());
    }

    public static Response processRequest(Request request, int port) //make this method returning Response instance
    {
        trackRequest(request, port);

        switch (request.getCommand())
        {
            case LOGIN:
                return UserDomain.login((User) request.getEntityData());
            case REGISTER:
                return UserDomain.register((User) request.getEntityData());
            case GET_SUPPLIES:
                return SuppliesDomain.getSuppliesList((User) request.getEntityData());
            case GET_SALES:
                return SalesDomain.getSalesList((User) request.getEntityData());
            case GET_PRODUCTS:
                return ProductsDomain.getProductsList();
            case GET_CONTRACTORS:
                return ContractorsDomain.getContractorsList();
            case GET_STORAGE:
                return StorageDomain.getStorageList(request.getEntityData().getId());
            case ADD_TO_SUPPLIES:
                return SuppliesDomain.addSupply((Stats) request.getEntityData());
            case ADD_TO_SALES:
                return SalesDomain.addSale((Stats)request.getEntityData());
            case UPDATE:
                return InstanceManager.updateEntity(request);
            case REMOVE:
                return InstanceManager.removeEntity(request);
            case ADD:
                return InstanceManager.addEntity(request);
            case REMOVE_STATS:
                return StorageDomain.removeStats(request.getEntityData().getId());
            case QUIT:
                return new Response(Notification.QUIT_SUCCESS);
            default:
                return new Response(Notification.UNKNOWN_COMMAND_ERROR);

        }
    }

    private RequestHandler(){};
}

//привести доменй модели к общему классу??