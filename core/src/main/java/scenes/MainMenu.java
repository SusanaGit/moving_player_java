package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.susanafigueroa.MovingPlayer;

import helpers.GameInfo;

public class MainMenu implements Screen {

    private MovingPlayer movingPlayer;

    // background
    private Texture imageBackground;

    // I want to use the SpriteBatch from MovingPlayer class
    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;
        imageBackground = new Texture("Game BG.png");
    }

    // create method in the MovingPlayer class
    @Override
    public void show() {

    }

    // render method in the MovingPlayer class
    @Override
    public void render(float delta) {
        movingPlayer.getBatch().begin();
        movingPlayer.getBatch().draw(imageBackground, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        movingPlayer.getBatch().end();
    }

    // to ensure that our screen will always be the WIDTH and the HEIGHT
    @Override
    public void resize(int width, int height) {

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

    }
}
