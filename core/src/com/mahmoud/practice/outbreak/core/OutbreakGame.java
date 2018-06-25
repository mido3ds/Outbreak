package com.mahmoud.practice.outbreak.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2D;

public class OutbreakGame extends Game {

    @Override
    public void create() {
        Box2D.init();
        setScreen(new PlayingScreen());
    }
}
