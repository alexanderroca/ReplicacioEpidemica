package Nodes.Client;

import Layers.CoreLayer;
import Layers.Layer1;
import Layers.Layer2;
import Nodes.A2;
import Nodes.A3;
import Nodes.B2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static Nodes.B2.lineNoUpdatedB2;
import static Nodes.B2.lineUpdatedB2;
import static Nodes.A2.lineNoUpdatedA2;
import static Nodes.A2.lineUpdatedA2;
import static Nodes.A3.lineNoUpdatedA3;
import static Nodes.A3.lineUpdatedA3;
import static Nodes.Client.Message.*;

public class ClientCommand extends Thread {

    private final String nodeName;
    private final String NOREADONLY = "b, r(12), w(49,53), r(69), c\n";
    private final String READONLY;
    private CoreLayer cl;
    private Layer1 l1;
    private Layer2 l2;
    private String sourceNodeLogInfo;
    private StringBuffer sourceNodeLogInfoBuffer;
    private boolean source = false;
    public static volatile ConsoleInputReadType cirt;
    private int layer;
    private boolean A2OrA3SychronaizingWithLayer1 = false;
    private boolean B2SychronaizingWithLayer2 = false;

    public ClientCommand(String nodeName) {
        this.nodeName = nodeName;
        layer = -1;

        if(isNodeLayerCore()) {
            layer = 0;
            cl = new CoreLayer(nodeName, getToCoreLayer());
            if(nodeName.equals("A2") || nodeName.equals("A3"))
                l1 = new Layer1(nodeName, getToLayer1());
        }   //if
        else if(isNodeLayer1()) {
            layer = 1;
            if(nodeName.equals("B2"))
                l2 = new Layer2(nodeName, getToLayer2());
        }   //else-if
        else if(isNodeLayer2()) {
            layer = 2;
        }   //else-if

        READONLY = "b" + layer + ", r(30), r(49), r(69), c\n";
        sourceNodeLogInfo = "";
        sourceNodeLogInfoBuffer = new StringBuffer();
        cirt = new ConsoleInputReadType();
    }

    public void run(){
        while(true) {
            try {
                if(this.isInterrupted()) {

                    if(layer == 0) {

                        System.out.println("Synchronizing nodes from core layer, please wait...");
                        if (source)
                            sourceNodeLogInfo = cl.updateEverywhereActiveEagerReplication();
                        else
                            cl.eagerReplication();

                        while (blockTransaction);

                        if(nodeName.equals("A2") || nodeName.equals("A3")){
                            if((lineNoUpdatedA3 - 10) >= lineUpdatedA3 || (lineNoUpdatedA2 - 10) >= lineUpdatedA2){
                                System.out.println("\nSynchronizing nodes from layer 1, please wait...");
                                sourceNodeLogInfoBuffer = l1.lazyPassivePrimaryBackup();
                                A2OrA3SychronaizingWithLayer1 = l1.pasiveReplication();
                            }   //if
                            else
                                System.out.println("\nNothing to synchronize with nodes B1 and B2");
                        }   //if
                    }   //if
                    else if(layer == 1){

                        if(nodeName.equals("B2")) {
                            if (lineUpdatedB2 == lineNoUpdatedB2)
                                System.out.println("\nNothing to synchronize with nodes C1 and C2");
                            else {
                                System.out.println("\nSynchronizing nodes from layer 2, please wait...");
                                sourceNodeLogInfoBuffer = l2.lazyPassivePrimaryBackup();
                                B2SychronaizingWithLayer2 = l2.pasiveReplication();
                                lineUpdatedB2 = lineNoUpdatedB2;
                                B2.upDateVariablesOnFile();
                            }   //else
                        }   //if
                    }   //else-if

                    this.join();

                }   //if
                else
                    transaction();

            } catch (InterruptedException e) {

                if(layer == 0) {
                    if(source) {
                        while (source) {
                            if (synchronizationComplete == 2)
                                break;
                        }   //while
                        isOrigin = false;
                    }   //if
                    System.out.println("Synchronization nodes from core layer completed!");

                    if(A2OrA3SychronaizingWithLayer1) {
                        System.out.println("Synchronization nodes from layer 1 completed!");
                        if(nodeName.equals("A2")){
                            lineUpdatedA2 = lineNoUpdatedA2;
                            A2.upDateVariablesOnFile();
                        }   //if
                        else if(nodeName.equals("A3")){
                            lineUpdatedA3 = lineNoUpdatedA3;
                            A3.upDateVariablesOnFile();
                        }   //else-if
                    }   //if

                }   //if
                else if(layer == 1) {
                    if(nodeName.equals("B2") && B2SychronaizingWithLayer2)
                        System.out.println("Synchronization nodes from layer 2 completed!");
                }
                else if(layer == 2)
                    System.out.println("\nSynchronization nodes from layer 2 completed!");

                synchronizationComplete = 0;
                source = false;
                A2OrA3SychronaizingWithLayer1 = false;
                B2SychronaizingWithLayer2 = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   //while
    }

    private void transaction() throws IOException {

        boolean logModified = false;
        System.out.println("**********USER COMMANDS**********");
        System.out.println("Select type of transaction:");
        System.out.println("\t1 - read-only");
        System.out.println("\t2 - no-read-only");
        System.out.print("Option: ");

        String type = "";

        cirt.run();

        while (cirt.getLine() == null)
            if(cirt.isInterrupted()) {
                this.interrupt();
                cirt = new ConsoleInputReadType();
                return;
            }

        while((type = cirt.getLine()).equals(""));

        if(type.equals("1")) {
            writeTransactionOnLogFile(1);

            if(nodeName.equals("B2")) {
                lineNoUpdatedB2++;
                B2.upDateVariablesOnFile();
            }
            else if(nodeName.equals("A2")) {
                lineNoUpdatedA2++;
                A2.upDateVariablesOnFile();
            }   //else-if
            else if(nodeName.equals("A3")) {
                lineNoUpdatedA3++;
                A3.upDateVariablesOnFile();
            }   //else-if

            logModified = true;
        }   //if
        else if(type.equals("2") && isNodeLayerCore()) {
            writeTransactionOnLogFile(2);
            logModified = true;

            if(nodeName.equals("A2")) {
                lineNoUpdatedA2++;
                A2.upDateVariablesOnFile();
            }   //if
            else if(nodeName.equals("A3")) {
                lineNoUpdatedA3++;
                A3.upDateVariablesOnFile();
            }   //else-if
        }   //else-if
        else{
            if(type.equals("2") && !isNodeLayerCore())
                System.out.println("ERROR! No Layer Core Node, can't make a no-read-only action.");
            else
                System.out.println("ERROR! Number inserted is not valid");
        }
        System.out.println("*********************************");

        if(logModified && isNodeLayerCore()){
            cl.eagerReplication();
            source = true;
            isOrigin = true;
            this.interrupt();
        }   //if
    }

    private void writeTransactionOnLogFile(int option){
        try{
            FileWriter writeLogs = new FileWriter(nodeName + "Logs.txt", true);
            if(option == 1)
                writeLogs.write(READONLY);
            else if(option == 2)
                writeLogs.write(NOREADONLY);
            writeLogs.close();
            System.out.println("Transaction added succesfully on log file node.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong adding the transaction on log file node.");
        }
    }

    private boolean isNodeLayerCore(){
        return nodeName.equals("A1") || nodeName.equals("A2") || nodeName.equals("A3");
    }

    private boolean isNodeLayer1(){
        return nodeName.equals("B1") || nodeName.equals("B2");
    }

    private boolean isNodeLayer2(){
        return nodeName.equals("C1") || nodeName.equals("C2");
    }

    private ArrayList<String> getToCoreLayer(){
        ArrayList<String> destiny = new ArrayList<>();

        if(nodeName.equals("A1")){
            destiny.add("A2");
            destiny.add("A3");
        }   //if
        else if(nodeName.equals("A2")){
            destiny.add("A1");
            destiny.add("A3");
        }   //else-if
        else if(nodeName.equals("A3")){
            destiny.add("A1");
            destiny.add("A2");
        }   //else-if

        return destiny;
    }

    public ArrayList<String> getToLayer1(){
        ArrayList<String> destiny = new ArrayList<>();

        if(nodeName.equals("A2"))
            destiny.add("B1");
        else if(nodeName.equals("A3"))
            destiny.add("B2");

        return destiny;
    }

    public ArrayList<String> getToLayer2(){
        ArrayList<String> destiny = new ArrayList<>();

        if(nodeName.equals("B2")){
            destiny.add("C1");
            destiny.add("C2");
        }   //if

        return destiny;
    }

    public String getSourceNodeLogInfo() {
        while(sourceNodeLogInfo.equals(""));
        return sourceNodeLogInfo;
    }

    public void setSourceNodeLogInfo(String sourceNodeLogInfo) {
        this.sourceNodeLogInfo = sourceNodeLogInfo;
    }

    public StringBuffer getSourceNodeLogInfoBuffer() {
        while(sourceNodeLogInfoBuffer == null);
        return sourceNodeLogInfoBuffer;
    }

    public void setSourceNodeLogInfoBuffer(StringBuffer sourceNodeLogInfoBuffer) {
        this.sourceNodeLogInfoBuffer = sourceNodeLogInfoBuffer;
    }
}
