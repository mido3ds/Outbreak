package com.mahmoud.practice.outbreak.core;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

class MainBar extends Actor {
    private static final String TAG = MainBar.class.getSimpleName();
    private static final float BAR_HEIGHT = 12f;
    private static final float BAR_WIDTH = 80f;
    private static final float CENTER_Y = 30f;
    private static final float DENSITY = 0f;
    private static final float RESTITUTION = 1f;
    private static final float MOVING_DISTANCE = 5f;
    private static final Color COLOR = Color.WHITE;

    private final int worldWidth;
    private final ShapeRenderer renderer;
    private Body body;

    MainBar(World world, int worldWidth, ShapeRenderer renderer) {
        this.worldWidth = worldWidth;
        this.renderer = renderer;
        setSize(BAR_WIDTH, BAR_HEIGHT);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2, new Vector2(getWidth() / 2, getHeight() / 2), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.restitution = RESTITUTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(getInitialPosition());

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
    }

    private Vector2 getInitialPosition() {
        return new Vector2(worldWidth / 2 - getWidth() / 2, CENTER_Y - getHeight() / 2);
    }

    @Override
    public float getX() {
        return body.getPosition().x;
    }

    @Override
    public void setX(float x) {
        body.setTransform(x, body.getPosition().y, body.getAngle());
    }

    @Override
    public float getY() {
        return body.getPosition().y;
    }

    @Override
    public void setY(float y) {
        body.setTransform(body.getPosition().x, y, body.getAngle());
    }

    @Override
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(COLOR);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.end();
    }

    void moveToLeft() {
        setX(Math.max(getX() - MOVING_DISTANCE, 0));
    }

    void moveToRight() {
        setX(Math.min(getX() + MOVING_DISTANCE, worldWidth - getWidth()));
    }
}
