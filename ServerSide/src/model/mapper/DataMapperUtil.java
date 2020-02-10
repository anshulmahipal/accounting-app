package model.mapper;

import common.model.BaseEntity;
import common.model.dto.Stats;
import common.model.dto.StatsContent;
import common.model.entity.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

class DataMapperUtil
{
    static int deleteById(String sqlStatement, BaseEntity entity) throws SQLException
    {
        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement statement = connection.prepareStatement(sqlStatement);

        statement.setInt(1,entity.getId());

        try
        {
            return statement.executeUpdate();
        }
        catch (SQLIntegrityConstraintViolationException exc)
        {
            return -1;
        }
    }

    static void findStats(String getStats, String getStatsContent, int userID, ArrayList<BaseEntity> statistics, String tableName)
            throws SQLException
    {
        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement getStatsStatement = connection.prepareStatement(getStats);
        PreparedStatement getStatsContentStatement = connection.prepareStatement(getStatsContent);

        getStatsStatement.setInt(1, userID);

        ResultSet resultSet = getStatsStatement.executeQuery();

        while (resultSet.next())
        {
            int statsID = resultSet.getInt(tableName+"ID");

            ArrayList<StatsContent> contents = new ArrayList<>();

            getStatsContentStatement.setInt(1, statsID);

            ResultSet resultSet1 = getStatsContentStatement.executeQuery();

            while (resultSet1.next())
            {

                int productID = resultSet1.getInt("productID");
                String productName = resultSet1.getString("productName");
                double productCost = resultSet1.getDouble("productCost");
                int productAmount = resultSet1.getInt(tableName+"Amount");
                double summary = resultSet1.getDouble("summary");

                StatsContent content = new StatsContent(new Product(productID, productName, productCost, null), productAmount, summary);

                contents.add(content);
            }

            Timestamp statsDate = resultSet.getTimestamp(tableName+"Date");
            double total = resultSet.getDouble("total");
            String contractorName = resultSet.getString("contractorName");


            Stats stats = new Stats(userID, statsID, statsDate, total, contractorName, contents);

            statistics.add(stats);
        }
    }

    static int addStats(String getContractorID, String getStatsID, String addStatsEntity, String addStatsContentEntity, Stats stats, String tableName)
            throws SQLException
    {
        int contractorID = 0, statsID = 0, rowsAffected = 0;

        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement statement = connection.prepareStatement(getContractorID);

        statement.setString(1, stats.getContractorName());

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
            contractorID = resultSet.getInt("contractorID");

        statement = connection.prepareStatement(addStatsEntity);

        statement.setInt(1, stats.getId());
        statement.setInt(2, contractorID);
        statement.setTimestamp(3, stats.getStatsDate());

        rowsAffected = statement.executeUpdate();

        if(rowsAffected<=0)
            return -1;

        statement = connection.prepareStatement(getStatsID);

        statement.setTimestamp(1, stats.getStatsDate());

        resultSet = statement.executeQuery();
        if(resultSet.next())
            statsID = resultSet.getInt(tableName+"ID");

        statement = connection.prepareStatement(addStatsContentEntity);

        statement.setInt(1, statsID);

        ArrayList<StatsContent> contents = stats.getContents();

        for (StatsContent content : contents)
        {
            statement.setInt(2, content.getProduct().getId());
            statement.setInt(3, content.getProductAmount());

            rowsAffected = statement.executeUpdate();

            if(rowsAffected<=0)
                return -1;
        }

        return rowsAffected;
    }

    private DataMapperUtil(){};
}
