package com.ivn.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.io.File;

public class ResourceManager {
    public static AssetManager assets = new AssetManager();


    // BARRA VIDA
    public static Texture textureScore;
    public static Texture textureContainerScore;
    public static NinePatch scoreBar;
    public static NinePatch container;


    // MidBall
    public static Texture midBall3;
    public static Texture midBall4;
    public static Texture midBall5;

    // TURNOS
    public static Texture textureTurnos;
    public static Texture textureTurnosW;


    // HUD
    public static BitmapFont score;
    public static BitmapFont myName;
    public static BitmapFont enemyName;
    public static BitmapFont timerFont;
    public static BitmapFont fpsPantalla;
    public static BitmapFont countDownFont;

    public static Sprite myRonda1;
    public static Sprite myRonda2;
    public static Sprite myRonda3;
    public static Sprite enemyRonda1;
    public static Sprite enemyRonda2;
    public static Sprite enemyRonda3;

    public static MyTimer timer;
    public static MyTimer countDown;

    public static GlyphLayout layout;


    public static FileHandleResolver resolver = new InternalFileHandleResolver();
    public static FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
    public static FreetypeFontLoader.FreeTypeFontLoaderParameter parameter2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();


    /**
     * Carga todos los recursos del juego
     */
    public static void loadAllResources() {
        //assets.load("my_texture_atlas.pack", TextureAtlas.class);

        //loadSounds();
        //loadMusics();

        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        assets.setLoader(BitmapFont.class, ".otf", new FreetypeFontLoader(resolver));



        // SCOREBAR
        textureScore = new Texture("HUD/vida.jpg");
        textureContainerScore = new Texture("HUD/container.jpg");

        scoreBar = new NinePatch(textureScore, 0, 0, 0, 0);
        container = new NinePatch(textureContainerScore, 5, 5, 5, 5);


        // MIDBALL

        midBall3 = new Texture(Gdx.files.internal("midBall3.png"),true);
        midBall3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        midBall4 = new Texture(Gdx.files.internal("midBall4.png"),true);
        midBall4.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        midBall5 = new Texture(Gdx.files.internal("midBall5.png"),true);
        midBall5.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);


        // TURNOS
        textureTurnos = new Texture(Gdx.files.internal("turnos.png"),true);
        textureTurnos.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        textureTurnosW = new Texture(Gdx.files.internal("turnos_win.png"),true);
        textureTurnosW.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);


        // HUD
        //
        layout = new GlyphLayout(); //dont do this every frame! Store it as member

        FreeTypeFontGenerator.setMaxTextureSize(4096);

        parameter.fontParameters.size = (int)(Gdx.graphics.getWidth() * 0.027f);
        parameter.fontParameters.color = Color.WHITE;
        parameter.fontParameters.borderWidth = (int)(Gdx.graphics.getWidth() * 0.003f);
        parameter.fontFileName = "fonts/LemonMilk.otf";

        // COUNTDOWN

        parameter2.fontParameters.size = (int)(Gdx.graphics.getWidth() * 0.45f);
        parameter2.fontParameters.borderWidth = (int)(Gdx.graphics.getWidth() * 0.005f);
        parameter2.fontParameters.color = Color.WHITE;
        parameter2.fontFileName = "fonts/OpenSans-Semibold.ttf";

        assets.load("fonts/OpenSans-Semibold.ttf", BitmapFont.class, parameter2);
        assets.load("fonts/LemonMilk.otf", BitmapFont.class, parameter);


        // TURNOS
        int width = (int)(Gdx.graphics.getWidth() * 0.025f);
        myRonda1 = new Sprite(textureTurnos);
        myRonda1.setSize(width,width);
        myRonda2 = new Sprite(textureTurnos);
        myRonda2.setSize(width,width);
        myRonda3 = new Sprite(textureTurnos);
        myRonda3.setSize(width,width);
        enemyRonda1 = new Sprite(textureTurnos);
        enemyRonda1.setSize(width,width);
        enemyRonda2 = new Sprite(textureTurnos);
        enemyRonda2.setSize(width,width);
        enemyRonda3 = new Sprite(textureTurnos);
        enemyRonda3.setSize(width,width);

    }

    /** Actualiza la carga de recursos */
    public static boolean update() {
        return assets.update();
    }

    /**
     * Carga los sonidos
     */
    public static void loadSounds() {
        assets.load("sounds" + File.separator + "game_begin.wav", Sound.class);
    }

    /**
     * Carga las músicas
     */
    public static void loadMusics() {
        assets.load("musics" + File.separator + "bso.mp3", Music.class);
    }

    /**
     * Obtiene una región de textura o la primera de una animación
     * @param name
     * @return
     */
    public static TextureRegion getRegion(String name, String texture) {
        return assets.get(texture, TextureAtlas.class).findRegion(name);
    }

    /**
     * Obtiene una región de textura determinada de las que forman una animación
     */
    /*
    public static TextureRegion getRegion(String name, int position) {
        return assets.get(TEXTURE_ATLAS, TextureAtlas.class).findRegion(name, position);
    }
*/
    /**
     * Obtiene todas las regiones de textura que forman una misma animación
     * @param name
     * @return
     *//*
    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assets.get(TEXTURE_ATLAS, TextureAtlas.class).findRegions(name);
    }
*/
    /**
     * Obtiene un sonido determinado
     */
    public static Sound getSound(String name) {
        return assets.get(name, Sound.class);
    }

    /**
     * Obtiene una música determinada
     */
    public static Music getMusic(String name) {
        return assets.get(name, Music.class);
    }
}