package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static Nodes.Main.cc;

public class C2 {

    public C2(){
        Server C2 = new Server(3339, "C2");
        Client C2ToB2 = new Client("127.0.0.1", 3337, "C2", "B2");

        File C2Logs = new File("C2Logs.txt");
        try {
            boolean created = C2Logs.createNewFile();
            if(created){
                FileWriter writeC2Logs = new FileWriter("C2Logs.txt");
                writeC2Logs.write("*********Transactions on Node C2**********\n");
                writeC2Logs.close();
            }   //if
        } catch (IOException e) {
            e.printStackTrace();
        }

        C2.start();
        C2ToB2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cc.start();
    }
}
