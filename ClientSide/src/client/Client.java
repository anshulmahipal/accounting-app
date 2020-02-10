package client;


import common.util.request.Request;
import common.util.response.Response;
import util.ResponseHandler;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private static volatile Client instance;
    private Socket clientSocket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    private Client()
    {
        try
        {
            clientSocket = new Socket("localhost",2525);

            System.out.println("Client connected to server!");

            objOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objIn = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Client getInstance()
    {
        if(instance == null)
            synchronized (Client.class)
            {
                if(instance == null)
                    instance = new Client();
            }
        return instance;
    }

    public Response sendRequest(Request request)
    {
        Response response = null;

        if(!clientSocket.isClosed())
        {
            try
            {
                System.out.println("\nClient requested: "+request.getCommand());
                objOut.writeObject(request);
                response = (Response) objIn.readObject();
            }
            catch (IOException | ClassNotFoundException  exc)
            {
                exc.printStackTrace();
            }
        }

        return response;
    }

    public void close()
    {
        try
        {
            objIn.close();
            objOut.close();
            clientSocket.close();

            System.out.println("Resources are freed!");
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }
}
