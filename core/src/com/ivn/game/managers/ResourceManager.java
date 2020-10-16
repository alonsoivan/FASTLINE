package com.ivn.game.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.File;

public class ResourceManager {
    public static AssetManager assets = new AssetManager();


    // BARRA VIDA
    public static Texture textureVida;
    public static Texture textureContainerVida;
    public static NinePatch health;
    public static NinePatch container;


    /**
     * Carga todos los recursos del juego
     */
    public static void loadAllResources() {
        //assets.load("my_texture_atlas.pack", TextureAtlas.class);

        //loadSounds();
        //loadMusics();


        // BARRA VIDA
        textureVida = new Texture("HUD/vida.jpg");
        textureContainerVida = new Texture("HUD/container.jpg");

        health = new NinePatch(textureVida, 0, 0, 0, 0);
        container = new NinePatch(textureContainerVida, 5, 5, 5, 5);
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