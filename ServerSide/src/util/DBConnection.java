package util;

import server.Config;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class DBConnection
{
    private static volatile DBConnection instance;
    private volatile Connection connection;

    private DBConnection()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/accounting", "root", "tapuza11");

            System.out.println("Database connection successful.");
        }
        catch (ClassNotFoundException | SQLException exc)
        {
            exc.printStackTrace();
            System.exit(2);
        }
    }

    public static DBConnection getInstance()
    {
        if(instance == null)
            synchronized (DBConnection.class)
            {
                if(instance == null)
                    instance = new DBConnection();
            }
        return instance;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public static void close(ResultSet resultSet)
    {
        try
        {
            if(resultSet!=null)
                resultSet.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void close(Statement statement)
    {
        try
        {
            if(statement!=null)
                statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void destroy()
    {
        try
        {
            if(connection!=null)
                connection.close();

            System.out.println("DBConnection closed.");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
