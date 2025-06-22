package br.com.gabriel.chefboom.world;

public class CurrentLevel {

    // 0 = NIVEL 1
    // 1 = NIVEL 2
    // 2 = NIVEL 3
    // 3 = MODO INFINITO
    public static int level = 2;
    public static int Maxlevel = 2;

    public static int getMaxlevel(){
        return Maxlevel;
    }

    public static int getLevel() {
        return level;
    }
    public static void setLevel(int levell) {
        System.out.println("NIVEL: " + levell);
        level = levell;

        if(level > Maxlevel)
            Maxlevel = level;
    }
}
