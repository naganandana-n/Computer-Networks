import java.util.*;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket server = null;

        try{
            server = new ServerSocket(8009); // Create a new ServerSocket object, listening on port 8009
            server.setReuseAddress(true); 

            while(true){
                Socket client = server.accept(); // Accept incoming client connections
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress()); // Display a message indicating that a new client is connected, along with its IP address

                ClientHandler1 clientSock = new ClientHandler1(client); // Create a new ClientHandler1 object to handle the client connection
                new Thread(clientSock).start(); // Start a new thread to handle the client connection
            }
        } catch (IOException e){
            e.printStackTrace(); // Print any IO exceptions that occur
        } finally{
            // Close the server socket when the server is shutting down
            if (server != null){
                try{
                    server.close();
                } catch (IOException e){
                    e.printStackTrace(); // Print any exceptions that occur while closing the server socket
                }
            }
        }
    }    
}

