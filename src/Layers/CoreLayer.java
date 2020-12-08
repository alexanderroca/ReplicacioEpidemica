package Layers;

import java.io.*;
import java.util.ArrayList;


import static Nodes.A1.A1ToA2;
import static Nodes.A1.A1ToA3;
import static Nodes.A2.A2ToA1;
import static Nodes.A2.A2ToA3;
import static Nodes.A3.A3ToA1;
import static Nodes.A3.A3ToA2;

public class CoreLayer {

    private String from;
    private ArrayList<String> to;

    public CoreLayer(String from, ArrayList<String> to) {
        this.from = from;
        this.to = to;
    }

    public void eagerReplication(){

        if(from.equals("A1")){
            A1ToA2.interrupt();
            A1ToA3.interrupt();
        }   //if
        else if(from.equals("A2")){
            A2ToA1.interrupt();
            A2ToA3.interrupt();
        }   //else-if
        else if(from.equals("A3")){
            A3ToA1.interrupt();
            A3ToA2.interrupt();
        }   //else-if
    }

    public String updateEverywhereActiveEagerReplication(){

        try {
            File logFile = new File(from + "Logs.txt");
            FileReader fr = new FileReader(logFile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String lastLine = new String();

            br.readLine();
            while((line=br.readLine())!=null)
                lastLine = line;

            fr.close();

            return lastLine;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void addNewTransactionOnLogFile(String line){
        try {
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
