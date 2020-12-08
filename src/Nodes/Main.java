package Nodes;

import Nodes.Client.ClientCommand;

public class Main {

    public static volatile ClientCommand cc;

    public static void main(String args[]){


        switch(args[0]){
            case "A1":
                cc = new ClientCommand("A1");
                new A1();
                break;
            case "A2":
                cc = new ClientCommand("A2");
                new A2();
                break;
            case "A3":
                cc = new ClientCommand("A3");
                new A3();
                break;
            case "B1":
                cc = new ClientCommand("B1");
                new B1();
                break;
            case "B2":
                cc = new ClientCommand("B2");
                new B2();
                break;
            case "C1":
                cc = new ClientCommand("C1");
                new C1();
                break;
            case "C2":
                cc = new ClientCommand("C2");
                new C2();
                break;
            default:
                System.out.println("ERROR! This node doesn't exist.");
        }
    }
}
