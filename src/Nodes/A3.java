package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.*;

import static Nodes.Main.cc;

public class A3 {

    public static Client A3ToA1;
    public static Client A3ToA2;
    public static Client A3ToB2;
    public static volatile int lineUpdatedA3;
    public static volatile int lineNoUpdatedA3;

    public A3(){
        Server A3 = new Server(3335, "A3");
        A3ToA1 = new Client("127.0.0.1", 3333, "A3", "A1");
        A3ToA2 = new Client("127.0.0.1", 3334, "A3", "A2");
        A3ToB2 = new Client("127.0.0.1", 3337, "A3", "B2");

        File A3Logs = new File("A3Logs.txt");
        try {
            boolean created = A3Logs.createNewFile();
            if(created){
                FileWriter writeA3Logs = new FileWriter("A3Logs.txt");
                writeA3Logs.write("*********Transactions on Node A3**********\n");
                writeA3Logs.write("lineUpdated = 0\n");
                writeA3Logs.write("lineNoUpdated = 0\n");
                writeA3Logs.close();
                lineUpdatedA3 = 0;
                lineNoUpdatedA3 = 0;
            }   //if
            else{
                FileReader fr = new FileReader(A3Logs);
                BufferedReader br = new BufferedReader(fr);
                String line;
                br.readLine();
                line = br.readLine();
                lineUpdatedA3 = Integer.parseInt(String.valueOf(line.charAt(14)));
                line = br.readLine();
                lineNoUpdatedA3 = Integer.parseInt(String.valueOf(line.charAt(16)));
            }   //else
        } catch (IOException e) {
            e.printStackTrace();
        }

        A3.start();
        A3ToA1.start();
        A3ToA2.start();
        A3ToB2.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cc.start();
    }

    public static void upDateVariablesOnFile(){

        try {
            BufferedReader reader = new BufferedReader(new FileReader("A3Logs.txt"));

            String oldContent = "";
            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String title = oldContent.substring(0, 43);
            String updateLine = oldContent.substring(43, 57) + lineUpdatedA3 + "\n";
            String noUpdateLine = oldContent.substring(59, 75) + lineNoUpdatedA3 + "\n";
            String content = oldContent.substring(77, oldContent.length());

            if(!content.equals("")) {
                char aux = oldContent.charAt(77);
                if (aux == '\n')
                    content = oldContent.substring(78, oldContent.length());
            }

            String newContent = title + updateLine + noUpdateLine + content;

            FileWriter writer = new FileWriter("A3Logs.txt");

            writer.write(newContent);

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
