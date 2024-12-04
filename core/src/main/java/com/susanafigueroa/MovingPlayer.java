package com.susanafigueroa;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import helpers.GameInfo;

public class MovingPlayer extends Game {

    // main component for driving things on the screen
    private SpriteBatch batch;
    private Texture image;
    private Sprite turtle;

    // I want to use the WIDTH and the HEIGHT of the GameInfo to declare thing positions and sizes
    // OrthographicCamera -> defines 2D perspective of the game
    private OrthographicCamera camera;

    // StretchViewport -> to maintain a fixed aspect ratio
    private StretchViewport viewport;

    @Override
    public void create() {
        // initialize camera and viewport
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera);

        // set the position of the camera in the middle of the screen
        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();

        batch = new SpriteBatch();
        image = new Texture("Game BG.png");
        turtle = new Sprite(new Texture("turtle.png"));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(image, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        batch.draw(turtle, (float) GameInfo.WIDTH /2, 0, 200, 200);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        turtle.getTexture().dispose();
    }
}
