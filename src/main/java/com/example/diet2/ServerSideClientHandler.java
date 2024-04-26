package com.example.diet2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerSideClientHandler implements Runnable {

    private static ArrayList<ServerSideClientHandler> clientsArrayList = new ArrayList<>();
    Socket socket;
    BufferedReader br;
    BufferedWriter bw;
    String username, password, message;



    public ServerSideClientHandler(Socket socket) {
        try {
            this.socket = socket;
            br = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            username = br.readLine();
            password = br.readLine();
            System.out.println("New client "+username+" just joined with password " + password);
            bw.write(username);
            bw.newLine();
            bw.flush();


            clientsArrayList.add(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void listenForMessages() {
        while (socket.isConnected()) {
            try {
                message = br.readLine(); //runs constantly and if no message is received it prints null. is that ok??
                if (message == null) {
                    closeEverything(socket, bw, br);
                    break;
                }
                if (message != null) {
                    System.out.println(username+ ": " + message);
                    broadcastMessages(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, bw, br);
                break;
            }

        }
    }

    @Override
    public void run() {
        listenForMessages();
    }

    public void broadcastMessages(String message){
        try{
            for( ServerSideClientHandler client : clientsArrayList){
                if (!client.username.equals(username)){
                    client.bw.write(message);
                    client.bw.newLine();
                    client.bw.flush();
                }
            }

        }
        catch (IOException e){
            //e.printStackTrace();
            closeEverything(socket, bw, br);
            System.out.println(username + "'s connection terminated.");
        }
    }

    public void removeClient(){
        if (socket.isClosed()){
            clientsArrayList.remove(this);
        }
    }

    public void closeEverything(Socket socket, BufferedWriter bw, BufferedReader br){
        try {
            if (socket != null) {
                socket.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (br != null) {
                br.close();
            }
            removeClient();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
