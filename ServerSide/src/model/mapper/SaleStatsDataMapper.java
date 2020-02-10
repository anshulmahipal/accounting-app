package model.mapper;

import common.model.BaseEntity;
import common.model.dto.Stats;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SaleStatsDataMapper
{
    public static synchronized ArrayList<BaseEntity> findAll(int userID)
    {
        ArrayList<BaseEntity> statistics = new ArrayList<>();

        String getSale =
                "select s.saleID, saleDate, contractorName, sum(productCost*saleAmount) as total from saleContent " +
                "inner join sale s on saleContent.saleID = s.saleID " +
                "inner join contractor c on s.contractorID = c.contractorID " +
                "inner join product p on saleContent.productID = p.productID " +
                "where s.userID = ? " +
                "group by s.saleID";

        String getSaleContent =
                "select p.productID, productName, productCost, saleAmount, (productCost*saleAmount) as summary from saleContent " +
                "inner join product p on saleContent.productID = p.productID " +
                "where saleID = ? ";

        try
        {
            DataMapperUtil.findStats(getSale,getSaleContent,userID,statistics,"sale");
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return statistics;

    }

    public static synchronized int add(Stats saleStats)
    {
        String getContractorID = "select * from contractor where contractorName=?";
        String addSaleEntity = "insert into sale(userID, contractorID, saleDate) values(?,?,?)";
        String getSaleID = "select * from sale where saleDate=?";
        String addSaleContentEntity = "insert into saleContent values(?,?,?)";

        try
        {
           return DataMapperUtil.addStats(getContractorID,getSaleID,addSaleEntity,addSaleContentEntity,saleStats,"sale");
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int deleteAll(int userID)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "delete from sale where userID="+userID;

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    private SaleStatsDataMapper(){};

}
