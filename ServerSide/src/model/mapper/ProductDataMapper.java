package model.mapper;

import common.model.BaseEntity;
import common.model.entity.Product;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class ProductDataMapper
{
    private static void setStatement(PreparedStatement statement, Product product) throws SQLException
    {
        statement.setString(1,product.getName());
        statement.setDouble(2,product.getCost());
        statement.setString(3,product.getGroup());
    }

    public static synchronized ArrayList<BaseEntity> findAll()
    {
        ArrayList<BaseEntity> products = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from product";

        try
        {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next())
            {
                int id = resultSet.getInt("productID");
                String name = resultSet.getString("productName");
                double cost = resultSet.getDouble("productCost");
                String group = resultSet.getString("productGroup");

                Product product = new Product(id, name, cost, group);

                products.add(product);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return products;
    }

    public static synchronized Product findByName(String name)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from product where productName='"+name+"'";

        try
        {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            if (resultSet.next() && !resultSet.isAfterLast())
            {
                double cost = resultSet.getDouble("productCost");
                String group = resultSet.getString("productGroup");

                return new Product(resultSet.getInt("productID"), name, cost, group);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }
        return null;
    }

    public static synchronized int add(Product product)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "insert into product(productName, productCost, productGroup) values(?,?,?)";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            setStatement(statement,product);

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int update(Product product)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "update product set productName=?, productCost=?, productGroup=? where productID=?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            setStatement(statement, product);
            statement.setInt(4, product.getId());

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int delete(Product product)
    {
        String sqlStatement = "delete from product where productID=?";

        try
        {
            return DataMapperUtil.deleteById(sqlStatement,product);
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    private ProductDataMapper(){};
}
