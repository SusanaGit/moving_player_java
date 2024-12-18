package com.susanafigueroa.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.susanafigueroa.MovingPlayer;
import com.susanafigueroa.bodiesmap.BodiesMap;
import com.susanafigueroa.bullet.Bullet;
import com.susanafigueroa.contact.ContactMovingPlayer;
import com.susanafigueroa.helpers.GameInfo;
import com.susanafigueroa.magicalobjects.chandelier.Chandelier;
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage;
import com.susanafigueroa.magicalobjects.chest.Chest;
import com.susanafigueroa.magicalobjects.chest.ChestManage;
import com.susanafigueroa.player.Player;
import com.susanafigueroa.timer.Timer;
import com.susanafigueroa.villains.Villain;
import com.susanafigueroa.villains.VillainManage;

public class MainMenu implements Screen {
    private MovingPlayer movingPlayer;
    private OrthographicCamera mapCamera;
    private OrthographicCamera box2DCamera;
    // HUD -> Head-Up Display
    private OrthographicCamera hudCamera;
    private StretchViewport viewport;
    private TiledMap tiledMap;
    private BodiesMap bodiesMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player cuteGirl;
    private VillainManage villainManage;
    private ChandelierManage chandelierManage;
    private ChestManage chestManage;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Timer timer;
    private ContactMovingPlayer contactMovingPlayer;

    public MainMenu(MovingPlayer movingPlayer) {
        this.movingPlayer = movingPlayer;

        // gravedad -9 eje y
        world = new World(new Vector2(0, -9.8f), true);

        // map configuration
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("map/mapa.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // camera for the map -> TiledMap -> pixels
        mapCamera = new OrthographicCamera();
        mapCamera.setToOrtho(false, (float) GameInfo.WIDTH, (float) GameInfo.HEIGHT);
        mapCamera.position.set(
            (GameInfo.WIDTH/2f) ,
            (GameInfo.HEIGHT/2f),
            0);
        mapCamera.update();

        // camera for the Box2D -> ppm
        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, (float) GameInfo.WIDTH / GameInfo.PPM, (float) GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(
            ((GameInfo.WIDTH/2f)/GameInfo.PPM),
            (GameInfo.HEIGHT/2f)/GameInfo.PPM,
            0);
        box2DCamera.update();

        // camera for HUD -> pixels
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        hudCamera.update();

        viewport = new StretchViewport((float) GameInfo.WIDTH, (float) GameInfo.HEIGHT, mapCamera); // permite que el juego se vea bien en distintos dispositivos

        timer = new Timer(new BitmapFont(),120f);

        cuteGirl = new Player(world, "player/player.png", (float) GameInfo.WIDTH/2 , (float) GameInfo.HEIGHT/2, timer);

        bodiesMap = new BodiesMap();
        bodiesMap.createStaticBodiesFromMap(tiledMap, world);

        villainManage = new VillainManage();
        villainManage.createStaticSpriteVillains(tiledMap, world);

        chandelierManage = new ChandelierManage();
        chandelierManage.createStaticSpriteChandeliers(tiledMap, world);

        chestManage = new ChestManage();
        chestManage.createStaticSpriteChests(tiledMap, world);

        contactMovingPlayer = new ContactMovingPlayer(timer, chandelierManage, chestManage, villainManage);
        world.setContactListener(contactMovingPlayer);

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {
    }

    private void updateCamera() {
        Vector2 positionPlayerCuteGirl = cuteGirl.getBody().getPosition(); // 4,8ppm x | 3,2ppm y

        // pixels map
        float mapWidthTiles = tiledMap.getProperties().get("width", Integer.class);
        float mapHeightTiles = tiledMap.getProperties().get("height", Integer.class);

        float mapWidthPixels = mapWidthTiles * 32;
        float mapHeightPixels = mapHeightTiles * 32;

        float cameraWidth = mapCamera.viewportWidth;
        float cameraHeight = mapCamera.viewportHeight;

        float cameraX = Math.max(cameraWidth/2, Math.min(positionPlayerCuteGirl.x * GameInfo.PPM,
            mapWidthPixels - cameraWidth/2));
        float cameraY = Math.max(cameraHeight/2, Math.min(positionPlayerCuteGirl.y * GameInfo.PPM,
            mapHeightPixels - cameraHeight/2));

        // pixels cam TiledMap
        mapCamera.position.set(cameraX, cameraY, 0);
        mapCamera.update();

        // ppm cam BoxD2
        box2DCamera.position.set(cameraX / GameInfo.PPM, cameraY / GameInfo.PPM, 0);
        box2DCamera.update();
    }

    @Override
    public void render(float delta) {

        cuteGirl.cleanBullets();

        cuteGirl.handleInput();

        updateCamera();

        chestManage.updateListChests();
        chandelierManage.updateListChandeliers();
        villainManage.updateListVillains();

        cuteGirl.updatePlayer(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        mapRenderer.setView(mapCamera);
        mapRenderer.render();

        debugRenderer.render(world, box2DCamera.combined);

        movingPlayer.getBatch().setProjectionMatrix(mapCamera.combined);

        movingPlayer.getBatch().begin();

        for (Bullet bullet: cuteGirl.getBullets()) {
            if (bullet != null) {
                bullet.updateBullet(delta);
                bullet.drawBulletAnimation(movingPlayer.getBatch());
            }
        }

        cuteGirl.drawPlayerAnimation(movingPlayer.getBatch());
        for(Villain villain: villainManage.getListVillains()) {
            villain.villainIsWalking(delta);
            villain.drawVillainAnimation(movingPlayer.getBatch());
        }
        for(Chest chest: chestManage.getListChests()) {
            chest.drawChest(movingPlayer.getBatch());
        }
        for(Chandelier chandelier: chandelierManage.getListChandeliers()) {
            chandelier.drawChandelier(movingPlayer.getBatch());
        }
        movingPlayer.getBatch().end();

        // HUD
        movingPlayer.getBatch().setProjectionMatrix(hudCamera.combined);
        movingPlayer.getBatch().begin();
        timer.runTimer(movingPlayer.getBatch());
        cuteGirl.messageGameOver(movingPlayer.getBatch());
        movingPlayer.getBatch().end();

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
        cuteGirl.getTexture().dispose();
        debugRenderer.dispose();
    }
}
