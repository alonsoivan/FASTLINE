package com.ivn.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.io.File;

public class ResourceManager {
    public static AssetManager assets = new AssetManager();

    // SOUNDS
    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/wizards.mp3"));
    public static Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/pop.mp3"));


    // BARRA VIDA
    public static Texture textureScore;
    public static Texture textureContainerScore;
    public static NinePatch scoreBar;
    public static NinePatch container;

    // Powerups
    public static Texture freezedBall;
    public static Texture icedBall;
    public static Texture inkBall;
    public static Texture vibrationBall;
    public static Sprite inkScreen;

    // MidBall
    public static Texture midBall3;
    public static Texture midBall4;
    public static Texture midBall5;

    // TURNOS
    public static Texture textureTurnos;
    public static Texture textureTurnosW;

    // HUD
    public static Sprite tactilIzq;
    public static Sprite tactilDer;
    public static BitmapFont scoreFont;
    public static BitmapFont myNameFont;
    public static BitmapFont enemyNameFont;
    public static BitmapFont timerFont;
    public static BitmapFont fpsFont;

    public static Sprite myRonda1;
    public static Sprite myRonda2;
    public static Sprite myRonda3;
    public static Sprite enemyRonda1;
    public static Sprite enemyRonda2;
    public static Sprite enemyRonda3;

    public static Texture back1;
    public static Texture back2;
    public static Texture bg = new Texture("HUD/bg.png");


    public static MyTimer timer;
    public static MyTimer countDown;

    public static Texture countDown1;
    public static Texture countDown2;
    public static Texture countDown3;

    public static GlyphLayout layout;

    public static FileHandleResolver resolver = new InternalFileHandleResolver();
    public static FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
    public static FreetypeFontLoader.FreeTypeFontLoaderParameter parameter2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();

    public static Preferences prefs = Gdx.app.getPreferences("fast_line");
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

        // Powerups
        freezedBall = new Texture("freezed.png");
        icedBall = new Texture("ice.png");
        vibrationBall = new Texture("vibration.png");
        inkBall = new Texture("ink.png");
        inkScreen = new Sprite(new Texture("inkScreen.png"));

        // MIDBALL

        midBall3 = new Texture(Gdx.files.internal("balls/midBall3.png"),true);
        midBall3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        midBall4 = new Texture(Gdx.files.internal("balls/midBall4.png"),true);
        midBall4.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        midBall5 = new Texture(Gdx.files.internal("balls/midBall5.png"),true);
        midBall5.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);


        // TURNOS
        textureTurnos = new Texture(Gdx.files.internal("HUD/turnos.png"),true);
        textureTurnos.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        textureTurnosW = new Texture(Gdx.files.internal("HUD/turnos_win.png"),true);
        textureTurnosW.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        // HUD
        layout = new GlyphLayout();

        FreeTypeFontGenerator.setMaxTextureSize(4096);

        parameter.fontParameters.size = (int)(Gdx.graphics.getWidth() * 0.027f);
        parameter.fontParameters.color = Color.WHITE;
        parameter.fontParameters.borderWidth = (int)(Gdx.graphics.getWidth() * 0.003f);
        parameter.fontFileName = "fonts/LemonMilk.otf";

        assets.load("fonts/LemonMilk.otf", BitmapFont.class, parameter);

        tactilIzq = new Sprite(new Texture("HUD/tactil.png"));
        tactilDer = new Sprite(new Texture("HUD/tactil.png"));
        tactilDer.flip(true,false);

        back1 = new Texture("HUD/back1.png");
        back2 = new Texture("HUD/back2.png");


        // COUNTDOWN
        countDown1 = new Texture("fonts/1.png");
        countDown2 = new Texture("fonts/2.png");
        countDown3 = new Texture("fonts/3.png");

        // SKINS

        parameter2.fontParameters.size = (int)(Gdx.graphics.getWidth() * 0.035f);
        parameter2.fontParameters.color = Color.WHITE;
        parameter2.fontFileName = "fonts/OpenSans-Semibold.ttf";
        parameter2.fontParameters.borderWidth = 0;

        assets.load("fonts/OpenSans-Semibold.ttf", BitmapFont.class, parameter2);
        assets.load("fonts/OpenSans-Semibold.ttf", BitmapFont.class, parameter);

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