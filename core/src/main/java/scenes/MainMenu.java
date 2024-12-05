package scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;

import helpers.GameInfo;
import player.Player;

public class MainMenu implements Screen {
    private MovingPlayer movingPlayer;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player turtle;

    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;

        // map configuration
        TmxMapLoader mapLoader = new TmxMapLoader(); // cargo el mapa con TmxMapLoader
        tiledMap = mapLoader.load("mapa.tmx"); // tiledMap contiene toda la info del mapa
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap); // renderiza el mapa en la pantalla

        camera = new OrthographicCamera(); // define el mundo que se mostrará en pantalla
        viewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, camera); // permite que el juego se vea bien en distintos dispositivos

        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();

        turtle = new Player("turtle.png", (float) GameInfo.WIDTH / 2 , (float) GameInfo.HEIGHT / 2);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        movingPlayer.getBatch().setProjectionMatrix(camera.combined);

        movingPlayer.getBatch().begin();
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
        tiledMap.dispose();
        turtle.getTexture().dispose();
    }
}
