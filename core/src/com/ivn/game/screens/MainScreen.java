package com.ivn.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ivn.game.managers.ResourceManager.*;

public class MainScreen implements Screen {
    private Stage stage;
    private MainGame game;

    //public static Music musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));

    public static float PPM = 100;

    SpriteBatch batch;

    OrthographicCamera camera;

    World world;

    Box2DDebugRenderer debugRenderer;

    Body ground;
    Body paredIzq;
    Body paredDer;
    Body techo;

    Array<Body> bodies = new Array<>();

    Array<Sprite> sprites = new Array<>();

    VisImage titleImage;

    Texture bola;
    Texture bola2;
    Texture bola3;

    public MainScreen(MainGame game) {
        this.game = game;
    }

    Timer.Task task;
    private void generarBolas() {
        task = new Timer.Task() {
                public void run() {
                    bodies.add(createBody(0));

                    int num = MathUtils.random(2);
                    Sprite sprite = null;
                    if(num == 0)
                        sprite = new Sprite(bola);
                    if(num == 1)
                        sprite = new Sprite(bola2);
                    if(num == 2)
                        sprite = new Sprite(bola3);

                    //sprite.setSize(sprite.getWidth() /PPM , sprite.getHeight() /PPM );
                    sprite.setSize(ballWidth / PPM,ballWidth / PPM);
                    sprites.add(sprite);
                }
            };
        Timer.schedule( task, 0,0.4f, 60);
    }

    boolean lado = false;
    float ballWidth = Gdx.graphics.getHeight()*0.175f;
    private Body createBody( float rotation) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 /PPM, 0 /PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = 0.5f;

        CircleShape shape = new CircleShape();
        //shape.setRadius((bola.getWidth()/2)  /PPM);
        shape.setRadius((ballWidth/2) / PPM);

        fdef.shape = shape;

        body.createFixture(fdef).setUserData(this);

        Vector2 pos;
        int impulso;
        if (lado) {
            lado = !lado;
            pos = new Vector2(0 - bola.getWidth() / PPM, (Gdx.graphics.getHeight() - bola.getWidth()) / PPM);
            impulso = 10;
        }else {
            lado = !lado;
            pos = new Vector2(Gdx.graphics.getWidth() / PPM, (Gdx.graphics.getHeight() - bola.getWidth()) / PPM);
            impulso = -10;
        }

        body.setTransform(pos.x,pos.y, rotation);

        body.setLinearVelocity(new Vector2( impulso, body.getLinearVelocity().x ));

        return body;
    }

    @Override
    public void show() {
        if(prefs.getBoolean("sounds") && !music.isPlaying()){
            music.play();
            music.setVolume(0.2f);
            music.setLooping(true);
        }

        bola = new Texture("balls/yellowBall.png");
        bola2 = new Texture("balls/blueBall.png");
        bola3 = new Texture("balls/redBall.png");

        /*
        if(prefs.getBoolean("sound"))
            if (!musicaFondo.isPlaying())
                playMusicaFondo();
        */

        Box2D.init();

        batch = new SpriteBatch();

        camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth() /PPM, Gdx.graphics.getHeight() /PPM);
        camera.update();

        world = new World(new Vector2(0, -10), true);

        debugRenderer = new Box2DDebugRenderer();

        generarBolas();


        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);

        stage = new Stage();

        VisTable table = new VisTable(true);
        //table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json"));
        TextButton.TextButtonStyle textButtonStyle = skin.get("default", TextButton.TextButtonStyle.class);
        //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
        textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);


        float titleWidth = Gdx.graphics.getWidth()*0.35f;

        titleImage = new VisImage(new Texture("HUD/title.png"));
        titleImage.setSize(titleWidth,titleWidth);
        stage.addActor(titleImage);


        TextButton multiPlayerButton = new TextButton("2 PLAYER2",textButtonStyle);
        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                task.cancel();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadingMultiPlayerScreen(game));
                dispose();
            }
        });
        multiPlayerButton.getColor().a = 0.8f;

        TextButton singlePlayerButton = new TextButton("1 PLAYER",textButtonStyle);
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                task.cancel();
                game.setScreen(new SinglePlayerScreen(game));
                dispose();
            }
        });
        singlePlayerButton.getColor().a = 0.8f;

        TextButton rankingButton = new TextButton("RANKING", textButtonStyle);
        rankingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                task.cancel();
                game.setScreen(new RankingScreen(game));
                dispose();
            }
        });
        rankingButton.getColor().a = 0.8f;

        float width = Gdx.graphics.getWidth()*0.40f;
        float height = Gdx.graphics.getHeight()*0.25f;
        float pad = Gdx.graphics.getHeight()*0.005f;


        final ImageButton imageButton = new  ImageButton(skin);
        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               prefs.putBoolean("sounds",!prefs.getBoolean("sounds"));
               prefs.flush();

               if(prefs.getBoolean("sounds") && !music.isPlaying()){
                   music.play();
                   music.setVolume(0.2f);
                   imageButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen.png")));
                   imageButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen1.png")));

               }

               if(!prefs.getBoolean("sounds") && music.isPlaying()) {
                   music.stop();
                   imageButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen3.png")));
                   imageButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen2.png")));
               }
            }
        });
        if(!prefs.getBoolean("sounds")) {
            imageButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen3.png")));
            imageButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen2.png")));
        }else {
            imageButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen.png")));
            imageButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/volumen1.png")));
        }
        imageButton.getColor().a = 0.8f;

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(multiPlayerButton).center().width(width).height(height).pad(pad);
        table.row();
        table.add(singlePlayerButton).center().width(width).height(height).pad(pad);
        table.row();
        table.add(rankingButton).center().width(width).height(height).pad(pad,pad,0,pad);
        table.add(imageButton).right().width(height/1.5f).height(height/1.5f).pad(pad);

        float posY = Gdx.graphics.getHeight()/2;
        float posX = Gdx.graphics.getWidth()/4;
        table.setPosition(posX * 3,posY);

        titleImage.setPosition(posX - titleImage.getWidth()/2, posY - titleImage.getWidth()/2);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Step the physics world.
        world.step(1/60f,6,2);

        //debugRenderer.render(world,camera.combined);

        // open the sprite batch buffer for drawing
        batch.begin();

        batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // iterate through each of the fruits
        for (int i = 0; i < bodies.size; i++) {

            // get the physics body of the fruit
            Body body = bodies.get(i);

            // get the position of the fruit from Box2D
            Vector2 position = body.getPosition();

            // get the degrees of rotation by converting from radians
            float degrees = (float) Math.toDegrees(body.getAngle());

            // draw the fruit on the screen
            drawSprite(sprites.get(i), position.x - sprites.get(i).getWidth()/2, position.y - sprites.get(i).getWidth()/2, degrees);
        }

        // close the buffer - this is what actually draws the sprites
        batch.end();

        stage.act(dt);
        stage.draw();
    }

    private void drawSprite(Sprite sprite, float x, float y, float degrees) {
        sprite.setPosition(x, y);

        sprite.setRotation(degrees);

        sprite.draw(batch);
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width , height);

        batch.setProjectionMatrix(camera.combined);

        createGround();
    }

    private void createGround() {
        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(camera.viewportWidth, 1 /PPM);

        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);

        ground.setTransform(0 /PPM, -1 /PPM, 0);

        // techo

        techo = world.createBody(bodyDef);
        techo.createFixture(fixtureDef);

        techo.setTransform( 10 /PPM, camera.viewportHeight -1 /PPM, 0);

        // pared izq

        shape.setAsBox(1 /PPM, (Gdx.graphics.getHeight() - bola.getWidth()*2) / PPM);

        fixtureDef.shape = shape;
        paredIzq = world.createBody(bodyDef);
        paredIzq.createFixture(fixtureDef);

        paredIzq.setTransform( 0 /PPM, 0 /PPM, 0);


        // pared der

        fixtureDef.shape = shape;
        paredDer = world.createBody(bodyDef);
        paredDer.createFixture(fixtureDef);

        paredDer.setTransform(camera.viewportWidth - 1 /PPM, 10 /PPM, 0);

        shape.dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera los recursos de la escena
        stage.dispose();

        world.dispose();

        debugRenderer.dispose();
    }
}
