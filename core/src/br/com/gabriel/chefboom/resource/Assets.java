package br.com.gabriel.chefboom.resource;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    //TEXTURAS PERSONAGEM
    @Asset public static final AssetDescriptor<Texture> playerFrente = new AssetDescriptor<Texture>("player/player-frente.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerCostas = new AssetDescriptor<Texture>("player/player-costas.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerDireita = new AssetDescriptor<Texture>("player/player-direita.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> playerEsquerda = new AssetDescriptor<Texture>("player/player-esquerda.png", Texture.class);

    //TEXTURA MAPA
    @Asset public static final AssetDescriptor<Texture> map = new AssetDescriptor<Texture>("mapa.png", Texture.class);

    // TEXTURAS DO HUD
    @Asset public static final AssetDescriptor<Texture> hudbackground = new AssetDescriptor<Texture>("hudBackground.png", Texture.class);

    // TEXTURAS DO MENU
    @Asset public static final AssetDescriptor<Texture> iniciarBotao = new AssetDescriptor<>("IniciarBotao.png", Texture.class);


    public static void load(){
        Texture.setAssetManager(manager);
        manager.load(Assets.class); // Carrega tudo anotado com @Asset
    }
}
