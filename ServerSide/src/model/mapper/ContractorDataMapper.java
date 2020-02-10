package model.mapper;

import common.model.BaseEntity;
import common.model.entity.Contractor;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class ContractorDataMapper
{
    private static String[] getContractorData(ResultSet resultSet) throws SQLException
    {
        String name = resultSet.getString("contractorName");
        String adress = resultSet.getString("contractorAdress");
        String phone = resultSet.getString("contractorPhone");
        String requisites = resultSet.getString("contractorRequisites");
        String type = resultSet.getString("contractorType");

        return new String[]{name,adress,phone,requisites,type};
    }

    private static Contractor getContractor(ResultSet resultSet) throws SQLException
    {
        if (resultSet.next() && !resultSet.isAfterLast())
        {
            String[] data = getContractorData(resultSet);

            return new Contractor(resultSet.getInt("contractorID"), data[0], data[1], data[2], data[3], data[4]);
        }
        return null;
    }

    private static Contractor getContractor(String requisites, String sqlStatement)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setString(1,requisites);

            ResultSet resultSet = statement.executeQuery();

            return getContractor(resultSet);
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return null;
    }

    private static void setStatement(PreparedStatement statement, Contractor contractor) throws SQLException
    {
        statement.setString(1, contractor.getName());
        statement.setString(2, contractor.getAdress());
        statement.setString(3, contractor.getPhone());
        statement.setString(4, contractor.getRequisites());
        statement.setString(5, contractor.getType());
    }

    public static synchronized ArrayList<BaseEntity> findAll()
    {
        ArrayList<BaseEntity> contractors = new ArrayList<>();

        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from contractor";

        try
        {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            while (resultSet.next())
            {
                int id = resultSet.getInt("contractorID");
                String[] data = getContractorData(resultSet);

                Contractor contractor = new Contractor(id, data[0], data[1], data[2], data[3], data[4]);

                contractors.add(contractor);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return contractors;
    }

    public static synchronized Contractor findByName(String name)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from contractor where contractorName='"+name+"'";

        try
        {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlStatement);

            return getContractor(resultSet);
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return null;
    }

    public static synchronized Contractor findByPhone(String phone)
    {
        String sqlStatement = "select * from contractor where contractorPhone=?";

        return getContractor(phone, sqlStatement);
    }

    public static synchronized Contractor findByRequisites(String requisites)
    {
        String sqlStatement = "select * from contractor where contractorRequisites=?";

        return getContractor(requisites, sqlStatement);
    }

    public static synchronized int add(Contractor contractor)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "insert into contractor(contractorName, contractorAdress, contractorPhone, contractorRequisites, contractorType)" +
                "values(?,?,?,?,?)";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            setStatement(statement,contractor);

            return statement.executeUpdate();
        }
        catch (SQLException  exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int update(Contractor contractor)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String  sqlStatement = "update contractor set contractorName=?, contractorAdress=?, contractorPhone=?, contractorRequisites=?," +
                " contractorType=? where contractorID = ?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            setStatement(statement,contractor);
            statement.setInt(6, contractor.getId());

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }

    }

    public static synchronized int delete(Contractor contractor)
    {
        String sqlStatement = "delete from contractor where contractorID=?";

        try
        {
            return DataMapperUtil.deleteById(sqlStatement,contractor);
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    private ContractorDataMapper(){};
}
