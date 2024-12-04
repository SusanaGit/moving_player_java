package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;

import helpers.GameInfo;

public class MainMenu implements Screen {

    private MovingPlayer movingPlayer;

    // background
    private Texture imageBackground;

    // I want to use the WIDTH and the HEIGHT of the GameInfo to declare thing positions and sizes
    // OrthographicCamera -> defines 2D perspective of the game
    private OrthographicCamera camera;

    // StretchViewport -> to maintain a fixed aspect ratio
    private StretchViewport viewport;

    // I want to use the SpriteBatch from MovingPlayer class
    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;
        imageBackground = new Texture("Game BG.png");
    }

    // create method in the MovingPlayer class
    @Override
    public void show() {
        // initialize camera and viewport
        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera);

        // set the position of the camera in the middle of the screen
        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();
    }

    // render method in the MovingPlayer class
    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        movingPlayer.getBatch().setProjectionMatrix(camera.combined);

        movingPlayer.getBatch().begin();
        movingPlayer.getBatch().draw(imageBackground, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
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
    }
}
