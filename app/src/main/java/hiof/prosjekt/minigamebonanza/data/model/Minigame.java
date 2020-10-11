package hiof.prosjekt.minigamebonanza.data.model;

import java.util.ArrayList;

public class Minigame {
    private int uid;
    private String title;
    private String description;
    private int time;
    private ArrayList<Minigame> minigameList = new ArrayList<Minigame>();

    public Minigame(int uid, String title, String description, int time) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.time = time;
        minigameList.add(this);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<Minigame> getMinigameList(ArrayList<Minigame> list) {
        return list;
    }



}
