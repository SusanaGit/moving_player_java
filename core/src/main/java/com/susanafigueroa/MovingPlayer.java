package com.susanafigueroa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MovingPlayer extends Game {

    // main component for driving things on the screen
    private SpriteBatch batch;
    private Texture image;
    private Texture turtle;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("Game BG.png");
        turtle = new Texture("turtle.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(image, 0, 0, screenWidth, screenHeight);
        batch.draw(turtle, (float) screenWidth /2, 0, 200, 200);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
