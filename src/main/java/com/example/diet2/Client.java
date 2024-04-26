package com.example.diet2;

import java.io.*;
import java.net.Socket;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {

    Socket socket;
    BufferedReader br;
    BufferedWriter bw;


    public Client(Socket socket){
        try {
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socket != null){
                socket.close();
            }
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeOnBuffer(String string){
        try{
            //TODO: OBJECTMAPPER to convert to json file and send it to server stringified
            bw.write(string);
            bw.newLine();
            bw.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public BufferedReader getBr(){
        return br;
    }

    public BufferedWriter getBw(){
        return bw;
    }


}
