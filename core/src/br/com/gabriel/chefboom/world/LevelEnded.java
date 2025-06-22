package br.com.gabriel.chefboom.world;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.screen.GameScreen;
import br.com.gabriel.chefboom.screen.NextLevelScreen;
import br.com.gabriel.chefboom.screen.YouLoseScreen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;

public class LevelEnded extends CurrentLevel {

    public static int QuantidadeClientesDaFase;
    public static int ClientsSpawnados = 0;
    public static int ClientesAtendidos = 0;
    public static boolean ContadorIniciou = false;
    public static int NivelAntesDoContador;
    public static int VidasRestantes = 3;

    private static Music gameMusic = GameScreen.getGameMusic();

    public static void levelEndedCheck(){

        //TESTA SE PASSOU DE FASE PRA ZERAR OS CLIENTES SPAWNADOS
        if(NextLevelScreen.getPassouDeNivel() == 1 || getVidasRestantes() == 0 ){
            if(ChefBoom.DEBUG){
                System.out.println("VALORES ZERADOS");
            }
            setClientsSpawnados(0);
            setClientesAtendidos(0);
            setVidasRestantes(3);
            NextLevelScreen.setPassouDeNivel(0);
        }

        if(QuantidadeClientesDaFase <= ClientsSpawnados)  {

            //TESTA SE JA ATENDEU TODOS
            if(QuantidadeClientesDaFase == ClientesAtendidos){
                gameMusic.stop();
                ChefBoom.getInstance().setScreen(new NextLevelScreen());
                setLevel(getLevel() + 1);

            }
        }
    }

    public static int getClientesAtendidos() {
        return ClientesAtendidos;
    }

    public static void setClientesAtendidos(int clientesAtendidos) {
        ClientesAtendidos = clientesAtendidos;
        if(ChefBoom.DEBUG){
            System.out.println("CLIENTES ATENDIDOS: " + ClientesAtendidos);
        }
    }

    public static int getClientsSpawnados() {
        return ClientsSpawnados;
    }

    public static void setClientsSpawnados(int clientsSpawnados) {
        ClientsSpawnados = clientsSpawnados;
        if(ChefBoom.DEBUG){
            System.out.println("CLIENTES SPAWNADOS: " + ClientsSpawnados);
        }
    }

    public static int getQuantidadeClientesDaFase() {
        return QuantidadeClientesDaFase;
    }

    public static void setQuantidadeClientesDaFase(int quantidadeClientesDaFase) {
        QuantidadeClientesDaFase = quantidadeClientesDaFase;
        if(ChefBoom.DEBUG){
            System.out.println("CLIENTES TOTAIS DA FASE: " + QuantidadeClientesDaFase);
        }
    }

    public static void setVidasRestantes(int vidasRestantes){
        VidasRestantes = vidasRestantes;
    }

    public static int getVidasRestantes(){
        return VidasRestantes;
    }
}
