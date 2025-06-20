package br.com.gabriel.chefboom.world;

import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.screen.NextLevelScreen;
import br.com.gabriel.chefboom.screen.YouLoseScreen;
import com.badlogic.gdx.utils.Timer;

public class LevelEnded extends CurrentLevel {

    public static int QuantidadeClientesDaFase;
    public static int ClientsSpawnados = 0;
    public static int ClientesAtendidos = 0;
    public static boolean ContadorIniciou = false;
    public static int NivelAntesDoContador;

    public static void levelEndedCheck(){
        if(QuantidadeClientesDaFase == ClientsSpawnados)  {

            //TESTA SE JA ATENDEU TODOS
            if(ClientsSpawnados == ClientesAtendidos){
                ChefBoom.getInstance().setScreen(new NextLevelScreen());
                setLevel(getLevel() + 1);

            }

            //TESTA SE FALTA 1(SO ENTRA 1 VEZ)
            if(ClientsSpawnados == (ClientesAtendidos +1)){
                NivelAntesDoContador = getLevel();
                if(!ContadorIniciou){

                    ContadorIniciou = true;

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if(NivelAntesDoContador == getLevel()) {
                                ChefBoom.getInstance().setScreen(new NextLevelScreen());
                            }
                        }
                    }, 40);

                }
            }

        }

    }




    public static int getClientesAtendidos() {
        return ClientesAtendidos;
    }

    public static void setClientesAtendidos(int clientesAtendidos) {
        ClientesAtendidos = clientesAtendidos;
        LevelEnded.levelEndedCheck();
    }

    public static int getClientsSpawnados() {
        return ClientsSpawnados;
    }

    public static void setClientsSpawnados(int clientsSpawnados) {
        ClientsSpawnados = clientsSpawnados;

    }

    public static int getQuantidadeClientesDaFase() {
        return QuantidadeClientesDaFase;
    }

    public static void setQuantidadeClientesDaFase(int quantidadeClientesDaFase) {
        QuantidadeClientesDaFase = quantidadeClientesDaFase;

    }
}
