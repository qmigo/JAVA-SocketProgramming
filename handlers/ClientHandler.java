package handlers;
import java.net.*;

import servers.MultiServer;
import threads.ReaderThread;
import threads.WriterThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientHandler implements Runnable
{   
    private Socket clientSocket;
    BufferedReader reader;
    PrintWriter writer;
    Thread readerThread;
    Thread writerThread;

    private String name;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run() {

        try {

            reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            OutputStream outputStream = this.clientSocket.getOutputStream();
            writer = new PrintWriter(outputStream, true);

            readerThread = new Thread(new ReaderThread(this, reader));
            writerThread = new Thread(new WriterThread());

            readerThread.start();
            writerThread.start();
            
            readerThread.join();
            writerThread.join();


            this.clientSocket.close();

        } catch (Exception e) {
            // e.printStackTrace();

        } finally {
            try {
                clientSocket.close();
                MultiServer.disconnect(this);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String toString() {
        InetAddress inetAddress = clientSocket.getInetAddress();
        String clientAddressString = inetAddress.getHostAddress();
        return clientAddressString;
    }
}
