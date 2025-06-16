package br.com.gabriel.chefboom.world;

public class CurrentLevel {

    // 0 = NIVEL 1
    // 1 = NIVEL 2
    // 2 = NIVEL 3
    // 3 = MODO INFINITO
    public static int level = 3;

    public static int getLevel() {
        return level;
    }
    public static void setLevel(int levell) {
       if(level != 3)
        level = levell;
    }
}
