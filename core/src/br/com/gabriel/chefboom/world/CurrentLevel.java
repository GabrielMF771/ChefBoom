package br.com.gabriel.chefboom.world;

import br.com.gabriel.chefboom.ChefBoom;

public class CurrentLevel {

    // 0 = NIVEL 1
    // 1 = NIVEL 2
    // 2 = NIVEL 3
    // 3 = MODO INFINITO
    public static int level = 0;
    public static int Maxlevel = 0;

    public static int getMaxlevel(){
        return Maxlevel;
    }

    public static int getLevel() {
        return level;
    }
    public static void setLevel(int levell) {
        if(ChefBoom.DEBUG){
            System.out.println("NIVEL: " + levell);
        }
        level = levell;

        if(level > Maxlevel)
            Maxlevel = level;
    }
}
