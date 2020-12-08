package Layers;

import java.io.*;
import java.util.ArrayList;

import static Nodes.B2.*;

public class Layer2 {

    private String from;
    private ArrayList<String> to;

    public Layer2(String from, ArrayList<String> to) {
        this.from = from;
        this.to = to;
    }

    public boolean pasiveReplication(){

        if(from.equals("B2")){
            B2ToC1.interrupt();
            B2ToC2.interrupt();
        }   //if

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

            while(i < lineUpdatedB2) {
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
