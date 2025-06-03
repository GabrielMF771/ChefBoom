package br.com.gabriel.jogoteste.resource;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {

    public static final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());

    //TEXTURAS PERSONAGEM
    @Asset public static final AssetDescriptor<Texture> testeFrente = new AssetDescriptor<Texture>("player/teste-frente.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> testeCostas = new AssetDescriptor<Texture>("player/teste-costas.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> testeDireita = new AssetDescriptor<Texture>("player/teste-direita.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> testeEsquerda = new AssetDescriptor<Texture>("player/teste-esquerda.png", Texture.class);

    //TEXTURA TILES
    @Asset public static final AssetDescriptor<Texture> barrier = new AssetDescriptor<Texture>("barrier.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> ground1 = new AssetDescriptor<Texture>("ground1.png", Texture.class);
    @Asset public static final AssetDescriptor<Texture> ground2 = new AssetDescriptor<Texture>("ground2.png", Texture.class);

    public static void load(){
        Texture.setAssetManager(manager);

        manager.load(Assets.class);
    }
}
