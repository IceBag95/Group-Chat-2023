package com.example.diet2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket;


    public Server(){
        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server Running");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void listenForClients(){
        while (!serverSocket.isClosed()){
            try {
                Socket socket = serverSocket.accept();
                ServerSideClientHandler clientHandler = new ServerSideClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }








    public static void main(String[] args){
        Server server = new Server();
        server.listenForClients();
    }
}
