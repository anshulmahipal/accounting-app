package server;

import util.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadServer
{
    private ServerSocket serverSocket;
    private static ExecutorService executorService;

    private BufferedReader reader;

    private volatile HashMap<Integer,Socket> clientsMap;
    private AtomicInteger id;

    public MultiThreadServer()
    {
        try
        {
            serverSocket = new ServerSocket(Config.getPORT());
            executorService = Executors.newFixedThreadPool(Config.getMaxUsers());
            DBConnection.getInstance(); //connecting to database at server run

            reader = new BufferedReader(new InputStreamReader(System.in));

            id = new AtomicInteger(0);
            clientsMap = new HashMap<>();

            createServerListener();

            System.out.println("Server ran successfully.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(3);
        }
    }

    public void start()
    {
        while (!serverSocket.isClosed())
        {

            try
            {
                Socket clientSocket = serverSocket.accept();

                clientConnectedNotification(clientSocket);

                executorService.execute(new ClientHandler(clientSocket, id.get(), this));
            }
            catch (SocketException exc)//stopping server manually
            {
                break;
            }
            catch (IOException exc)//error
            {
                exc.printStackTrace();
                break;
            }
        }

        executorService.shutdown();
    }

    private void clientConnectedNotification(Socket clientSocket)
    {
        int key = id.incrementAndGet();

        clientsMap.put(key,clientSocket);

        System.out.println("\nClient with port number = "+clientSocket.getPort()+" connected.");
        System.out.println("There are "+clientsMap.size()+" clients on the server.\n");
    }

    void clientDisconnectedNotification(int id)
    {
        int port = clientsMap.get(id).getPort();

        clientsMap.remove(id);

        System.out.println("\nClient with port number = "+port+" disconnected.");
        System.out.println("There are "+clientsMap.size()+ " clients on the server.\n");
    }

    private void stop() throws IOException
    {
        System.out.println("main server initiate exiting...");

        while(!clientsMap.isEmpty())
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException exc)
            {
                exc.printStackTrace();
            }
        }

        reader.close();
        serverSocket.close();
    }

    private void listenCommands()
    {
        try
        {
            if (reader.ready())
            {
                String serverCommand = reader.readLine();

                if (serverCommand.equalsIgnoreCase("showClients"))
                {
                    if (clientsMap.isEmpty())
                        System.out.println("No clients connected.");
                    else
                    {
                        for (Map.Entry<Integer, Socket> entry : clientsMap.entrySet())
                        {
                            System.out.println("Client id#" + entry.getKey() + ", port = " + entry.getValue().getPort());
                        }
                        System.out.println("Total clients: "+clientsMap.size());
                    }
                }
                else if (serverCommand.equalsIgnoreCase("quit"))
                {
                    stop();
                }

            }
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    private void createServerListener()
    {
        new Thread(() ->
        {
            while (!serverSocket.isClosed())
            {
               listenCommands();
            }
        }).start();
    }
}
