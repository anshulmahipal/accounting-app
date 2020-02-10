package model.domain;

import common.model.BaseEntity;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.SaleStatsDataMapper;
import model.mapper.StorageDataMapper;
import model.mapper.SupplyStatsDataMapper;

import java.util.ArrayList;

public class StorageDomain
{
    public static Response getStorageList(int userID)
    {
        ArrayList<BaseEntity> entities = StorageDataMapper.findAll(userID);

        if(entities.isEmpty())
            return new Response(Notification.GET_ERROR);
        else
            return new Response(Notification.GET_SUCCESS,entities);
    }

    public static Response removeStats(int userID)
    {
        int rowsAffectedSupplies = SupplyStatsDataMapper.deleteAll(userID);
        int rowsAffectedSales = SaleStatsDataMapper.deleteAll(userID);

        if(rowsAffectedSales == -1 || rowsAffectedSupplies == -1)
            return new Response(Notification.REMOVE_ERROR);
        else
            return new Response(Notification.REMOVE_SUCCESS);
    }
}
