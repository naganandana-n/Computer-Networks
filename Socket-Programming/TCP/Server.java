// developing a multi threaded client server:
// single server handling multiple clients

// simple TCP client server: only 1 client and 1 server

/*

Execution instructions:

javac server.java
java server

- open another command prompt
javac client.java
java client <server addrs> 
eg: java client localhost 8009 
(give ip addrs and port no. 
- here ip addrs is localhost, as it on the same pc as client
- port no here is 8009
- local host ip addrs - 127.0.0.1)

 */

 import java.util.*;
 import java.io.*;
 import java.net.*;

 public class Server{
    public static void main(String[] args){
        ServerSocket server = null;
        try{
            server = new ServerSocket(8009);
            server.setReuseAddress(true);
            while(true){
                Socket client = server.accept();
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());
                ClientHandler1 clientSock = new ClientHandler1(client); // ClientHandler1
                new Thread(clientSock).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            if (server != null){
                try{
                    server.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
 } 

 