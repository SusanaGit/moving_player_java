package com.susanafigueroa.villains;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.susanafigueroa.helpers.GameInfo;

public class Villain extends Sprite {

    private World world;
    private Body body;

    public Villain(World world, String nameTexturePath, float x, float y) {
        super(new Texture(nameTexturePath));
        this.world = world;

        // ppm to pixels
        float xPixels = x * GameInfo.PPM;
        Gdx.app.log("POSITION X VILLAIN MIN 0: ", Float.toString(xPixels));
        float yPixels = y * GameInfo.PPM;
        Gdx.app.log("POSITION Y VILLAIN MIN 0: ", Float.toString(yPixels));

        setSize(96f, 96f);

        setPosition(xPixels - getWidth() / 2, yPixels - getHeight() / 2);
    }

    public void addBody(Body villainBody) {
        this.body = villainBody;
    }

    public void updateVillain() {
        this.setPosition(
            (body.getPosition().x * GameInfo.PPM) - getWidth() / 2,
            (body.getPosition().y * GameInfo.PPM) - getHeight() / 2
        );
    }

}
