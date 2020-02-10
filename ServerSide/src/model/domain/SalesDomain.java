package model.domain;

import common.model.BaseEntity;
import common.model.dto.Stats;
import common.model.dto.StatsContent;
import common.model.entity.User;
import common.util.response.Notification;
import common.util.response.Response;
import model.mapper.SaleStatsDataMapper;

import java.util.ArrayList;

public class SalesDomain
{
    public static Response getSalesList(User user)
    {
        ArrayList<BaseEntity> entities = SaleStatsDataMapper.findAll(user.getId());

        if(entities.isEmpty())
            return new Response(Notification.GET_ERROR);
        else
        {
            for(BaseEntity entity:entities)
            {
                double total = calculateSale(((Stats)entity).getContents(),user.getAdding(),user.getTax());

                ((Stats)entity).setStatsTotal(total);
            }
            return new Response(Notification.GET_SUCCESS, entities);
        }
    }

    public static Response addSale(Stats saleSats)
    {
        int rowsAffected = SaleStatsDataMapper.add(saleSats);

        if(rowsAffected == -1)
            return new Response(Notification.ADD_ERROR);
        else
            return new Response(Notification.ADD_SUCCESS);
    }

    private static double calculateSale(ArrayList<StatsContent> contents, int adding, int tax)
    {
        double totalForSupply = 0;

        for (StatsContent content:contents)
        {
            double saleCost = content.getProduct().getCost();
            saleCost+=saleCost*(double)adding/100;
            saleCost+=saleCost*(double)tax/100;

            content.getProduct().setCost(saleCost);
            content.setProductSummary(saleCost*content.getProductAmount());

            totalForSupply+=content.getProductSummary();
        }

        return totalForSupply;
    }
}
