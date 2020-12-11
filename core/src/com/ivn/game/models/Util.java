package com.ivn.game.models;

public class Util {
    public enum PowerUp {
        INK, VIBRATION, FREEZED
    }

    public PowerUp pu;

    public Util(){}

    public Util(PowerUp pu){
        this.pu = pu;
    }

}
