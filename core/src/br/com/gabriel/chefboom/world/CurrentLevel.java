package br.com.gabriel.chefboom.world;

public class CurrentLevel {

    public static int level = 0;

    public static int getLevel() {
        return level;
    }
    public static void setLevel(int levell) {
       if(level != 3)
        level = levell;
    }
}
