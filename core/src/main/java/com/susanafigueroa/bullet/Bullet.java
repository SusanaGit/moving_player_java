package com.susanafigueroa.bullet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.susanafigueroa.helpers.GameInfo;

public class Bullet extends Sprite {

    private World world;
    private Body body;

    // animation
    private Float elapsedTime;
    private TextureAtlas playerAtlas;
    private Animation<TextureRegion> shootingAnimation;

    public Bullet(World world, String name, float x, float y) {
        super(new Texture(name));
        this.world = world;
        this.elapsedTime = 0f;
        setSize(100f, 100f);
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        createBody();

        playerAtlas = new TextureAtlas("bullet/bulletatlas.atlas");

        // player shooting
        Array<TextureRegion> shootingFrames = new Array<>();
        shootingFrames.add(playerAtlas.findRegion("shoot1"));
        shootingFrames.add(playerAtlas.findRegion("shoot2"));
        shootingFrames.add(playerAtlas.findRegion("shoot3"));
        shootingFrames.add(playerAtlas.findRegion("shoot4"));
        shootingFrames.add(playerAtlas.findRegion("shoot7"));
        shootingAnimation = new Animation<>(1f / 20f, shootingFrames);
    }

    public Body getBody() {
        return this.body;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void createBody() {

        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(
            (getX() + getWidth() / 2) / GameInfo.PPM,
            (getY() + getHeight() / 2) / GameInfo.PPM
        );

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
            (getWidth() / 2f) / GameInfo.PPM,
            (getHeight() / 2f) / GameInfo.PPM
        );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.1f;

        Fixture bulletFixture = body.createFixture(fixtureDef);
        bulletFixture.setUserData(this);

        shape.dispose();
    }

    public void updateBullet(float delta) {
        elapsedTime += delta;
        this.setPosition(
            (body.getPosition().x * GameInfo.PPM) - getWidth() / 2,
            (body.getPosition().y * GameInfo.PPM) - getHeight() / 2
        );
    }

    public void drawBulletAnimation(SpriteBatch batch) {
        TextureRegion currentTexture = shootingAnimation.getKeyFrame(elapsedTime, true);

        batch.draw(
            currentTexture, getX(), getY(), getWidth(), getHeight()
        );
    }

    public boolean isFinished() {

        int timeBullet = 2;

        if (elapsedTime > timeBullet) {
            return true;
        }

        return false;
    }
}
