package com.ivn.game.models;

public class Ranking {
    public String[] names = new String[5];
    public String[] scores = new String[5];
    public String[] dates = new String[5];

    public Ranking(){}

    public Ranking(String[] names, String[] scores, String[] dates){
        this.names = names;
        this.scores = scores;
        this.dates = dates;
    }
}