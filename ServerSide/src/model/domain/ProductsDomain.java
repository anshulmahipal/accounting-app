package model.domain;

import common.model.BaseEntity;
import common.model.entity.Product;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.ProductDataMapper;

import java.util.ArrayList;

public class ProductsDomain
{
    public static Response getProductsList()
    {
        ArrayList<BaseEntity> entities = ProductDataMapper.findAll();

        if(entities.isEmpty())
            return new Response(Notification.GET_ERROR);
        else
            return new Response(Notification.GET_SUCCESS,entities);
    }

    public static Response updateProduct(Product product)
    {
        Product productDuplicate = ProductDataMapper.findByName(product.getName());

        if(productDuplicate!=null && productDuplicate.getId()!=product.getId())
            return new Response(Notification.UPDATE_ERROR);

        int rowsAffected = ProductDataMapper.update(product);

        if(rowsAffected==-1)
            return new Response(Notification.UPDATE_ERROR);
        else
            return new Response(Notification.UPDATE_SUCCESS);
    }

    public static Response removeProduct(Product product)
    {
        int rowsAffected = ProductDataMapper.delete(product);

        if(rowsAffected == -1)
            return new Response(Notification.REMOVE_ERROR);
        else
            return new Response(Notification.REMOVE_SUCCESS);
    }

    public static Response addProduct(Product product)
    {
        Product productDuplicate = ProductDataMapper.findByName(product.getName());

        if(productDuplicate!=null)
            return new Response(Notification.ADD_ERROR);

        int rowsAffected = ProductDataMapper.add(product);

        if(rowsAffected == -1)
            return new Response(Notification.ADD_ERROR);
        else
            return new Response(Notification.ADD_SUCCESS);

    }
}
