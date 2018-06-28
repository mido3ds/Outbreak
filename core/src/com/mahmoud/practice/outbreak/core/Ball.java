package com.mahmoud.practice.outbreak.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

class Ball extends Actor {
    private static final String TAG = Ball.class.getSimpleName();
    private static final float RADIUS = 15f;
    private static final float RESTITUTION = 1.5f;
    private static final float DENSITY = .1f;
    private static final int SEGMENTS = 700;
    private static final float IMPULSE = 850000f;
    private static final Color COLOR = Color.WHITE;
    private static final float FRICTION = 3f;

    private final ShapeRenderer renderer;
    private final Body body;

    Ball(World world, MainBar mainBar, ShapeRenderer renderer) {
        this.renderer = renderer;

        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = RESTITUTION;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getInitialPoint(mainBar));

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
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

    private Vector2 getInitialPoint(MainBar bar) {
        return new Vector2(
                bar.getX() + 1 / 2f * bar.getWidth(),
                bar.getY() + bar.getHeight() + RADIUS + .6f);
    }

    float getRadius() {
        return RADIUS;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(COLOR);
        renderer.circle(getX(), getY(), getRadius(), SEGMENTS);
        renderer.end();
    }

    void impulse() {
        Gdx.app.log(TAG, "impulse coming at " + body.getPosition().toString());
        body.setLinearVelocity(new Vector2(IMPULSE, 0).setAngle(new Random().nextInt(180)));
    }
}
