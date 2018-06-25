package com.mahmoud.practice.outbreak.core;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Bar extends Actor {
    private static final float DENSITY = 2f;
    private static final float RESTITUTION = 1f;
    private static final Color COLOR = Color.WHITE;
    private static final ShapeRenderer.ShapeType SHAPE_TYPE = ShapeRenderer.ShapeType.Filled;

    private final ShapeRenderer renderer;
    private final Body body;

    Bar(World world, ShapeRenderer renderer, float x, float y, float width, float height) {
        this.renderer = renderer;
        setBounds(x, y, width, height);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2,
                new Vector2(width / 2, height / 2), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.restitution = RESTITUTION;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.begin(SHAPE_TYPE);
        renderer.setColor(COLOR);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.end();
    }

    @Override
    public float getY() {
        return body.getPosition().y;
    }

    @Override
    public float getX() {
        return body.getPosition().x;
    }

    Body getBody() {
        return body;
    }
}
