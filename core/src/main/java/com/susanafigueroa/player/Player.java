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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.susanafigueroa.MovingPlayer;
import com.susanafigueroa.helpers.GameInfo;

import java.util.ArrayList;
import java.util.List;

public class Player extends Sprite {

    private World world;
    private Body body;
    private MovingPlayer movingPlayer;

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

    public Player(World world, String name, float x, float y) {
        super(new Texture(name));
        this.world = world;
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

        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void handleInput(float dt) {

        isWalkingRight = false;
        isWalkingLeft = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            isWalkingLeft = true;
            body.applyLinearImpulse(
                new Vector2(-3f, 0), body.getWorldCenter(), true
            );
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            isWalkingRight = true;
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
                body.applyLinearImpulse(
                    new Vector2(-3f, 0), body.getWorldCenter(), true
                );
            } else {
                isWalkingRight = true;
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
            isJumping = false;
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

        } else {
            currentTexture = defaultTexture;
        }

        batch.draw(
            currentTexture, getX(), getY(), getWidth(), getHeight()
        );
    }

    public Body getBody() {
        return this.body;
    }

}
