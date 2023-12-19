import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class NewClient {
    private Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    String userName;

    public NewClient(String userName) {
        try {

            this.userName = userName;
            socket = new Socket("127.0.0.1", 5000);
            System.out.println("Connected to Server");

            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();
            this.writer = new PrintWriter(outputStream, true);

            Thread readerThread = new Thread(new ClientReaderThread(reader));
            Thread writerThread = new Thread(new ClientWriterThread(userName, writer));

            readerThread.start();
            writerThread.start();

            readerThread.join();
            writerThread.join();
            
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        try {
            System.out.print("Enter your username : ");
            BufferedReader readName = new BufferedReader(new InputStreamReader(System.in));
            String userName = readName.readLine();
            new NewClient(userName);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}
