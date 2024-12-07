package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;

import bodiesmap.BodiesMap;
import helpers.GameInfo;
import player.Player;

public class MainMenu implements Screen {
    private MovingPlayer movingPlayer;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private TiledMap tiledMap;
    private BodiesMap bodiesMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player turtle;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;

        // gravedad -9 eje y
        world = new World(new Vector2(0, -9.8f), true);

        // map configuration
        TmxMapLoader mapLoader = new TmxMapLoader(); // cargo el mapa con TmxMapLoader
        tiledMap = mapLoader.load("mapa.tmx"); // tiledMap contiene toda la info del mapa
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap); // renderiza el mapa en la pantalla

        camera = new OrthographicCamera(); // define el mundo que se mostrará en pantalla
        camera.setToOrtho(false, (float) GameInfo.WIDTH, (float) GameInfo.HEIGHT);
        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f, 0);
        camera.update();

        viewport = new StretchViewport((float) GameInfo.WIDTH, (float) GameInfo.HEIGHT, camera); // permite que el juego se vea bien en distintos dispositivos

        turtle = new Player(world, "turtle.png", (float) GameInfo.WIDTH/2 , (float) GameInfo.HEIGHT/2);

        bodiesMap = new BodiesMap();
        bodiesMap.createStaticBodiesFromMap(tiledMap, world);

        debugRenderer = new Box2DDebugRenderer();
    }

    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            turtle.getBody().applyLinearImpulse(
                new Vector2( -20f, 0), turtle.getBody().getWorldCenter(), true
                );
        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            turtle.getBody().applyLinearImpulse(
                new Vector2(+20f, 0), turtle.getBody().getWorldCenter(), true
            );
        }

        if (Gdx.input.isTouched()) {
            float valueTouchX = Gdx.input.getX();
            float screenWidth = Gdx.graphics.getWidth();

            if (valueTouchX < screenWidth / 2) {
                turtle.getBody().applyLinearImpulse(
                    new Vector2(-20f, 0), turtle.getBody().getWorldCenter(), true
                );
            } else {
                turtle.getBody().applyLinearImpulse(
                    new Vector2(+20f, 0), turtle.getBody().getWorldCenter(), true
                );
            }
        }

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        update(delta);

        turtle.updatePlayer();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        movingPlayer.getBatch().setProjectionMatrix(camera.combined);

        movingPlayer.getBatch().begin();
        movingPlayer.getBatch().draw(turtle, turtle.getX(), turtle.getY(), turtle.getWidth(), turtle.getHeight());
        movingPlayer.getBatch().end();

        debugRenderer.render(world, camera.combined);

        // world contiene la info de los cuerpos, contactos, fuerzas físicas
        // step -> actualiza posiciones, velocidades, fuerzas de los objetos
        // DeltaTime -> devuelve el tiempo desde el último fotograma
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
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
        debugRenderer.dispose();
    }
}
