import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class ClientWriterThread implements Runnable {
    private PrintWriter writer;

    public ClientWriterThread (String userName, PrintWriter writer) {
        this.writer = writer;
        writer.println(userName);
    }
    public void run() {
        try {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));            
            while(true) {
                String content = consoleReader.readLine();
                if (content.equals(";;")) {
                    break;
                }
                writer.println(content);
            }
            consoleReader.close();
        } catch (Exception e) {
            // e.printStackTrace();
            
        } finally {
            writer.close();
        }
    }
}
