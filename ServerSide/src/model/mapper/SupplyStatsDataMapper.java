package model.mapper;

import common.model.BaseEntity;
import common.model.dto.Stats;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplyStatsDataMapper
{
    public static synchronized ArrayList<BaseEntity> findAll(int userId)
    {
        ArrayList<BaseEntity> statistics = new ArrayList<>();

        String getSupply =
                "select s.supplyID, supplyDate, contractorName, sum(productCost*supplyAmount) as total from supplyContent " +
                "inner join supply s on supplyContent.supplyID = s.supplyID " +
                "inner join contractor c on s.contractorID = c.contractorID " +
                "inner join product p on supplyContent.productID = p.productID " +
                "where s.userID = ? " +
                "group by s.supplyID";

        String getSupplyContent =
                "select p.productID, productName, productCost, supplyAmount, (productCost*supplyAmount) as summary from supplyContent " +
                "inner join product p on supplyContent.productID = p.productID " +
                "where supplyID = ? ";

        try
        {
            DataMapperUtil.findStats(getSupply,getSupplyContent,userId,statistics,"supply");
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return statistics;

    }

    public static synchronized int add(Stats supplyStats)
    {
        String getContractorID = "select * from contractor where contractorName=?";
        String addSupplyEntity = "insert into supply(userID, contractorID, supplyDate) values(?,?,?)";
        String getSupplyID = "select * from supply where supplyDate=?";
        String addSupplyContentEntity = "insert into supplyContent values(?,?,?)";

        try
        {
            return DataMapperUtil.addStats(getContractorID,getSupplyID,addSupplyEntity,addSupplyContentEntity,supplyStats,"supply");
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

        String sqlStatement = "delete from supply where userID="+userID;

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

    private SupplyStatsDataMapper(){};
}
