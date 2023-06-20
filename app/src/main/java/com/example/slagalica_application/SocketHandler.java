package com.example.slagalica_application;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    static Socket socket;

    public static void setSocket(){
        try {
            socket = IO.socket("http://192.168.100.56:3000");
            //IP racunara koji hostuje node server koji sluzi za komunikaciju dva igraca
            //ne zakucavati ovaj IP vec ga smestiti u local properties i pozivati ga preko promenljive
        } catch (Exception e){

        }
    }

    public static Socket getSocket(){
        return socket;
    }
}
