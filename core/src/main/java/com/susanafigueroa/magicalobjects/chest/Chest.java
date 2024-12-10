package com.susanafigueroa.magicalobjects.chest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.susanafigueroa.helpers.GameInfo;

public class Chest extends Sprite {

    private World world;
    private Body chestBody;

    public Chest (World world, String nameTexturePath, float x, float y) {
        super(new Texture(nameTexturePath));
        this.world = world;

        // ppm to pixels
        float xPixels = x * GameInfo.PPM;
        float yPixels = y * GameInfo.PPM;

        setSize(32f, 32f);

        setPosition(xPixels - getWidth() / 2, yPixels - getHeight() / 2);
    }

    public void addBody(Body chestBody) {
        this.chestBody = chestBody;
    }

    public Body getChestBody() {
        return chestBody;
    }

    public void drawChest(SpriteBatch batch) {
        batch.draw(
            getTexture(), getX(), getY(), getWidth(), getHeight()
        );
    }

}
