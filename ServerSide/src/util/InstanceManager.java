package util;

import common.model.entity.Contractor;
import common.model.entity.Product;
import common.model.entity.User;
import common.util.request.Request;
import common.util.response.Notification;
import common.util.response.Response;
import model.domain.ContractorsDomain;
import model.domain.ProductsDomain;
import model.domain.UserDomain;

class InstanceManager
{
    static Response addEntity(Request request)
    {
        if(request.getEntityData() instanceof Product)
            return ProductsDomain.addProduct((Product)request.getEntityData());
        else if(request.getEntityData() instanceof Contractor)
            return ContractorsDomain.addContractor((Contractor)request.getEntityData());

        return new Response(Notification.UNKNOWN_COMMAND_ERROR);
    }

    static Response updateEntity(Request request)
    {
        if(request.getEntityData() instanceof Product)
            return ProductsDomain.updateProduct((Product) request.getEntityData());
        else if(request.getEntityData() instanceof Contractor)
            return ContractorsDomain.updateContractor((Contractor)request.getEntityData());
        else if(request.getEntityData() instanceof User)
            return UserDomain.updateUser((User)request.getEntityData());

        return new Response(Notification.UNKNOWN_COMMAND_ERROR);
    }

    static Response removeEntity(Request request)
    {
        if(request.getEntityData() instanceof Product)
            return ProductsDomain.removeProduct((Product)request.getEntityData());
        else if(request.getEntityData() instanceof Contractor)
            return ContractorsDomain.removeContractor((Contractor)request.getEntityData());
        else if(request.getEntityData() instanceof User)
            return UserDomain.removeUser((User)request.getEntityData());

        return new Response(Notification.UNKNOWN_COMMAND_ERROR);
    }
}
