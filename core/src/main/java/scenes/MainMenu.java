package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;

import helpers.GameInfo;
import player.Player;

public class MainMenu implements Screen {
    private MovingPlayer movingPlayer;
    private Texture imageBackground;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Player turtle;

    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;

        imageBackground = new Texture("Game BG.png");

        turtle = new Player("turtle.png", (float) GameInfo.WIDTH / 2 , (float) GameInfo.HEIGHT / 2);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera);

        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();
        movingPlayer.getBatch().setProjectionMatrix(camera.combined);

        movingPlayer.getBatch().begin();
        movingPlayer.getBatch().draw(imageBackground, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        movingPlayer.getBatch().draw(turtle, turtle.getX(), turtle.getY(), turtle.getWidth(), turtle.getHeight());
        movingPlayer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        movingPlayer.getBatch().dispose();
        imageBackground.dispose();
        turtle.getTexture().dispose();
    }
}
