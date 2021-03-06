package com.ivn.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.ivn.game.MainGame;
import com.ivn.game.managers.HUD;
import com.ivn.game.managers.NetworkManager;
import com.ivn.game.managers.ResourceManager;
import com.ivn.game.models.Ball;
import com.ivn.game.models.MidBall;
import com.ivn.game.models.Util;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.ivn.game.managers.ResourceManager.*;
import static com.ivn.game.models.Ball.Color.*;
import static com.ivn.game.models.MidBall.enemyWinRounds;
import static com.ivn.game.screens.SinglePlayerScreen.State.PAUSE;
import static com.ivn.game.screens.SinglePlayerScreen.State.PLAY;

public class MultiPlayerScreen implements Screen {

    public static boolean endGame;
    public static boolean disconected;

    SpriteBatch batch;

    public static Array<Ball> balls = new Array<>();
    MidBall midBall = new MidBall();

    public static boolean freezed = false;
    public static int freezerTimer = 0;

    Texture amarillo;
    Texture azul;
    Texture rojo;
    Texture green;

    // testing
    Array<Sprite> puntos = new Array<>();
    Texture punto = new Texture("punto.png");

    HUD hud;

    public static SinglePlayerScreen.State state;

    private Array<Sprite> arrayBackground1 = new Array<>();
    private Array<Sprite> arrayBackground2 = new Array<>();


    // Menús
    private Stage stage;
    private ImageButton pauseButton;

    private MainGame game;
    public MultiPlayerScreen(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        state = PAUSE;

        amarillo = new Texture("balls/yellowBall.png");
        azul = new Texture("balls/blueBall.png");
        rojo = new Texture("balls/redBall.png");
        green = new Texture("balls/greenBall.png");

        batch = new SpriteBatch();

        generarBolas();

        hud = new HUD();

        ResourceManager.countDown.start();

        generarMenu();
    }
    VisTable table;
    public void generarMenu(){
        if (!VisUI.isLoaded())
            VisUI.load(VisUI.SkinScale.X2);
        stage = new Stage();


        final Skin skin = new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json"));

        float pauseWidth = Gdx.graphics.getHeight() * 0.15f;

        table = new VisTable(true);

        pauseButton = new ImageButton(skin);
        pauseButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/pausa2.png")));
        pauseButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("HUD/pausa.png")));
        pauseButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getHeight() * 0.1f - pauseWidth/2, Gdx.graphics.getHeight() * 0.1f - pauseWidth/2 );
        pauseButton.setSize(pauseWidth,pauseWidth);
        pauseButton.getColor().a = 0.8f;
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {

                Dialog dialog = new Dialog("", table.getSkin()) {
                    public void result(Object obj) {
                        if((boolean)obj == true){
                            midBall.restart();
                            game.setScreen(new VictoryOrDefeatScreen(game));
                        }
                    }
                };

                TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
                //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
                textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

                dialog.text(" ¿ABANDONAR PARTIDA? ");
                dialog.button(" SALIR ",true,textButtonStyle);
                dialog.button(" CANCELAR ",false, textButtonStyle);
                dialog.show(stage);
            }
        });
        stage.addActor(pauseButton);
    }

    public void generarBolas(){
        SinglePlayerScreen.task = new Timer.Task() {
            public void run() {
                Vector2 pos;
                if (MathUtils.randomBoolean())
                    pos = new Vector2(-128, MathUtils.random(0, Gdx.graphics.getHeight()));
                else
                    pos = new Vector2(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight()));

                /////
                //pos = new Vector2(0,Gdx.graphics.getHeight()/2);

                //midball.level
                int rango = 2;
                if (midBall.level == 2) {
                    rango = 3; // 4 colores
                    midBall.changeColors(4);
                }

                int num = MathUtils.random(rango);
                Ball.Color color = BLUE;
                Texture tAle = azul;

                if (num == 0) {
                    tAle = amarillo;
                    color = YELLOW;
                }
                if (num == 1) {
                    tAle = azul;
                    color = BLUE;
                }
                if (num == 2) {
                    tAle = rojo;
                    color = RED;
                }
                if (num == 3) {
                    tAle = green;
                    color = GREEN;
                }

                System.out.println("saca bola");
                System.out.println(pos + " " + tAle + " " + color);

                int powerUp = MathUtils.random(3);

                if(state == PLAY && !freezed){
                    balls.add(new Ball(pos, tAle, color, powerUp));
                }
            }
        };

        Timer.schedule( SinglePlayerScreen.task, 1,1.5f, 300);
    }

    @Override
    public void render(float delta) {
        draw(delta);

        if(state == PLAY)
            update();

        //
        if(ResourceManager.countDown.isFinished && !ResourceManager.timer.isStarted){
            ResourceManager.timer.start();
            state = PLAY;
        }
    }

    public void background(){
        int width = Gdx.graphics.getWidth();

        if(arrayBackground1.size < 3){
            Sprite sprite = new Sprite(back1);
            sprite.setSize(width,width);
            sprite.setRegion(new TextureRegion(back1,0,0,back1.getWidth() ,back1.getHeight()));
            sprite.setBounds(0,0,width,width);

            if(arrayBackground1.size > 0)
                sprite.setPosition(0, arrayBackground1.get(arrayBackground1.size-1).getY()+width);
            else
                sprite.setPosition(0,0);

            arrayBackground1.add(sprite);
        }

        if(arrayBackground2.size < 3){
            Sprite sprite = new Sprite(back2);
            sprite.setSize(width,width);
            sprite.setRegion(new TextureRegion(back2,0,0,back2.getWidth() ,back2.getHeight()));
            sprite.setBounds(0,0,width,width);

            if(arrayBackground2.size > 0)
                sprite.setPosition(0, arrayBackground2.get(arrayBackground2.size-1).getY()+width);
            else
                sprite.setPosition(0,0);

            arrayBackground2.add(sprite);
        }
    }

    public void update(){

        for(Sprite back : arrayBackground1)
            if((back.getY()+back.getHeight()) < 0)
                arrayBackground1.removeIndex(0);
            else
                back.setPosition(back.getX(),back.getY()-5);

        for(Sprite back : arrayBackground2)
            if((back.getY()+back.getHeight()) < 0)
                arrayBackground2.removeIndex(0);
            else
                back.setPosition(back.getX(),back.getY()-3);

        background();

        for(Ball bola : balls)
            bola.mover(freezed);

        userInput();

        collisions();

        if(endGame) {
            game.setScreen(new VictoryOrDefeatScreen(game));
        }else if(disconected){
            disconected = false;
            state = PAUSE;

            Skin skin = new Skin(Gdx.files.internal("skins/star-soldier/skin/star-soldier-ui.json"));
            TextButton.TextButtonStyle textButtonStyle = skin.get(TextButton.TextButtonStyle.class);
            //TextButton.TextButtonStyle textButtonStyle = table.getSkin().get(TextButton.TextButtonStyle.class);
            textButtonStyle.font = assets.get("fonts/OpenSans-Semibold.ttf", BitmapFont.class);

            Dialog dialog = new Dialog("", table.getSkin()) {
                public void result(Object obj) {
                    midBall.restart();
                    game.setScreen(new VictoryOrDefeatScreen(game));
                }
            };
            dialog.text(" !Parece que hubo algún\n problema con la conexión! ");
            TextButton bt = new TextButton(" ACEPTAR ",textButtonStyle);
            dialog.button(bt);
            dialog.show(stage);

        }

        if(ResourceManager.timer.isFinished){
            if(midBall.myScore > midBall.enemyScore)
                MidBall.myWinRounds++;
            else
                enemyWinRounds++;


            if(MidBall.myWinRounds >= 3 || enemyWinRounds >= 3)
                endGame = true;
            else {
                MidBall.nextRound();
                HUD.setRounds(MidBall.myWinRounds, enemyWinRounds);
            }

            freezed = false;
        }

        if(freezerTimer-2 >= timer.seg || timer.seg >= 29)
            freezed = false;

    }

    boolean flag = false;
    int x = 0;
    public void userInput(){

        // INPUT
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                flag = false;

                if(Gdx.input.isTouched())
                    flag = true;
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                flag = true;
                x = screenX;
                return true;
            }

        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        if(flag)
            if(x > Gdx.graphics.getWidth()/2)
                midBall.rotateRight();
            else
                midBall.rotateLeft();

    }

    public void draw(float dt){

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for(Sprite back : arrayBackground1)
            back.draw(batch);
        for(Sprite back : arrayBackground2)
            back.draw(batch);

        batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());



        midBall.draw(batch);

        hud.draw(batch, true);

        for (Ball bola : balls)
            bola.draw(batch,freezed);

        for (Sprite punto : puntos)
            punto.draw(batch);


        // POWERUPS
        if(ResourceManager.timer.isStarted){
            inkScreen.setPosition(0,0);
            inkScreen.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            inkScreen.setColor(inkScreen.getColor().r,inkScreen.getColor().g,inkScreen.getColor().b,opacity);
            inkScreen.draw(batch);
            if(opacity > 0)
                opacity-= 0.005f;
        }
        batch.end();

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();

    }
    public static float opacity = 0;

    public void collisions(){
        // Colisiones

        for(Ball ball : balls)
            if(Intersector.overlaps(midBall.circle,ball.circle)) {

                if(sameColor(ball)){
                    if(prefs.getBoolean("sounds"))
                        sound.play(0.4f);

                    midBall.myScore += 10;
                    NetworkManager.client.sendTCP(midBall.myScore);

                    if(ball.hasPowerUp)
                        NetworkManager.client.sendTCP(new Util(ball.pu));
                }
                else{
                    System.out.println("wrong color");
                }

                balls.removeValue(ball,false);
            }
    }

    public boolean sameColor(Ball ball){
        // color de pixel de la pantalla


        //testing
        Vector2 pos = ball.position;
        Vector2 mid = new Vector2(Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight()/2);
        Vector2 dir = mid.sub(ball.position);
        dir.nor();
        dir.scl(Gdx.graphics.getWidth()*0.07f);
        pos.add(dir);

        Sprite sprite = new Sprite(punto);
        sprite.setPosition(pos.x - punto.getWidth()/2,pos.y - punto.getWidth()/2);
        //puntos.add(sprite);

        //if(puntos.size > 2)
        //   puntos.removeIndex(0);




        int pixel = ScreenUtils.getFrameBufferPixmap( MathUtils.round(pos.x ),MathUtils.round(pos.y ),1,1).getPixel(0,0);
        //int pixel = 0;
        Color midBallColor = new Color(pixel);

        System.out.println(midBallColor.r + " " + midBallColor.b + " " + midBallColor.g);
        int r = midBallColor.r > 0 ? 1 : 0;
        int g = midBallColor.g > 0 ? 1 : 0;
        int b = midBallColor.b > 0 ? 1 : 0;
        String col = null;

        System.out.println(r + " " + b + " " + g);

        if(r == 1 && g == 0 && b == 0) {
            col = "rojo";
            if (ball.color == RED)
                return true;
        }
        else if(r == 0 && g == 1 && b == 1) {
            col = "azul";
            if (ball.color == BLUE)
                return true;
        }
        else if(r == 1 && g == 1 && b == 0) {
            col = "amarillo";
            if (ball.color == YELLOW)
                return true;
        }
        else if(r == 0 && g == 1 && b == 0) {
            col = "verde";
            if (ball.color == GREEN)
                return true;
        }

        System.out.println(col);
        return false;
    }


    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}