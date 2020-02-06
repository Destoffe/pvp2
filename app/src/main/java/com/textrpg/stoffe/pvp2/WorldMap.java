package com.textrpg.stoffe.pvp2;

import java.util.ArrayList;
import java.util.List;

public class WorldMap {

    String name;
    int position;
    List<Integer> items;;
    List<Integer> quests;
    List<Integer> monsters;;
    public WorldMap(){

    }
    public WorldMap(String name,int position){
        this.name = name;
        this.position = position;
        items = new ArrayList<Integer>();
        items.add(0);
        quests = new ArrayList<Integer>();
        quests.add(0);
        monsters = new ArrayList<Integer>();
        monsters.add(0);
    }
    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public List<Integer> getQuests() {
        return quests;
    }

    public void setQuests(List<Integer> quests) {
        this.quests = quests;
    }

    public List<Integer> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Integer> monsters) {
        this.monsters = monsters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



}
