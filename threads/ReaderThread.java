package threads;
import java.io.BufferedReader;

import handlers.ClientHandler;
import servers.MultiServer;


public class ReaderThread implements Runnable {
    private ClientHandler source;
    private BufferedReader reader;
    
    public ReaderThread(ClientHandler source, BufferedReader reader) {
        
        this.source = source;
        this.reader = reader;
        
    }
    public void run() {

        try {
            String username = reader.readLine();
            System.out.println("User connected :" + username);
            source.setName(username);
            MultiServer.broadcast(source, username+" has joined the chat.");
            String msg;
            while((msg = reader.readLine())!=null) {
                if(msg.equals(";;")) {
                    break;
                }
                System.out.println(username+" : "+msg);
                MultiServer.broadcast(source, username+" : "+msg);
            }
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                System.out.println(source.getName()+" left");
                MultiServer.disconnect(source);
                reader.close();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

    }
}
