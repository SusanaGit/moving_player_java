package com.susanafigueroa.villains;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.susanafigueroa.helpers.GameInfo;

public class Villain extends Sprite {

    private World world;
    private Body body;

    // animation
    private TextureAtlas villainAtlas;
    private Animation<TextureRegion> walkingVillainRightAnimation;
    private Animation<TextureRegion> walkingVillainLeftAnimation;
    private float elapsedTime;
    private boolean isVillainWalkingRight;
    private boolean isVillainWalkingLeft;
    private Boolean villainIsDead;
    private final float initialVillainXPosition;

    public Villain(World world, String nameTexturePath, float x, float y) {
        super(new Texture(nameTexturePath));
        this.world = world;
        this.villainIsDead = false;

        // ppm to pixels
        float xPixels = x * GameInfo.PPM;

        // save initial x position
        initialVillainXPosition = xPixels;
        float yPixels = y * GameInfo.PPM;

        setSize(96f, 96f);

        setPosition(xPixels - getWidth() / 2, yPixels - getHeight() / 2);

        // starting walking left
        isVillainWalkingRight = false;
        isVillainWalkingLeft = true;

        // animation villain
        villainAtlas = new TextureAtlas("villains/villainsatlas.atlas");

        // villain walking right
        Array<TextureRegion> walkingVillainRightFrames = new Array<>();
        walkingVillainRightFrames.add(villainAtlas.findRegion("Walk (1)"));
        walkingVillainRightFrames.add(villainAtlas.findRegion("Walk (3)"));
        walkingVillainRightFrames.add(villainAtlas.findRegion("Walk (6)"));
        walkingVillainRightFrames.add(villainAtlas.findRegion("Walk (7)"));
        walkingVillainRightAnimation = new Animation<>(1f / 5f, walkingVillainRightFrames);

        // villain walking left
        Array<TextureRegion> walkingVillainLeftFrames = new Array<>();

        TextureRegion walkVillainLeft1 = new TextureRegion(villainAtlas.findRegion("Walk (1)"));
        walkVillainLeft1.flip(true, false);
        walkingVillainLeftFrames.add(walkVillainLeft1);

        TextureRegion walkVillainLeft2 = new TextureRegion(villainAtlas.findRegion("Walk (3)"));
        walkVillainLeft2.flip(true, false);
        walkingVillainLeftFrames.add(walkVillainLeft2);

        TextureRegion walkVillainLeft3 = new TextureRegion(villainAtlas.findRegion("Walk (6)"));
        walkVillainLeft3.flip(true, false);
        walkingVillainLeftFrames.add(walkVillainLeft3);

        TextureRegion walkVillainLeft4 = new TextureRegion(villainAtlas.findRegion("Walk (7)"));
        walkVillainLeft4.flip(true, false);
        walkingVillainLeftFrames.add(walkVillainLeft4);

        walkingVillainLeftAnimation = new Animation<>(1f / 5f, walkingVillainLeftFrames);

    }

    public void addBody(Body villainBody) {
        this.body = villainBody;
    }

    public void updateVillainPositionBody() {
        this.setPosition(
            (body.getPosition().x * GameInfo.PPM) - getWidth() / 2,
            (body.getPosition().y * GameInfo.PPM) - getHeight() / 2
        );
    }

    public void villainIsWalking(float delta ) {

        if (villainIsDead) {
            return;
        }

        // position villain x ppm
        float positionVillainX = body.getPosition().x * GameInfo.PPM;

        if (isVillainWalkingLeft && positionVillainX <= initialVillainXPosition - 64) {
            isVillainWalkingLeft = false;
            isVillainWalkingRight = true;
        } else if (isVillainWalkingRight && positionVillainX >= initialVillainXPosition) {
            isVillainWalkingRight = false;
            isVillainWalkingLeft = true;
        }

        float velVillainX = 0f;

        if (isVillainWalkingLeft) {
            velVillainX = -1f;
        } else if (isVillainWalkingRight) {
            velVillainX = +1f;
        }

        body.setLinearVelocity(velVillainX, body.getLinearVelocity().y);

        // update position villainbody
        updateVillainPositionBody();

        elapsedTime += delta;

    }

    public void drawVillainAnimation(SpriteBatch batch) {
        TextureRegion currentTexture;
        TextureRegion defaultTexture = new TextureRegion(getTexture());

        if (isVillainWalkingRight) {
            currentTexture = walkingVillainRightAnimation.getKeyFrame(elapsedTime, true);
        } else if (isVillainWalkingLeft) {
            currentTexture = walkingVillainLeftAnimation.getKeyFrame(elapsedTime, true);
        } else {
            currentTexture = defaultTexture;
        }

        batch.draw(
            currentTexture, getX(), getY(), getWidth(), getHeight()
        );
    }

}
