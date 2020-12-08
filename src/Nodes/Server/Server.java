package Nodes.Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private int portNumber;
    private String serverName;

    public Server(int portNumber, String serverName) {
        this.portNumber = portNumber;
        this.serverName = serverName;
    }

    public void run(){

        // starts server and waits for a connection
        try {
            server = new ServerSocket(portNumber);
            System.out.println("Server " + serverName + " started");

            System.out.println("Waiting for a client ...");

            while(true){

                try {
                    socket = server.accept();
                    System.out.println("Client accepted");
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }

                new EchoServer(socket, serverName).start();
            }   //while

        } catch (IOException i) {
            System.out.println(i);
        }
    }
}