package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;

import helpers.GameInfo;

public class MainMenu implements Screen {
    private MovingPlayer movingPlayer;
    private Texture imageBackground;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Sprite turtle;

    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;

        imageBackground = new Texture("Game BG.png");
        turtle = new Sprite(new Texture("turtle.png"));
        turtle.setPosition((float) GameInfo.WIDTH/2, 0);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera);

        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();
    }

    // create method in the MovingPlayer class
    @Override
    public void show() {
    }

    // render method in the MovingPlayer class
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        movingPlayer.getBatch().setProjectionMatrix(camera.combined);

        movingPlayer.getBatch().begin();
        movingPlayer.getBatch().draw(imageBackground, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        turtle.setPosition(turtle.getX(), turtle.getY());
        movingPlayer.getBatch().draw(turtle, turtle.getX(), turtle.getY(), 200, 200);
        movingPlayer.getBatch().end();
    }

    // to ensure that our screen will always be the WIDTH and the HEIGHT
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    // stop rendering the sprites
    @Override
    public void pause() {

    }

    // if we hit the pause button, we are going to continue to do everything
    @Override
    public void resume() {

    }

    // the app will not be visible on the device
    @Override
    public void hide() {

    }

    // free resources manually and avoid memory problems
    @Override
    public void dispose() {
        movingPlayer.getBatch().dispose();
        imageBackground.dispose();
        turtle.getTexture().dispose();
    }
}
