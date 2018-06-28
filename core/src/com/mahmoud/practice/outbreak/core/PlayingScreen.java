package com.mahmoud.practice.outbreak.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class PlayingScreen implements Screen {
    private static final String TAG = PlayingScreen.class.getSimpleName();
    private static final float PHYS_TIME_STEP = 1 / 30f;
    private static final int PHYS_ITERATIONS = 5;
    private static final int SPEED = 3;

    private MainBar mainBar;
    private Ball ball;
    private World world;
    private Stage stage;
    private int height;
    private int width;
    private boolean started;
    private ShapeRenderer renderer;
    private Body toBeDestroyed;

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Bar bar;
                Object a = contact.getFixtureA().getUserData();
                Object b = contact.getFixtureB().getUserData();
                if (a instanceof Bar) {
                    bar = (Bar) a;
                    toBeDestroyed = bar.getBody();
                    bar.remove();
                }

                if (b instanceof Bar) {
                    bar = (Bar) b;
                    toBeDestroyed = bar.getBody();
                    bar.remove();
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
        renderer = new ShapeRenderer();
        toBeDestroyed = null;
        started = false;

        mainBar = new MainBar(world, width, renderer);
        ball = new Ball(world, mainBar, renderer);
        SideWall wall1 = new SideWall(world, 0, height);
        SideWall wall2 = new SideWall(world, width, height);
        Array<Bar> bars = createBars(5, 6, 30, 100, 10f);

        stage = new Stage();
        stage.addActor(mainBar);
        stage.addActor(ball);
        stage.addActor(wall1);
        stage.addActor(wall2);
        for (Bar bar : bars) {
            stage.addActor(bar);
        }
        Gdx.input.setInputProcessor(stage);
    }

    private Array<Bar> createBars(int r, int c, float heightPrec, float widthPrec, float margin) {
        Array<Bar> bars = new Array<Bar>();
        heightPrec = MathUtils.clamp(heightPrec, 0, 100);
        widthPrec = MathUtils.clamp(widthPrec, 0, 100);
        margin = MathUtils.clamp(margin, 0, width);

        float totalHeight = heightPrec / 100 * height;
        float totalWidth = widthPrec / 100 * width;
        float additionalHorizontalMargin = (100 - widthPrec) / 100 * width / 2;
        float additionalVerticalMargin = 40;
        float widthPerBar = (totalWidth - (c + 1) * margin) / c;
        float heightPerBar = (totalHeight - r * margin) / r;
        float x, y;

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                x = additionalHorizontalMargin + margin + j * (widthPerBar + margin);
                y = height - (additionalVerticalMargin + margin + i * (heightPerBar + margin));
                bars.add(new Bar(world, renderer, x, y, widthPerBar, heightPerBar));
            }
        }
        return bars;
    }

    @Override
    public void render(float delta) {
        clearScreen();
        destroyBuddies();
        listenForInput();

        if (hasWon()) {
            onWinning();
        } else if (hasFailed()) {
            onFailing();
        }
        for (int i = 0; i < SPEED; i++) {
            if (started) {
                world.step(PHYS_TIME_STEP, PHYS_ITERATIONS, PHYS_ITERATIONS);
                stage.act(PHYS_TIME_STEP);
            }
        }
        stage.draw();
    }

    private void onFailing() {
        Gdx.app.log(TAG, "you failed, try again");
        restart();
    }

    private boolean hasFailed() {
        return ball.getY() <= 0;
    }

    private void onWinning() {
        Gdx.app.log(TAG, "you won, yay");
        restart();
    }

    private boolean hasWon() {
        return ball.getY() >= height + ball.getRadius();
    }

    private void destroyBuddies() {
        if (toBeDestroyed != null) {
            world.destroyBody(toBeDestroyed);
            toBeDestroyed = null;
        }
    }

    private void listenForInput() {
        if (started) {
            float accY = Gdx.input.getAccelerometerY();

            if (Gdx.input.isKeyPressed(Keys.LEFT) || accY < -1) {
                mainBar.moveToLeft();
            }

            if (Gdx.input.isKeyPressed(Keys.RIGHT) || accY > 1) {
                mainBar.moveToRight();
            }
        } else {
            if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
                ball.impulse();
                started = true;
            }
        }

        if (Gdx.input.isKeyPressed(Keys.R)) {
            restart();
        }
    }

    private void restart() {
        dispose();
        show();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        renderer.dispose();
        world.dispose();
    }
}
