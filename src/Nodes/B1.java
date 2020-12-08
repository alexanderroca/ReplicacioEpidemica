package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static Nodes.Main.cc;

public class B1 {

    public B1(){
        Server B1 = new Server(3336, "B1");
        Client B1ToA2 = new Client("127.0.0.1", 3334, "B1", "A2");

        File B1Logs = new File("B1Logs.txt");
        try {
            boolean created = B1Logs.createNewFile();
            if(created){
                FileWriter writeB1Logs = new FileWriter("B1Logs.txt");
                writeB1Logs.write("*********Transactions on Node B1**********\n");
                writeB1Logs.close();
            }   //if
        } catch (IOException e) {
            e.printStackTrace();
        }

        B1.start();
        B1ToA2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cc.start();
    }
}
