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
    public static int VidasRestantes = 3;

    public static void levelEndedCheck(){

        //TESTA SE PASSOU DE FASE PRA ZERAR OS CLIENTES SPAWNADOS
        if(NextLevelScreen.getPassouDeNivel() == 1 || getVidasRestantes() == 0 ){
            System.out.println("VALORES ZERADOS");
            setClientsSpawnados(0);
            setClientesAtendidos(0);
            setVidasRestantes(3);
            NextLevelScreen.setPassouDeNivel(0);
        }

        if(QuantidadeClientesDaFase <= ClientsSpawnados)  {

            //TESTA SE JA ATENDEU TODOS
            if(QuantidadeClientesDaFase == ClientesAtendidos){
                ChefBoom.getInstance().setScreen(new NextLevelScreen());
                setLevel(getLevel() + 1);

            }

            //CONSEGUI FAZER SEM O CONTADOR, MAS É BOM DEIXAR ELE COMENTADO AI

            //TESTA SE FALTA 1(SO ENTRA 1 VEZ)
           /* if(QuantidadeClientesDaFase == (ClientesAtendidos +1)){
                NivelAntesDoContador = getLevel();
                if(!ContadorIniciou){

                    ContadorIniciou = true;
                    System.out.println("CONTADOR INICIOU");

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            if(NivelAntesDoContador == getLevel()) {
                                ChefBoom.getInstance().setScreen(new NextLevelScreen());
                                setLevel(getLevel() + 1);
                            }
                        }
                    }, 40);

                }

           }*/

        }

    }




    public static int getClientesAtendidos() {
        return ClientesAtendidos;
    }

    public static void setClientesAtendidos(int clientesAtendidos) {
        ClientesAtendidos = clientesAtendidos;
        System.out.println("CLIENTES ATENDIDOS: " + ClientesAtendidos);
    }

    public static int getClientsSpawnados() {
        return ClientsSpawnados;
    }

    public static void setClientsSpawnados(int clientsSpawnados) {
        ClientsSpawnados = clientsSpawnados;
        System.out.println("CLIENTES SPAWNADOS: " + ClientsSpawnados );
    }

    public static int getQuantidadeClientesDaFase() {
        return QuantidadeClientesDaFase;
    }

    public static void setQuantidadeClientesDaFase(int quantidadeClientesDaFase) {
        QuantidadeClientesDaFase = quantidadeClientesDaFase;
        System.out.println("CLIENTES TOTAIS DA FASE" + QuantidadeClientesDaFase);

    }

    public static void setVidasRestantes(int vidasRestantes){
        VidasRestantes = vidasRestantes;

    }

    public static int getVidasRestantes(){
        return VidasRestantes;
    }
}
