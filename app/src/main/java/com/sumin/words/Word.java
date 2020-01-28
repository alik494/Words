package com.sumin.words;

public class Word {
private int id;
private String eng;
private String rus;
private int health;

    public Word( int id, String eng, String rus, int health) {
        this.id = id;
        this.eng = eng;
        this.rus = rus;
        this.health = health;
    }

    public int getId() {
        return id;
    }

    public String getEng() {
        return eng;
    }

    public String getRus() {
        return rus;
    }

    public int getHealth() {
        return health;
    }
}
