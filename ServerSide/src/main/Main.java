package main;

import server.MultiThreadServer;

public class Main {

    public static void main(String[] args)
    {
        MultiThreadServer server = new MultiThreadServer();
        server.start();
    }
}
