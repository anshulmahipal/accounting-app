package model.mapper;

import common.model.entity.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDataMapper
{
    private static User getUser(ResultSet resultSet) throws SQLException
    {
        User user = new User(resultSet.getInt("userID"), resultSet.getString("userEmail"),
                resultSet.getString("userPassword"), resultSet.getBoolean("userIsAdmin"));

        user.setSurname(resultSet.getString("userSurname"));
        user.setName(resultSet.getString("userName"));
        user.setPatronymic(resultSet.getString("userPatronymic"));
        user.setWorkplace(resultSet.getString("userWorkplace"));
        user.setCity(resultSet.getString("userCity"));
        user.setPhone(resultSet.getString("userPhone"));
        user.setTax(resultSet.getInt("userTax"));
        user.setAdding(resultSet.getInt("userAdding"));

        return user;
    }

    public static synchronized User find(String email, String password) //loginning //add exception throw
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from users where userEmail=? and userPassword =?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next() && !resultSet.isAfterLast())
            {
                return getUser(resultSet);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return null;
    }

    public static synchronized User find(String email) //finding duplicates //add  exception throw
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "select * from users where userEmail=?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
            {
                return getUser(resultSet);
            }
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
        }

        return null;
    }

    public static synchronized int add(User user)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "insert into users(userEmail, userPassword, userSurname, userName, userPatronymic," +
                " userWorkplace, userCity, userPhone, userIsAdmin) values(?,?,?,?,?,?,?,?,?)";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getSurname());
            statement.setString(4, user.getName());
            statement.setString(5, user.getPatronymic());
            statement.setString(6, user.getWorkplace());
            statement.setString(7, user.getCity());
            statement.setString(8, user.getPhone());
            statement.setBoolean(9, user.isAdmin());

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int update(User user)
    {
        Connection connection = DBConnection.getInstance().getConnection();

        String sqlStatement = "update users set userPassword=?, userSurname=?, userName=?, userPatronymic=?," +
                " userWorkplace=?, userCity=?, userPhone=?, userTax=?, userAdding=?, userEmail=? where userID=?";

        try
        {
            PreparedStatement statement = connection.prepareStatement(sqlStatement);

            statement.setString(1, user.getPassword());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getName());
            statement.setString(4, user.getPatronymic());
            statement.setString(5, user.getWorkplace());
            statement.setString(6, user.getCity());
            statement.setString(7, user.getPhone());
            statement.setInt(8,user.getTax());
            statement.setInt(9,user.getAdding());
            statement.setString(10,user.getEmail());
            statement.setInt(11,user.getId());

            return statement.executeUpdate();
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    public static synchronized int delete(User user)
    {
        String sqlStatement = "delete from users where userID=?";

        try
        {
           return DataMapperUtil.deleteById(sqlStatement,user);
        }
        catch (SQLException exc)
        {
            exc.printStackTrace();
            return -1;
        }
    }

    private UserDataMapper(){};
}
