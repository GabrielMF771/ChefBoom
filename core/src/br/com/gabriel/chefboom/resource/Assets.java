package br.com.gabriel.chefboom.resource;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;


public class Assets {

    private static Texture pixelTexture;

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    /* TODO:
        * Adicionar animação de andar dos clientes
     */

    //TEXTURAS PERSONAGEM
    @Asset public static final AssetDescriptor<Texture> playerEsquerda = new AssetDescriptor<Texture>("player/player-esquerda.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> playerIdle = new AssetDescriptor<Texture>("player/player-idle.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerWalk = new AssetDescriptor<Texture>("player/player-walk.png", Texture.class);
    //@Asset public static final AssetDescriptor<Texture> playerLift = new AssetDescriptor<Texture>("player/player-lift.png", Texture.class);
    //@Asset public static final AssetDescriptor<Texture> playerthrow = new AssetDescriptor<Texture>("player/player-throw.png", Texture.class);

    //TEXTURAS CLIENTES
    @Asset public static final AssetDescriptor<Texture> cliente1 = new AssetDescriptor<Texture>("npc/cliente1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente2 = new AssetDescriptor<Texture>("npc/cliente2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente3 = new AssetDescriptor<Texture>("npc/cliente3.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente4 = new AssetDescriptor<Texture>("npc/cliente4.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente5 = new AssetDescriptor<Texture>("npc/cliente5.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente6 = new AssetDescriptor<Texture>("npc/cliente6.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente7 = new AssetDescriptor<Texture>("npc/cliente7.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> cliente8 = new AssetDescriptor<Texture>("npc/cliente8.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado1 = new AssetDescriptor<Texture>("npc/clienteSentado1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado2 = new AssetDescriptor<Texture>("npc/clienteSentado2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado3 = new AssetDescriptor<Texture>("npc/clienteSentado3.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado4 = new AssetDescriptor<Texture>("npc/clienteSentado4.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado5 = new AssetDescriptor<Texture>("npc/clienteSentado5.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado6 = new AssetDescriptor<Texture>("npc/clienteSentado6.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado7 = new AssetDescriptor<Texture>("npc/clienteSentado7.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> clienteSentado8 = new AssetDescriptor<Texture>("npc/clienteSentado8.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> explosao1 = new AssetDescriptor<Texture>("npc/explosion-01.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> explosao2 = new AssetDescriptor<Texture>("npc/explosion-02.png", Texture.class);

    //TEXTURA MAPA
    @Asset public static final AssetDescriptor<Texture> map0 = new AssetDescriptor<Texture>("map/mapa0.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> map1 = new AssetDescriptor<Texture>("map/mapa1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> map2 = new AssetDescriptor<Texture>("map/mapa2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> map3 = new AssetDescriptor<Texture>("map/mapa3.png", Texture.class);

    // TEXTURAS DO HUD
    @Asset public static final AssetDescriptor<Texture> hudBackground = new AssetDescriptor<Texture>("hud/hudBackground.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> heart = new AssetDescriptor<Texture>("hud/heart.png", Texture.class);

    // TEXTURAS DO MENU
    @Asset public static final AssetDescriptor<Texture> menuBackground = new AssetDescriptor<>("menu/menuBackground.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoIniciar = new AssetDescriptor<>("menu/botaoIniciar.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoProximaFase = new AssetDescriptor<>("menu/botaoProximaFase.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoTentarNovamente = new AssetDescriptor<>("menu/botaoTentarNovamente.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoFase1 = new AssetDescriptor<>("menu/botaoFase1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoFase2 = new AssetDescriptor<>("menu/botaoFase2.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoFase3 = new AssetDescriptor<>("menu/botaoFase3.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoModoInfinito = new AssetDescriptor<>("menu/botaoFaseInfinito.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoVoltarProMenu = new AssetDescriptor<>("menu/botaoMenu.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoBloqueado = new AssetDescriptor<>("menu/botaoBloqueado.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> botaoConfig = new AssetDescriptor<>("menu/botaoConfig.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> botaoCreditos = new AssetDescriptor<>("menu/botaoCreditos.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> checkboxChecked = new AssetDescriptor<>("menu/checkboxChecked.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> checkboxUnchecked = new AssetDescriptor<>("menu/checkboxUnchecked.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> sliderBackground = new AssetDescriptor<>("menu/sliderBackgroundTexture.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> sliderFill = new AssetDescriptor<>("menu/sliderFillTexture.png", Texture.class);

    @Asset public static final AssetDescriptor<Texture> botaoTutorial = new AssetDescriptor<>("menu/botaoTutorial.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> telaTutorial = new AssetDescriptor<>("menu/tutorial.png", Texture.class);

    // TEXTURAS DOS ITENS
    @Asset public static final AssetDescriptor<Texture> burguer = new AssetDescriptor<>("item/burguer.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> fries = new AssetDescriptor<>("item/fries.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> soda = new AssetDescriptor<>("item/soda.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> donuts = new AssetDescriptor<>("item/donuts.png", Texture.class);

    // SONS
    @Asset public static final AssetDescriptor<Music> gameMusic = new AssetDescriptor<>("music/game-music.mp3", Music.class);
    @Asset public static final AssetDescriptor<Music> menuMusic = new AssetDescriptor<>("music/menu-music.mp3", Music.class);

    @Asset public static final AssetDescriptor<Sound> explosionSound = new AssetDescriptor<>("sound/explosion.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> readySound = new AssetDescriptor<>("sound/ready.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> trashSound = new AssetDescriptor<>("sound/trash.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> gameoverSound = new AssetDescriptor<>("sound/youlose.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> wrongSound = new AssetDescriptor<>("sound/wrong.mp3", Sound.class);
    @Asset public static final AssetDescriptor<Sound> dashSound = new AssetDescriptor<>("sound/dash.mp3", Sound.class);

    public static void load(){
        Texture.setAssetManager(manager);
        manager.load(Assets.class); // Carrega tudo anotado com @Asset
    }
}
