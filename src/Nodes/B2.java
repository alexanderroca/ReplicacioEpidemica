package Nodes;

import Nodes.Client.Client;
import Nodes.Client.ClientCommand;
import Nodes.Server.Server;

import java.io.*;

import static Nodes.Client.ClientCommand.cirt;
import static Nodes.Main.cc;

public class B2 {

    public static Client B2ToA3;
    public static Client B2ToC1;
    public static Client B2ToC2;
    public static volatile boolean timer10s;
    public static volatile int lineUpdatedB2;
    public static volatile int lineNoUpdatedB2;

    public B2(){
        Server B2 = new Server(3337, "B2");
        B2ToA3 = new Client("127.0.0.1", 3335, "B2", "A3");
        B2ToC1 = new Client("127.0.0.1", 3338, "B2", "C1");
        B2ToC2 = new Client("127.0.0.1", 3339, "B2", "C2");

        File B2Logs = new File("B2Logs.txt");
        try {
            boolean created = B2Logs.createNewFile();
            if(created){
                FileWriter writeB2Logs = new FileWriter("B2Logs.txt");
                writeB2Logs.write("*********Transactions on Node B2**********\n");
                writeB2Logs.write("lineUpdated = 0\n");
                writeB2Logs.write("lineNoUpdated = 0\n");
                writeB2Logs.close();
                lineUpdatedB2 = 0;
                lineNoUpdatedB2 = 0;
            }   //if
            else{
                FileReader fr = new FileReader(B2Logs);
                BufferedReader br = new BufferedReader(fr);
                String line;
                br.readLine();
                line = br.readLine();
                lineUpdatedB2 = Integer.parseInt(String.valueOf(line.charAt(14)));
                line = br.readLine();
                lineNoUpdatedB2 = Integer.parseInt(String.valueOf(line.charAt(16)));
            }   //else
        } catch (IOException e) {
            e.printStackTrace();
        }

        B2.start();
        B2ToA3.start();
        B2ToC1.start();
        B2ToC2.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cc.start();

        while (true){
            timer10s = false;
            timer();
            cc.interrupt();
        }   //while

    }

    private void timer(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cirt.interrupt();
        cc.interrupt();
        timer10s = true;
    }

    public static void upDateVariablesOnFile(){

        try {
            BufferedReader reader = new BufferedReader(new FileReader("B2Logs.txt"));

            String oldContent = "";
            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String title = oldContent.substring(0, 43);
            String updateLine = oldContent.substring(43, 57) + lineUpdatedB2 + "\n";
            String noUpdateLine = oldContent.substring(59, 75) + lineNoUpdatedB2 + "\n";
            String content = oldContent.substring(77, oldContent.length());

            String newContent = title + updateLine + noUpdateLine + content;

            FileWriter writer = new FileWriter("B2Logs.txt");

            writer.write(newContent);

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
