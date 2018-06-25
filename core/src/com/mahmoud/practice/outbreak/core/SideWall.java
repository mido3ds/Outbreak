package com.mahmoud.practice.outbreak.core;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

class SideWall extends Actor {
    private static final float DENSITY = 1f;

    SideWall(World world, int x, int height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        EdgeShape shape = new EdgeShape();
        shape.set(x, -.5f * height,
                x, 1.5f * height);

        Body body = world.createBody(bodyDef);
        body.createFixture(shape, DENSITY).setUserData(this);
    }
}
