package Nodes.Server;

import Nodes.Client.Message;

import java.io.*;
import java.net.Socket;

public class EchoServer extends Thread{
    protected Socket socket;
    protected String serverName;

    public EchoServer(Socket clientsocket, String serverName) {
        this.socket = clientsocket;
        this.serverName = serverName;
    }

    public void run(){

        while (true){
            Message.extractInfo(socket, serverName);
        }   //while
    }
}
