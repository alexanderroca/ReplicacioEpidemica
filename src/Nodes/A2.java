package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.*;

import static Nodes.Main.cc;

public class A2 {

    public static Client A2ToA1;
    public static Client A2ToA3;
    public static Client A2ToB1;
    public static volatile int lineUpdatedA2;
    public static volatile int lineNoUpdatedA2;

    public A2(){
        Server A2 = new Server(3334, "A2");
        A2ToA1 = new Client("127.0.0.1", 3333, "A2", "A1");
        A2ToA3 = new Client("127.0.0.1", 3335, "A2", "A3");
        A2ToB1 = new Client("127.0.0.1", 3336, "A2", "B1");

        File A2Logs = new File("A2Logs.txt");
        try {
            boolean created = A2Logs.createNewFile();
            if(created){
                FileWriter writeA2Logs = new FileWriter("A2Logs.txt");
                writeA2Logs.write("*********Transactions on Node A2**********\n");
                writeA2Logs.write("lineUpdated = 0\n");
                writeA2Logs.write("lineNoUpdated = 0\n");
                writeA2Logs.close();
                lineUpdatedA2 = 0;
                lineNoUpdatedA2 = 0;
            }   //if
            else{
                FileReader fr = new FileReader(A2Logs);
                BufferedReader br = new BufferedReader(fr);
                String line;
                br.readLine();
                line = br.readLine();
                lineUpdatedA2 = Integer.parseInt(String.valueOf(line.charAt(14)));
                line = br.readLine();
                lineNoUpdatedA2 = Integer.parseInt(String.valueOf(line.charAt(16)));
            }   //else
        } catch (IOException e) {
            e.printStackTrace();
        }

        A2.start();
        A2ToA1.start();
        A2ToA3.start();
        A2ToB1.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cc.start();
    }

    public static void upDateVariablesOnFile(){

        try {
            BufferedReader reader = new BufferedReader(new FileReader("A2Logs.txt"));

            String oldContent = "";
            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String title = oldContent.substring(0, 43);
            String updateLine = oldContent.substring(43, 57) + lineUpdatedA2 + "\n";
            String noUpdateLine = oldContent.substring(59, 75) + lineNoUpdatedA2 + "\n";
            String content = oldContent.substring(77, oldContent.length());

            if(!content.equals("")) {
                char aux = oldContent.charAt(77);
                if (aux == '\n')
                    content = oldContent.substring(78, oldContent.length());
            }

            String newContent = title + updateLine + noUpdateLine + content;

            FileWriter writer = new FileWriter("A2Logs.txt");

            writer.write(newContent);

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
