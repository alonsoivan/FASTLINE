package com.ivn.game.managers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ivn.game.MainGame;
import com.ivn.game.models.MidBall;

import java.io.IOException;

public class NetworkManager extends Listener.ThreadedListener {

    static public final int tcpPort = 18106;
    static public final int udpPort = 18106;
    static public final String address = "2.tcp.ngrok.io";
    static public final int timeOut = 6000;

    static public Client client;

    private MainGame game;

    public NetworkManager(MainGame game){
        super(new Listener());
        this.game = game;


        try {

            client = new Client();
            client.start();

            registrar();

            client.addListener(this);

            client.connect(timeOut, address, tcpPort);

            client.sendTCP(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void connected (Connection connection) {
        System.out.println("Conectado al servidor.");
    }

    public void received (Connection connection, Object object) {
        if (object instanceof Integer) {
            if((Integer) object == 20)
                MidBall.enemyScore += 20;
        }

        if(object instanceof Boolean){
            if((Boolean) object){
                game.isMultiReady = true;
            }
        }
    }

    public void disconnected (Connection connection) {
        System.out.println("Desconectado del servidor.");
        //System.exit(0);
    }

    // This registers objects that are going to be sent or received over the network.
    public void registrar() {
        //Kryo kryo = client.getKryo();
    }
}