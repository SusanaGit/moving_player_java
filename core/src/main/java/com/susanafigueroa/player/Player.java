package com.susanafigueroa.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.susanafigueroa.bullet.Bullet;
import com.susanafigueroa.helpers.GameInfo;
import com.susanafigueroa.timer.Timer;

public class Player extends Sprite {

    private World world;
    private Body body;

    // animation
    private TextureAtlas playerAtlas;
    private Animation<TextureRegion> walkingRightAnimation;
    private Animation<TextureRegion> walkingLeftAnimation;
    private Animation<TextureRegion> jumpingAnimation;
    private Animation<TextureRegion> dyingAnimation;
    private float elapsedTime;
    private boolean isWalkingRight;
    private boolean isWalkingLeft;
    private boolean isJumping;
    private boolean isDying;

    // timer
    private Timer timer;

    // bullets
    private Pool<Bullet> bulletPool;
    private Array<Bullet> bullets;
    private Boolean right = false;
    private Boolean left = false;

    public Player(World world, String name, float x, float y, Timer timer) {
        super(new Texture(name));
        this.world = world;
        this.elapsedTime = 0f;
        this.timer = timer;
        setSize(40f, 40f);
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        createBody();

        // animation player
        playerAtlas = new TextureAtlas("player/playersatlas.atlas");

        // player jumping
        Array<TextureRegion> jumpingFrames = new Array<>();
        jumpingFrames.add(playerAtlas.findRegion("Jump (1)"));
        jumpingFrames.add(playerAtlas.findRegion("Jump (4)"));
        jumpingAnimation = new Animation<>(1f / 20f, jumpingFrames);

        // player walking right
        Array<TextureRegion> walkingRightFrames = new Array<>();
        walkingRightFrames.add(playerAtlas.findRegion("Walk (1)"));
        walkingRightFrames.add(playerAtlas.findRegion("Walk (8)"));
        walkingRightAnimation = new Animation<>(1f / 5f, walkingRightFrames);

        // player walking left
        Array<TextureRegion> walkingLeftFrames = new Array<>();

        TextureRegion walkLeft1 = new TextureRegion(playerAtlas.findRegion("Walk (1)"));
        walkLeft1.flip(true, false);
        walkingLeftFrames.add(walkLeft1);

        TextureRegion walkLeft2 = new TextureRegion(playerAtlas.findRegion("Walk (8)"));
        walkLeft2.flip(true, false);
        walkingLeftFrames.add(walkLeft2);
        walkingLeftAnimation = new Animation<>(1f / 5f, walkingLeftFrames);

        // player dying
        Array<TextureRegion> dyingFrames = new Array<>();
        dyingFrames.add(playerAtlas.findRegion("Dead (1)"));
        dyingFrames.add(playerAtlas.findRegion("Dead (6)"));
        dyingFrames.add(playerAtlas.findRegion("Dead (17)"));
        dyingFrames.add(playerAtlas.findRegion("Dead (30)"));
        dyingAnimation = new Animation<>(1f / 4f, dyingFrames);

        // bullets
        bullets = new Array<>();
        bulletPool = new Pool<Bullet>(2) {
            @Override
            protected Bullet newObject() {
                return new Bullet(world, "bullet/b1.png", 0, 0);
            }
        };
    }

    public Body getBody() {
        return this.body;
    }


    public Array<Bullet> getBullets() {
        return bullets;
    }

    public void createBody() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(
            (getX() + getWidth() / 2)/GameInfo.PPM,
            (getY() + getHeight() / 2)/GameInfo.PPM
        );

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (getWidth() / 2f)/GameInfo.PPM,
            (getHeight() / 2f)/GameInfo.PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 80f;
        fixtureDef.friction = 40f;

        Fixture playerFixture = body.createFixture(fixtureDef);

        playerFixture.setUserData(this);

        shape.dispose();
    }

    public void handleInput() {

        isWalkingRight = false;
        isWalkingLeft = false;

        if (!isDying) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                isWalkingLeft = true;
                right = false;
                left = true;
                body.applyLinearImpulse(
                    new Vector2(-3f, 0), body.getWorldCenter(), true
                );
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                isWalkingRight = true;
                left = false;
                right = true;
                body.applyLinearImpulse(
                    new Vector2(+3f, 0), body.getWorldCenter(), true
                );
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                isJumping = true;
                body.applyLinearImpulse(
                    new Vector2(0, +3f), body.getWorldCenter(), true
                );
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                isJumping = true;
                body.applyLinearImpulse(
                    new Vector2(0, -3f), body.getWorldCenter(), true
                );
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                shoot();
            }

            if (Gdx.input.isTouched()) {

                isWalkingRight = false;
                isWalkingLeft = false;

                float valueTouchX = Gdx.input.getX();
                float valueTouchY = Gdx.input.getY();
                float screenWidth = Gdx.graphics.getWidth();
                float screenHeight = Gdx.graphics.getHeight();

                if (valueTouchX < screenWidth / 2) {
                    isWalkingLeft = true;
                    right = false;
                    left = true;
                    body.applyLinearImpulse(
                        new Vector2(-3f, 0), body.getWorldCenter(), true
                    );
                } else {
                    isWalkingRight = true;
                    left = false;
                    right = true;
                    body.applyLinearImpulse(
                        new Vector2(+3f, 0), body.getWorldCenter(), true
                    );
                }

                if (valueTouchY > screenHeight / 2) {
                    isJumping = true;
                    body.applyLinearImpulse(
                        new Vector2(0, -3f), body.getWorldCenter(), true
                    );
                } else {
                    isJumping = true;
                    body.applyLinearImpulse(
                        new Vector2(0, +3f), body.getWorldCenter(), true
                    );
                }

                if (Gdx.input.justTouched()) {
                    shoot();
                }
            }
        }
    }

    public void updatePlayer(float dt) {

        this.setPosition(
            (body.getPosition().x * GameInfo.PPM) - getWidth() / 2,
            (body.getPosition().y * GameInfo.PPM) - getHeight() / 2
        );

        if (isWalkingLeft || isWalkingRight) {
            elapsedTime += dt;
        }

        if (body.getLinearVelocity().y == 0) {
            elapsedTime += dt;
            isJumping = false;
        }

        if (timer.getTotalTime() == 0) {
            if (!isDying) {
                isDying = true;
                elapsedTime = 0;
            }
            elapsedTime += dt;
        }
    }

    public void drawPlayerAnimation(SpriteBatch batch) {
        TextureRegion currentTexture;
        TextureRegion defaultTexture = new TextureRegion(getTexture());

        if (isWalkingRight) {
            currentTexture = walkingRightAnimation.getKeyFrame(elapsedTime, true);
        } else if (isWalkingLeft) {
            currentTexture = walkingLeftAnimation.getKeyFrame(elapsedTime, true);
        } else if (isJumping) {
            currentTexture = jumpingAnimation.getKeyFrame(elapsedTime, true);
        } else if (isDying) {
            currentTexture = dyingAnimation.getKeyFrame(elapsedTime, false);
        } else {
            currentTexture = defaultTexture;
        }

        batch.draw(
            currentTexture, getX(), getY(), getWidth(), getHeight()
        );
    }

    private void shoot() {

        Bullet bullet = bulletPool.obtain();
        float positionYBullet = (body.getPosition().y * GameInfo.PPM) - getHeight() / 2;
        bullet.setElapsedTime(0);

        if (right) {
            float positionXBulletRight = ((body.getPosition().x * GameInfo.PPM) - getWidth() / 2) + 80;
            bullet.getBody().setTransform(positionXBulletRight / GameInfo.PPM, positionYBullet / GameInfo.PPM, 0);
            bullet.getBody().setLinearVelocity(10f, 0);
        }

        if (left) {
            float positionXBulletLeft = ((body.getPosition().x * GameInfo.PPM) - getWidth() / 2) - 50;
            bullet.getBody().setTransform(positionXBulletLeft / GameInfo.PPM, positionYBullet / GameInfo.PPM, 0);
            bullet.getBody().setLinearVelocity(-10f, 0);
        }

        bullets.add(bullet);
    }

    public void cleanBullets() {
        for(int i = 0; i < bullets.size; i++) {
            Bullet bullet = bullets.get(i);
           if(bullet.isFinished()) {
               world.destroyBody(bullet.getBody());
               bullet.setElapsedTime(0);
               bulletPool.free(bullet);
               bullets.removeIndex(i);
           }
        }
    }
}
