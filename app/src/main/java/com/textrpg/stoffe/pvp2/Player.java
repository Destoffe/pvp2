package com.textrpg.stoffe.pvp2;

public class Player {

    String username;
    int lvl;
    int str;
    int agi;
    int intl;
    int posX;
    int posY;

    public Player(){

    }

    public Player(String username,int lvl,int str,int agi,int intl,int posX,int posY){
        this.username = username;
        this.lvl = lvl;
        this.str = str;
        this.agi = agi;
        this.intl = intl;
        this.posX = posX;
        this.posY = posY;
    }

    public int getStr() {
        return str;
    }

    public int getAgi() {
        return agi;
    }

    public int getIntl() {
        return intl;
    }

    public int getLvl() {
        return lvl;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getUsername() {
        return username;
    }

    public String giveMeString(){
        return ("username: " + username + " lvl: " + lvl + " str: " + str + " agi:" + agi +" int: "+intl + " posX: "+posX + " posY:" + posY );
    }
}
