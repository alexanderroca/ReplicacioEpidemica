package Nodes;

import Layers.CoreLayer;
import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Client.ConsoleInputReadType;
import Nodes.Server.Server;
import static Nodes.Main.cc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class A1 {

    public static Client A1ToA2;
    public static Client A1ToA3;

    public A1(){
        Server A1 = new Server(3333, "A1");
        A1ToA2 = new Client("127.0.0.1", 3334, "A1", "A2");
        A1ToA3 = new Client("127.0.0.1", 3335, "A1", "A3");

        File A1Logs = new File("A1Logs.txt");
        try {
            boolean created = A1Logs.createNewFile();
            if(created){
                FileWriter writeA1Logs = new FileWriter("A1Logs.txt");
                writeA1Logs.write("*********Transactions on Node A1**********\n");
                writeA1Logs.close();
            }   //if
        } catch (IOException e) {
            e.printStackTrace();
        }

        A1.start();
        A1ToA2.start();
        A1ToA3.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cc.start();
    }
}
