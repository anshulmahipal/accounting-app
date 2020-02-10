package model.mapper;

import common.model.BaseEntity;
import common.model.dto.StatsContent;
import common.model.entity.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StorageDataMapper
{
    public static synchronized ArrayList<BaseEntity> findAll(int userID)
    {
        ArrayList<BaseEntity> entities = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        String chooseAllSupplies =
                "SELECT supplyContent.productID, productName, productGroup, productCost, sum(supplyAmount) AS supplyAmount FROM supplyContent " +
                "INNER JOIN product p ON supplyContent.productID = p.productID " +
                "INNER JOIN supply s ON supplyContent.supplyID = s.supplyID " +
                "WHERE userID =? " +
                "GROUP BY p.productID";

        String chooseAllSales =
                "SELECT saleContent.productID, productName, productGroup, productCost, sum(saleAmount) AS saleAmount FROM saleContent " +
                "INNER JOIN product p ON saleContent.productID = p.productID " +
                "INNER JOIN sale s ON saleContent.saleID = s.saleID " +
                "WHERE userID =? " +
                "GROUP BY p.productID";

        String sqlStatement =
                "SELECT supplies.productID,supplies.productName,supplies.productGroup,supplies.productCost, " +
                "supplyAmount-ifnull(saleAmount,0) AS amount FROM ("+chooseAllSupplies+") as supplies " +
                "left join ("+chooseAllSales+") as sales on supplies.productID=sales.productID " +
                "having amount>0";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setInt(1, userID);
            statement.setInt(2, userID);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                int productID = resultSet.getInt("productID");
                String productName = resultSet.getString("productName");
                String productGroup = resultSet.getString("productGroup");
                double productCost = resultSet.getDouble("productCost");
                int productAmount = resultSet.getInt("amount");

                StatsContent content = new StatsContent(new Product(productID, productName, productCost, productGroup), productAmount);

                entities.add(content);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return entities;
    }
}
