package Nodes.Client;

import java.io.IOException;
import java.net.Socket;

import static Nodes.Client.Message.blockTransaction;
import static Nodes.Main.cc;

public class Client extends Thread {

    private Socket socket;
    private String address;
    private int port;
    private String sourceNodeName;
    private String destinationNodeName;

    public Client(String address, int portToConnect, String sourceNodeName, String destinationNodeName) {
        this.address = address;
        this.port = portToConnect;
        this.sourceNodeName = sourceNodeName;
        this.destinationNodeName = destinationNodeName;
    }

    public void run(){

        boolean keepRunning = true;
        while(keepRunning) {
            try {
                socket = new Socket(address, port);
                System.out.println("Connected");
                keepRunning = false;
                Message.sendInfo(socket, sourceNodeName, sourceNodeName + " connected to " + destinationNodeName, false);
            } catch (IOException i) {}
        }   //while

        while (true){

            if(this.isInterrupted()){

                if(blockTransaction)
                    Message.sendInfo(socket, sourceNodeName, "synchronization completed!", false);
                else {
                    if(sourceNodeName.equals("B2") || sourceNodeName.equals("A2") && destinationNodeName.equals("B1") || sourceNodeName.equals("A3") && destinationNodeName.equals("B2"))
                        Message.sendInfo(socket, sourceNodeName, String.valueOf(cc.getSourceNodeLogInfoBuffer()), true);
                    else
                        Message.sendInfo(socket, sourceNodeName, cc.getSourceNodeLogInfo(), true);
                }
                try {
                    this.join();
                } catch (InterruptedException e) {}
            }   //if
        }   //while
    }
}
