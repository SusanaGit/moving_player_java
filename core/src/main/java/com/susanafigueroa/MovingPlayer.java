package com.susanafigueroa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import helpers.GameInfo;
import scenes.MainMenu;

public class MovingPlayer extends Game {

    // main component for driving things on the screen
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }


/*
    private Sprite turtle;






    @Override
    public void create() {


turtle = new Sprite(new Texture("turtle.png"));

        turtle.setPosition((float) GameInfo.WIDTH/2, 0);
    }

    @Override
    public void render() {


        turtle.setPosition(turtle.getX(), turtle.getY());



        batch.draw(turtle, turtle.getX(), turtle.getY(), 200, 200);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

        turtle.getTexture().dispose();
    }*/
}
