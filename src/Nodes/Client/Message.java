package Nodes.Client;

import Layers.CoreLayer;
import Layers.Layer1;
import Layers.Layer2;
import Nodes.A2;
import Nodes.A3;
import Nodes.B2;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static Nodes.A2.lineNoUpdatedA2;
import static Nodes.A3.lineNoUpdatedA3;
import static Nodes.B2.lineNoUpdatedB2;
import static Nodes.Client.ClientCommand.cirt;

public class Message {

    public static volatile boolean blockTransaction = false;
    public static volatile int synchronizationComplete = 0;

    public static void sendInfo(Socket to, String from, String info, boolean isTransaction){
        DataOutputStream out = null;

        try {
            // sends output to the socket
            out = new DataOutputStream(to.getOutputStream());
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }

        try
        {
            if(isTransaction)
                out.writeUTF("From " + from + " -> Log content: " + info);
            else
                out.writeUTF("From " + from + ": " + info);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void extractInfo(Socket socket, String actualNode){
        DataInputStream in;

        try {
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            return;
        }

        String line;

        try {
            line = in.readUTF();
            //System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if(line.substring(11,22).equals("Log content")){

            if(actualNode.equals("C1") || actualNode.equals("C2")){
                cirt.interrupt();
                Layer2 l2 = new Layer2(actualNode, null);
                l2.addNewTransactionOnLogFile(line.substring(24, line.length()));
            }   //if
            else if(actualNode.equals("B2")) {
                endLinesFromMessage(line.substring(24, line.length()));
                Layer1 l1 = new Layer1(actualNode, null);
                l1.addNewTransactionOnLogFile(line.substring(24, line.length()));
            }   //else
            else if(actualNode.equals("B1")){
                Layer1 l1 = new Layer1(actualNode, null);
                l1.addNewTransactionOnLogFile(line.substring(24, line.length()));
            }   //else-if
            else {
                blockTransaction = true;

                if(actualNode.equals("A2")) {
                    lineNoUpdatedA2++;
                    A2.upDateVariablesOnFile();
                }   //else-if
                else if(actualNode.equals("A3")) {
                    lineNoUpdatedA3++;
                    A3.upDateVariablesOnFile();
                }   //else-if

                cirt.interrupt();

                CoreLayer cl = new CoreLayer(actualNode, null);
                cl.addNewTransactionOnLogFile(line.substring(24, line.length()));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blockTransaction = false;
            }   //else
        }   //if
        else if(line.length() == 35 && line.substring(9,34).equals("synchronization completed"))
            synchronizationComplete++;
    }

    private static void endLinesFromMessage(String info){

        int count = 1;
        for(int i = 0; i < info.length(); i++){

            if(info.charAt(i) == '\n')
                count++;
        }   //for

        lineNoUpdatedB2 += count;
        B2.upDateVariablesOnFile();
    }
}
