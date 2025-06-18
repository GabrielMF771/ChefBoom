package br.com.gabriel.chefboom.world;


import br.com.gabriel.chefboom.Config;
import br.com.gabriel.chefboom.ChefBoom;
import br.com.gabriel.chefboom.block.Block;
import br.com.gabriel.chefboom.dictionary.Blocks;
import br.com.gabriel.chefboom.entity.EntitiesFactory;
import br.com.gabriel.chefboom.entity.component.ClientComponent;
import br.com.gabriel.chefboom.entity.component.InteractiveBlock;
import br.com.gabriel.chefboom.entity.system.*;
import br.com.gabriel.chefboom.resource.Assets;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.ui.EntityTrackerMainWindow;

import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

// TODO - Fazer o sistema de seleção de nível e o método que verifica se o nível foi concluído

public class World extends CurrentLevel {

    private boolean paused = false;

    private final EntityTrackerMainWindow entityTrackerWindow;

    public static final int BG = 1;
    public static final int FG = 0;

    // Variável que guarda o tempo das máquinas de itens
    public static float FRIESTIME = 1f;
    public static float GRILLTIME = 1f;
    public static float SODATIME = 1f;

    //VETOR Q ARMAZENA O MAPA - OS TILES SÃO FORMADOS DEPENDENDO DO TAMANHO DA TELA
    //2 COLUNAS DE PROFUNDIDADE - FOREGOUND E BACKGROUND
    private final int [] [] [] map = new int [(int) ((Config.SCREEN_WIDTH * 0.8f)/Block.TILE_SIZE)] [(int) ((Config.SCREEN_HEIGHT * 0.8f)/Block.TILE_SIZE)] [2];

    private final Rectangle[][] collisionBoxes = new Rectangle[Config.SCREEN_WIDTH/Block.TILE_SIZE][Config.SCREEN_HEIGHT/Block.TILE_SIZE];

    private final com.artemis.World artemis;

    private final int player;

    private int[] clients = new int[20];

    private int item;

    //TODO - Todo bloco interativo adicionado precisa aumentar esse número
    private int[] interactiveBlock = new int[10];

    private int[][] clienteNivelFila = new int[4][3];

    private boolean debugCollisionEnabled = false;
    // tem q zerar esses clientesSpawnados toda vez q trocar de fase (os 3)
    public int clientesSpawnados = 0 ;

    public int clientesSpawnados1 = 0 ;

    public int clientesSpawnados2 = 0 ;

    public int chegouNoBalcao = 0;

// TODO - Fazer o sistema de seleção de nível

    //VAI RECEBER A QUANTIDADE DE CLIENTES QUE VÃO SPAWNAR EM CADA FILA - CADA INDICE É UMA FILA
    int[] spawn = new int[3];

    private final EntitiesFactory entitiesFactory;

    public World(OrthographicCamera camera){
        entitiesFactory = new EntitiesFactory();

        WorldConfigurationBuilder worldConfigBuilder = new WorldConfigurationBuilder()
                .with(new PlayerControllerSystem())
                .with(new ClientControllerSystem(this))
                .with(new StateSystem())
                .with(new MovementSystem(this))
                .with(new ItemSystem(this, entitiesFactory))
                .with(new ClientInteractionSystem(this))
                .with(new OrderSystem())
                .with(new SpriteRenderSystem(camera));

        if(ChefBoom.DEBUG){
            worldConfigBuilder.with(new CollisionDebugSystem(camera, this));
            entityTrackerWindow = new EntityTrackerMainWindow(false, false);
            worldConfigBuilder.with(new EntityTracker(entityTrackerWindow));
        } else {
            entityTrackerWindow = null;
        }

        WorldConfiguration config = worldConfigBuilder.build();
        artemis = new com.artemis.World(config);


        EntitiesFactory entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        // BLOCOS INTERATIVOS
        interactiveBlock[0] = entitiesFactory.createInteractiveBlock(artemis, 23 * Block.TILE_SIZE, 0 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE,0 , Assets.manager.get(Assets.plate));
        interactiveBlock[1] = entitiesFactory.createInteractiveBlock(artemis, 21 * Block.TILE_SIZE, 0 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE, 0, Assets.manager.get(Assets.plate));
        interactiveBlock[2] = entitiesFactory.createInteractiveBlock(artemis, 19 * Block.TILE_SIZE, 0 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE, 0,Assets.manager.get(Assets.plate));
        interactiveBlock[3] = entitiesFactory.createInteractiveBlock(artemis, 23 * Block.TILE_SIZE, 12 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE, 0, Assets.manager.get(Assets.plate));
        interactiveBlock[4] = entitiesFactory.createInteractiveBlock(artemis, 21 * Block.TILE_SIZE, 12 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE, 0, Assets.manager.get(Assets.plate));
        interactiveBlock[5] = entitiesFactory.createInteractiveBlock(artemis, 19 * Block.TILE_SIZE, 12 * Block.TILE_SIZE, InteractiveBlock.Type.PLATE, 0, Assets.manager.get(Assets.plate));

        interactiveBlock[6] = entitiesFactory.createInteractiveBlock(artemis, 26 * Block.TILE_SIZE, 0 * Block.TILE_SIZE, InteractiveBlock.Type.TRASH, 0, Assets.manager.get(Assets.trash));
        // TODO - Ajustar o timer de cada bloco
        interactiveBlock[7] = entitiesFactory.createInteractiveBlock(artemis, 30 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, InteractiveBlock.Type.FRIESMACHINE, FRIESTIME, Assets.manager.get(Assets.friesMachine));
        interactiveBlock[8] = entitiesFactory.createInteractiveBlock(artemis, 30 * Block.TILE_SIZE, 6 * Block.TILE_SIZE, InteractiveBlock.Type.GRILL, GRILLTIME, Assets.manager.get(Assets.grill));
        interactiveBlock[9] = entitiesFactory.createInteractiveBlock(artemis, 30 * Block.TILE_SIZE, 2 * Block.TILE_SIZE, InteractiveBlock.Type.SODAMACHINE, SODATIME, Assets.manager.get(Assets.sodaMachine));

        // PLAYER
        player = entitiesFactory.createPlayer(artemis, 16 * Block.TILE_SIZE, 6 * Block.TILE_SIZE);

       //CHAMADA METODO QUE GERA CLIENTE FIGURANTE
        generateStaticClients();


        //FILA por NIVEL
        //0
        clienteNivelFila[0][0] = 0;
        clienteNivelFila[0][1] = 4;
        clienteNivelFila[0][2] = 0;
        //1
        clienteNivelFila[1][0] = 2;
        clienteNivelFila[1][1] = 3;
        clienteNivelFila[1][2] = 0;
        //2
        clienteNivelFila[2][0] = 2;
        clienteNivelFila[2][1] = 3;
        clienteNivelFila[2][2] = 2;

        if(getLevel() == 3) {
            Random gerarNumClientes = new Random();

            spawn[0] = gerarNumClientes.nextInt(4) + 2;
            spawn[1] = gerarNumClientes.nextInt(4) + 2;
            spawn[2] = gerarNumClientes.nextInt(4) + 2;

            if(ChefBoom.DEBUG){
                System.out.println("spawn[0]: " + spawn[0]);
                System.out.println("spawn[1]: " + spawn[1]);
                System.out.println("spawn[2]: " + spawn[2]);
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void render() {
        // Apenas processa os sistemas de renderização
        for (BaseSystem system : artemis.getSystems()) {
            if (system instanceof SpriteRenderSystem) {
                system.process();
            }
        }
    }

    public void generateClients(World world) {

        //System.out.println("Gerando clientes");

        //CHAMANDO A FABRICA
        EntitiesFactory entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        switch (getLevel()){

            //NIVEL 1------------------------------------------------------------------------------------------------------

            case 0:
                //spawn guarda quantos bonecos spawnam na fila
                spawn[1] = clienteNivelFila[0][1];
                switch (clientesSpawnados){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 0, true,'r');
                        clientesSpawnados = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados < spawn[1]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 0, true,'r');
                            clientesSpawnados++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados< spawn[1]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(800);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 0, true,'r');
                                clientesSpawnados++;
                                break;
                            }
                        }
                }


                break;

            //NIVEL 2-----------------------------------------------------------------------------------------------------------

            case 1:

                spawn[0] = clienteNivelFila[1][0];
                switch (clientesSpawnados){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                        clientesSpawnados = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados < spawn[0]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                            clientesSpawnados++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados< spawn[0]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(800);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                                clientesSpawnados++;
                                break;
                            }
                        }
                }

                //FILA DO MEIO

                spawn[1] = clienteNivelFila[1][1];
                switch (clientesSpawnados1){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                        clientesSpawnados1 = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados1 < spawn[1]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                            clientesSpawnados1++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados1< spawn[1]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(800);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                                clientesSpawnados1++;
                                break;
                            }
                        }
                }



                break;

          //NIVEL 3 --------------------------------------------------------------------------------------------

            case 2:

                spawn[0] = clienteNivelFila[2][0];
                switch (clientesSpawnados){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                        clientesSpawnados = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados < spawn[0]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                            clientesSpawnados++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados< spawn[0]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(2500);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                                clientesSpawnados++;
                                break;
                            }
                        }
                }

                //FILA DO MEIO

                spawn[1] = clienteNivelFila[2][1];
                switch (clientesSpawnados1){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                        clientesSpawnados1 = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados1 < spawn[1]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                            clientesSpawnados1++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados1< spawn[1]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(2500);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                                clientesSpawnados1++;
                                break;
                            }
                        }
                }

                //FILA DE BAIXO

                spawn[2] = clienteNivelFila[2][2];
                switch (clientesSpawnados2){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                        clientesSpawnados2 = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados2 < spawn[2]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                            clientesSpawnados2++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados2< spawn[2]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(2500);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                                clientesSpawnados2++;
                                break;
                            }
                        }
                }


                break;

            //MODO INFINITO

            case 3:

                switch (clientesSpawnados){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                        clientesSpawnados = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados < spawn[0]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                            clientesSpawnados++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados< spawn[0]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(1000);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 10 * Block.TILE_SIZE, 0, true,'r');
                                clientesSpawnados++;
                                break;
                            }
                        }
                }

                //FILA DO MEIO

                switch (clientesSpawnados1){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                        clientesSpawnados1 = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados1 < spawn[1]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                            clientesSpawnados1++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados1< spawn[1]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(1000);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 7 * Block.TILE_SIZE, 1, true,'r');
                                clientesSpawnados1++;
                                break;
                            }
                        }
                }

                //FILA DE BAIXO

                switch (clientesSpawnados2){

                    case 0:
                        clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                        clientesSpawnados2 = 1;
                        break;

                    default:

                        int[] clientIds = world.getClients();
                        ComponentMapper<ClientComponent> mClient =
                                world.getArtemis().getMapper(ClientComponent.class);

                        ClientComponent client = mClient.get(clients[0]);
                        //SPAWNA SE NÃO TIVER COMPLETADO O NUMERO DE CLIENTES E TIVER SUMIDO 1
                        if(chegouNoBalcao > 1 && client == null && clientesSpawnados2 < spawn[2]){
                            clients[0] = entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                            clientesSpawnados2++;
                        }

                        if(client != null && client.inQueue) {
                            chegouNoBalcao = 2;
                        }

                        if(clientesSpawnados2< spawn[2]) {
                            //cria var random
                            Random random = new Random();
                            int num = random.nextInt(1000);
                            //se cair 1 ele spawna outro
                            if (num == 1) {
                                entitiesFactory.createClient(artemis, -2 * Block.TILE_SIZE, 4 * Block.TILE_SIZE, 2, true,'r');
                                clientesSpawnados2++;
                                break;
                            }
                        }
                }


                break;
        }
    }

    public void generateStaticClients(){

        // CLIENTES FIGURANTES
        EntitiesFactory entitiesFactory = new EntitiesFactory();
        artemis.inject(entitiesFactory);

        int[] vetor = new int[14];

        Random random = new Random();

        vetor[0] = random.nextInt(2);
        vetor[1] = random.nextInt(2);
        vetor[2] = random.nextInt(2);
        vetor[3] = random.nextInt(2);
        vetor[4] = random.nextInt(2);
        vetor[5] = random.nextInt(2);
        vetor[6] = random.nextInt(2);
        vetor[7] = random.nextInt(2);
        vetor[8] = random.nextInt(2);
        vetor[9] = random.nextInt(2);
        vetor[10] = random.nextInt(2);
        vetor[11] = random.nextInt(2);
        vetor[12] = random.nextInt(2);
        vetor[13] = random.nextInt(2);


        //CIMA DIREITA
        if(vetor[0] == 1)
        entitiesFactory.createClient(artemis, 8.5f * Block.TILE_SIZE, 12.7f * Block.TILE_SIZE, 0, false,'r');
        if(vetor[1] == 1)
        entitiesFactory.createClient(artemis, 10.5f * Block.TILE_SIZE, 12.7f * Block.TILE_SIZE, 0, false,'l');
        if(vetor[2] == 1)
        entitiesFactory.createClient(artemis, 9.5f * Block.TILE_SIZE, 11.7f * Block.TILE_SIZE, 0, false,'u');
        if(vetor[3] == 1)
        entitiesFactory.createClient(artemis, 9.5f * Block.TILE_SIZE, 13.7f * Block.TILE_SIZE, 0, false,'d');
        //CIMA ESQUERDA
        if(vetor[4] == 1)
        entitiesFactory.createClient(artemis, 1.5f * Block.TILE_SIZE, 12.7f * Block.TILE_SIZE, 0, false,'r');
        if(vetor[5] == 1)
        entitiesFactory.createClient(artemis, 3.5f * Block.TILE_SIZE, 12.7f * Block.TILE_SIZE, 0, false,'l');
        if(vetor[6] == 1)
        entitiesFactory.createClient(artemis, 2.5f * Block.TILE_SIZE, 11.7f * Block.TILE_SIZE, 0, false,'u');
        if(vetor[7] == 1)
        entitiesFactory.createClient(artemis, 2.5f * Block.TILE_SIZE, 13.7f * Block.TILE_SIZE, 0, false,'d');

        //BAIXO DIREITA
        if(vetor[8] == 1)
        entitiesFactory.createClient(artemis, 8.5f * Block.TILE_SIZE, 1.3f * Block.TILE_SIZE, 0, false,'r');
        if(vetor[9] == 1)
        entitiesFactory.createClient(artemis, 10.5f * Block.TILE_SIZE, 1.3f * Block.TILE_SIZE, 0, false,'l');
        if(vetor[10] == 1)
        entitiesFactory.createClient(artemis, 9.5f * Block.TILE_SIZE, 0.3f * Block.TILE_SIZE, 0, false,'u');
        if(vetor[11] == 1)
        entitiesFactory.createClient(artemis, 9.5f * Block.TILE_SIZE, 2.3f * Block.TILE_SIZE, 0, false,'d');

        //BAIXO MEIO
        if(vetor[12] == 1)
        entitiesFactory.createClient(artemis, 6 * Block.TILE_SIZE, 0.3f * Block.TILE_SIZE, 0, false,'u');
        if(vetor[13] == 1)
        entitiesFactory.createClient(artemis, 6 * Block.TILE_SIZE, 2.3f * Block.TILE_SIZE, 0, false,'d');

    }




    public void regenerate() {
        float startX = (getWidth() / 2.5f);
        int endX = getWidth();
        int startY = -1;
        int endY = getHeight() - 1;

        // Define a margem interna do retângulo central de barreiras (quanto maior, menor o retângulo)
        int innerRectMargin = 5;
        int innerStartX = (int) startX + innerRectMargin;
        int innerEndX = endX - 1 - innerRectMargin;
        int innerStartY = startY + innerRectMargin;
        int innerEndY = endY - 1 - innerRectMargin - 3;

        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                for (int l = 0; l < getLayers(); l++) {
                    Block block = null;

                    if (l == 0) { // FOREGROUND
                        if (x < startX) {
                            block = Blocks.AIR;
                        } else {
                            // Bordas externas
                            boolean isBorder =
                                    (x >= startX && x < startX + 1) || // borda esquerda
                                            (x >= endX - 2 && x < endX) || // borda direita
                                            (y >= startY && y < startY + 2) || // borda inferior
                                            (y >= endY - 5 && y < endY); // borda superior

                            // Retângulo interno preenchido de BARRIER
                            if (x >= innerStartX && x <= innerEndX &&
                                    y >= innerStartY && y <= innerEndY) {
                                block = Blocks.BARRIER;
                            } else if (isBorder) {
                                block = Blocks.BARRIER;
                            } else {
                                block = Blocks.AIR;
                            }
                        }
                    } else { // BACKGROUND
                        block = Blocks.AIR;
                    }
                    map[x][y][l] = Blocks.getIdByBlock(block);
                }
            }
        }

        // Gera 3 linhas de barreira no topo do mapa para evitar que o jogador entre debaixo da hud
        for (int y = getHeight() - 3; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                map[x][y][FG] = Blocks.getIdByBlock(Blocks.BARRIER);
            }
        }
        init();
    }


    public void init(){
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                collisionBoxes[x][y] = getBlock(x, y, FG).getTileRectangle(this, x, y);
            }
        }
    }

    public void update(float delta) {
        if(ChefBoom.DEBUG){
            if(Gdx.input.isKeyJustPressed(Input.Keys.C)){
                toggleCollisionDebug();
            }
        }

        if(!paused){
            artemis.setDelta(delta);
            artemis.process();
        }
    }


    // Alterna o modo de debug de colisão
    public void toggleCollisionDebug() {
        debugCollisionEnabled = !debugCollisionEnabled;
    }

    // Verifica se o debug de colisão está ativo
    public boolean isDebugCollisionEnabled() {
        return debugCollisionEnabled;
    }

    //RETORNA O BLOCO DEPENDENDO DA COORDENADA INSERIDA
    public Block getBlock(int x, int y, int layer){
        return Blocks.getBlockById(map[x][y][layer]);
    }

    public Block getBlock(float x, float y, float layer){
        return getBlock(worldToMap(x),worldToMap(y), layer);
    }

    //RETORNA SE O BLOCO É SÓLIDO OU NÃO
    public boolean isSolid(int x, int y){
        return isValid(x,y) && getBlock(x,y, FG).isSolid();
    }

    public boolean isSolid(float x, float y){
        return isSolid(worldToMap(x),worldToMap(y));
    }

    public Rectangle getTileRectangle(int x, int y){
        if(isValid(x,y)){
            return collisionBoxes[x][y];
        }

        return null;
    }

    //RETORNA A QUANTIDADE DE BLOCOS
    public void getTilesRectangle(float startX, float startY, float endX, float endY, Array<Rectangle> tileRectangles){
        getTilesRectangle(worldToMap(startX), worldToMap(startY), worldToMap(endX), worldToMap(endY), tileRectangles);
    }

    public void getTilesRectangle(int startX, int startY, int endX, int endY, Array<Rectangle> tileRectangles){
        tileRectangles.clear();

        Rectangle rectangle;

        for(int y = startY; y <= endY; y++){
            for(int x = startX; x <= endX; x++){
                rectangle = getTileRectangle(x, y);

                if(rectangle != null){
                    tileRectangles.add(rectangle);
                }
            }
        }
    }

    //RETORNA A LARGURA DO MUNDO
    public int getWidth(){
        return map.length;
    }

    //RETORNA A ALTURA DO MUNDO
    public int getHeight(){
        return map[0].length;
    }

    //RETORNA O LAYER DO MUNDO
    public int getLayers() {
        return map[0][0].length;
    }

    public void dispose() {
        artemis.dispose();
    }

    public boolean isValid(int x, int y){
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }


    public EntityTrackerMainWindow getEntityTrackerWindow() {
        return entityTrackerWindow;
    }

    public int getPlayer() {
        return player;
    }

    public int[] getClients(){
        return clients;
    }

    public int getItem(){
        return item;
    }

    public com.artemis.World getArtemis() {
        return artemis;
    }

    public static float mapToWorld(int mapCoordinate){
        return mapCoordinate * Block.TILE_SIZE;
    }

    public static int worldToMap(float worldCoordinate){
        return (int) (worldCoordinate / Block.TILE_SIZE);
    }

}
