package server;

import common.util.request.Request;
import common.util.response.Notification;
import common.util.response.Response;
import util.RequestHandler;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    private MultiThreadServer server;

    private Socket clientSocket;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;

    private int id;

    ClientHandler(Socket clientSocket, int id, MultiThreadServer server)
    {
        try
        {
            this.server = server;
            this.clientSocket = clientSocket;
            this.id = id;

            objOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objIn = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
            System.exit(2);
        }
    }


    private void sendResponse(Response response)
    {
        if(!clientSocket.isClosed())
        {
            try
            {
                System.out.println("Server responded: "+response.getNotification());
                objOut.writeObject(response);
            }
            catch (IOException exc)
            {
                exc.printStackTrace();
            }
        }
    }

    private void close()
    {
        try
        {
            Thread.sleep(3000); //waiting for client to disconnect
            objIn.close();
            objOut.close();
            clientSocket.close();

            server.clientDisconnectedNotification(id);

            System.out.println("Resources are freed!");
        }
        catch (IOException | InterruptedException exc)
        {
            exc.printStackTrace();
        }
    }



    @Override
    public void run()
    {
        while (!clientSocket.isClosed())
        {
            System.out.println("\n------Server reading from channel------\n");

            try
            {
                Request request = (Request) objIn.readObject();

                Response response = RequestHandler.processRequest(request,clientSocket.getPort());

                sendResponse(response);

                if(response.getNotification() == Notification.QUIT_SUCCESS) //quit notification
                    close();
            }
            catch (ClassNotFoundException exc)
            {
                exc.printStackTrace();
            }
            catch (EOFException exc)
            {
                close();
                break;
            }
            catch (IOException exc)
            {
                exc.printStackTrace();
                break;
            }
        }
    }
}
