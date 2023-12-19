import java.net.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

class ReaderThread implements Runnable {
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

class WriterThread implements Runnable {

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

class ClientHandler implements Runnable
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

public class MultiServer {

    private ServerSocket server;
    private static List<ClientHandler> clients = new ArrayList<>();

    public MultiServer () {
        try {
            server = new ServerSocket(5000);
            System.out.println("Server has spinned of at localhost:5000");
            
            while(true) {
                Socket clientSocket = server.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                System.out.println(clientHandler);
                clients.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            // e.printStackTrace();
            
        } finally {
            try {
                server.close();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

    }

    public static void broadcast(ClientHandler source, String message) {
        for(ClientHandler client: clients) {
            if (client.equals(source)) {
                continue;
            }
            client.sendMessage(message);;
        }
    }

    public static void serverMessage(String message) {
        for (ClientHandler client: clients) {
            client.sendMessage("Message from Server : "+message);
        }
    }

    public static void disconnect(ClientHandler client) {
        clients.remove(client);
        broadcast(client, client.getName() + "Left the chat");
    }

    public static void main(String[] args) {
        new MultiServer();
    }
}
