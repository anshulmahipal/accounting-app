package model.domain;

import common.model.BaseEntity;
import common.model.dto.Stats;
import common.model.entity.User;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.SupplyStatsDataMapper;

import java.util.ArrayList;

public class SuppliesDomain
{
    public static Response getSuppliesList(User user)
    {
        ArrayList<BaseEntity> entities = SupplyStatsDataMapper.findAll(user.getId());
        if(entities.isEmpty())
            return new Response(Notification.GET_ERROR);
        else
            return new Response(Notification.GET_SUCCESS,entities);
    }

    public static Response addSupply(Stats supplyStats)
    {
        int rowsAffected = SupplyStatsDataMapper.add(supplyStats);

        if(rowsAffected == -1)
            return new Response(Notification.ADD_ERROR);
        else
            return new Response(Notification.ADD_SUCCESS);
    }
}
