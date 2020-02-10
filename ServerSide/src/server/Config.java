package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class Config
{
    private final static String PROPERTIES_FILE = "resources/server.properties";
    private final static int DEFAULT_PORT = 2025;
    private final static int DEFAULT_USERS=5;

    private static int MAX_USERS;

    private static int PORT;

    static
    {
        Properties properties = new Properties();

        try(FileInputStream propertiesFile = new FileInputStream(PROPERTIES_FILE))
        {
            properties.load(propertiesFile);

            PORT = Integer.parseInt(properties.getProperty("PORT"));

            MAX_USERS = Integer.parseInt(properties.getProperty("MAX_USERS"));
        }
        catch (NumberFormatException | FileNotFoundException exc)
        {
            PORT = DEFAULT_PORT;
            MAX_USERS  = DEFAULT_USERS;
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    public static int getPORT()
    {
        return PORT;
    }

    public static int getMaxUsers()
    {
        return MAX_USERS;
    }
}
