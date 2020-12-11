package com.ivn.game.managers;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ivn.game.MainGame;
import com.ivn.game.models.MidBall;
import com.ivn.game.models.Ranking;
import com.ivn.game.models.Util;
import com.ivn.game.screens.RankingScreen;

import java.io.IOException;

import static com.ivn.game.screens.MultiPlayerScreen.*;

public class NetworkManager extends Listener.ThreadedListener {

    static private String url = "2.tcp.ngrok.io:10336";
    static public final int tcpPort = Integer.parseInt(url.split(":")[1]);
    static public final String address = url.split(":")[0];
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

            if(client.isConnected()) {
                client.sendTCP("client");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkManager(){
        super(new Listener());

        try {
            client = new Client();
            client.start();

            registrar();

            client.addListener(this);

            client.connect(timeOut, address, tcpPort);

            if(client.isConnected()) {
                client.sendTCP("db");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkManager(Ranking ranking){
        super(new Listener());

        try {
            client = new Client();
            client.start();

            registrar();

            client.addListener(this);

            client.connect(timeOut, address, tcpPort);

            if(client.isConnected()) {
                client.sendTCP(ranking);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connected (Connection connection) {
        System.out.println("Conectado al servidor.");
    }

    public void received (Connection connection, Object object) {
        if (object instanceof Integer) {
            if((Integer) object != 1)
                MidBall.enemyScore = (Integer)object;
        }

        if (object instanceof Ranking) {
            RankingScreen.updateList((Ranking) object);
        }

        if(object instanceof String){
            if(object.equals("rdy")){
                client.sendTCP(MidBall.myName);
                game.isMultiReady = true;
            }else
                MidBall.enemyName = (String) object;
        }

        if(object instanceof Boolean){
            if((Boolean) object)
                MidBall.myWinRounds++;
            else
                MidBall.enemyWinRounds++;

            HUD.setRounds(MidBall.myWinRounds,MidBall.enemyWinRounds);

            if(MidBall.myWinRounds >= 3 || MidBall.enemyWinRounds >= 3)
                endGame = true;
            else
                MidBall.nextRound();
        }

        if(object instanceof Util){
            if(((Util)object).pu.equals(Util.PowerUp.INK)) {
                if (ResourceManager.timer.isStarted) {
                    opacity = 1;
                }
            }else if(((Util)object).pu.equals(Util.PowerUp.VIBRATION))
                Gdx.input.vibrate(2000);
            else if(((Util)object).pu.equals(Util.PowerUp.FREEZED)){
                freezed = true;
                freezerTimer = ResourceManager.timer.seg;
            }
        }
    }

    public void disconnected (Connection connection) {
        System.out.println("Desconectado del servidor.");

        if(!client.isConnected()) {
            disconected = true;
        }
    }

    // This registers objects that are going to be sent or received over the network.
    public void registrar() {
        Kryo kryo = client.getKryo();
        kryo.register(Util.class);
        kryo.register(Util.PowerUp.class);
        kryo.register(Ranking.class);
        kryo.register(String[].class);
    }
}