package servers;
import java.net.*;
import java.util.*;

import handlers.ClientHandler;

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
