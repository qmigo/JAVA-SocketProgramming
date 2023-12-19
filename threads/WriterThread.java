package threads;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import servers.MultiServer;


public class WriterThread implements Runnable {

    private static final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)); 
    
    public WriterThread() {

    }

    public void run() {
        try {
                       
            while(true) {
                String content = consoleReader.readLine();
                MultiServer.serverMessage(content);
                if(content.equals("exit")) {
                    break;
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();

        }
    }
}
