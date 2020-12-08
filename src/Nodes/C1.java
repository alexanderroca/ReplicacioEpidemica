package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static Nodes.Main.cc;

public class C1 {

    public C1(){

        Server C1 = new Server(3338, "C1");
        Client C1ToB2 = new Client("127.0.0.1", 3337, "C1", "B2");

        File C1Logs = new File("C1Logs.txt");
        try {
            boolean created = C1Logs.createNewFile();
            if(created){
                FileWriter writeC1Logs = new FileWriter("C1Logs.txt");
                writeC1Logs.write("*********Transactions on Node C1**********\n");
                writeC1Logs.close();
            }   //if
        } catch (IOException e) {
            e.printStackTrace();
        }

        C1.start();
        C1ToB2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cc.start();
    }

}
