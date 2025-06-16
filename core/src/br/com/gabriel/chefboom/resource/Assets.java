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

    //TEXTURA MAPA
    @Asset public static final AssetDescriptor<Texture> map = new AssetDescriptor<Texture>("mapa.png", Texture.class);

    // TEXTURAS DO HUD
    @Asset public static final AssetDescriptor<Texture> hudBackground = new AssetDescriptor<Texture>("hud/hudBackground.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> heart = new AssetDescriptor<Texture>("hud/heart.png", Texture.class);

    // TEXTURAS DO MENU
    @Asset public static final AssetDescriptor<Texture> iniciarBotao = new AssetDescriptor<>("menu/IniciarBotao.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> iniciarProximaFase = new AssetDescriptor<>("menu/Proximafase.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> TentarNovamente = new AssetDescriptor<>("menu/Tentarnovamente.png", Texture.class);


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

    public static void load(){
        Texture.setAssetManager(manager);
        manager.load(Assets.class); // Carrega tudo anotado com @Asset
    }
}
