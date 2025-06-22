package br.com.gabriel.chefboom.resource;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    /* TODO:
        * Atualizar as texturas do player
        * Fazer o player segurando a comida na cabeça
        * Adicionar animação de andar do player e dos clientes
        * Adicionar animação de explosão do cliente
     */

    //TEXTURAS PERSONAGEM
    @Asset public static final AssetDescriptor<Texture> playerFrente = new AssetDescriptor<Texture>("player/player-frente.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerCostas = new AssetDescriptor<Texture>("player/player-costas.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerDireita = new AssetDescriptor<Texture>("player/player-direita.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerEsquerda = new AssetDescriptor<Texture>("player/player-esquerda.png", Texture.class);

    //TEXTURAS CLIENTES
    @Asset public static final AssetDescriptor<Texture> cliente1 = new AssetDescriptor<Texture>("npc/cliente1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente2 = new AssetDescriptor<Texture>("npc/cliente2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente3 = new AssetDescriptor<Texture>("npc/cliente3.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente4 = new AssetDescriptor<Texture>("npc/cliente4.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente5 = new AssetDescriptor<Texture>("npc/cliente5.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente6 = new AssetDescriptor<Texture>("npc/cliente6.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> explosao1 = new AssetDescriptor<Texture>("npc/explosion-01.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> explosao2 = new AssetDescriptor<Texture>("npc/explosion-02.png", Texture.class);

    //TEXTURA MAPA
    @Asset public static final AssetDescriptor<Texture> map = new AssetDescriptor<Texture>("mapa.png", Texture.class);

    // TEXTURAS DO HUD
    @Asset public static final AssetDescriptor<Texture> hudBackground = new AssetDescriptor<Texture>("hud/hudBackground.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> heart = new AssetDescriptor<Texture>("hud/heart.png", Texture.class);

    // TEXTURAS DO MENU
    @Asset public static final AssetDescriptor<Texture> iniciarBotao = new AssetDescriptor<>("menu/IniciarBotao.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> iniciarProximaFase = new AssetDescriptor<>("menu/Proximafase.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> TentarNovamente = new AssetDescriptor<>("menu/Tentarnovamente.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> Fase1 = new AssetDescriptor<>("menu/FASE1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> Fase2 = new AssetDescriptor<>("menu/FASE2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> Fase3 = new AssetDescriptor<>("menu/FASE3.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> ModoInfinito = new AssetDescriptor<>("menu/INFINITO.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> VoltarProMenu = new AssetDescriptor<>("menu/VOLTAR PRO MENU.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> Bloqueado = new AssetDescriptor<>("menu/Bloqueado.png", Texture.class);


    // TEXTURAS DOS ITENS
    @Asset public static final AssetDescriptor<Texture> burguer = new AssetDescriptor<>("item/burguer.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> fries = new AssetDescriptor<>("item/fries.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> soda = new AssetDescriptor<>("item/soda.png", Texture.class);

    // TEXTURAS DOS BLOCO INTERATIVOS
    @Asset public static final AssetDescriptor<Texture> plate = new AssetDescriptor<>("blocks/plate.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> trash = new AssetDescriptor<>("blocks/trash.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> grill = new AssetDescriptor<>("blocks/grill.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> friesMachine = new AssetDescriptor<>("blocks/friesmachine.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> sodaMachine = new AssetDescriptor<>("blocks/sodamachine.png", Texture.class);

    // SONS
    @Asset public static final AssetDescriptor<Music> gameMusic = new AssetDescriptor<>("music/game-music.mp3", Music.class);
    @Asset public static final AssetDescriptor<Sound> explosionSound = new AssetDescriptor<>("sound/explosion.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> readySound = new AssetDescriptor<>("sound/ready.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> gameoverSound = new AssetDescriptor<>("sound/youlose.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> wrongSound = new AssetDescriptor<>("sound/wrong.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> dashSound = new AssetDescriptor<>("sound/dash.mp3", Sound.class);

    public static void load(){
        Texture.setAssetManager(manager);
        manager.load(Assets.class); // Carrega tudo anotado com @Asset
    }
}
