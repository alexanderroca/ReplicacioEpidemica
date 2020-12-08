package Layers;

import java.io.*;
import java.util.ArrayList;

import static Nodes.A2.*;
import static Nodes.A3.*;

public class Layer1 {

    private String from;
    private ArrayList<String> to;

    public Layer1(String from, ArrayList<String> to) {
        this.from = from;
        this.to = to;
    }

    public boolean pasiveReplication(){

        if(from.equals("A2"))
            A2ToB1.interrupt();
        else if(from.equals("A3"))
            A3ToB2.interrupt();
        return true;
    }

    public StringBuffer lazyPassivePrimaryBackup(){

        StringBuffer sb = new StringBuffer();

        try {
            File logFile = new File(from + "Logs.txt");
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            String line;

            int i = 0;

            br.readLine();
            br.readLine();
            br.readLine();

            int lineUpdated = 0;

            if(from.equals("A2"))
                lineUpdated = lineUpdatedA2;
            else if(from.equals("A3"))
                lineUpdated = lineUpdatedA3;

            while(i < lineUpdated) {
                br.readLine();
                i++;
            }   //while

            while((line=br.readLine())!=null)
                sb.append(line + "\n");

            sb.deleteCharAt(sb.length() - 1);
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return sb;
    }

    public void addNewTransactionOnLogFile(String line){

        try {
            System.out.println();
            File logFile = new File(from + "Logs.txt");
            FileWriter fr = new FileWriter(logFile, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(line + "\n");

            br.close();
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
