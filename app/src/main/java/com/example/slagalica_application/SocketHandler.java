package com.example.slagalica_application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketHandler {

    static Socket socket;

    public static void setSocket(){

        try {
            socket = IO.socket("http://" + BuildConfig.IP_ADDR + ":3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Socket connected!");
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args[0].toString());
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Socket disconnect!");
            }
        });
    }

    public static Socket getSocket(){
        return socket;
    }
}
