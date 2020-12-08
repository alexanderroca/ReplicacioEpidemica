package Nodes.Client;

import java.io.*;
import java.util.concurrent.Callable;

public class ConsoleInputReadType extends Thread implements Callable<String> {

    private String line;

    public String call() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String input;
        do {
            try {
                // wait until we have data to complete a readLine()
                while (!br.ready()) {
                    Thread.sleep(200);

                    if(this.isInterrupted()){
                        return null;
                    }
                }
                input = br.readLine();
            } catch (InterruptedException e) {
                return null;
            }
        } while ("".equals(input));
        return input;
    }

    public void run(){

        line = "";
        try {
            line = call();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLine() {
        return line;
    }
}