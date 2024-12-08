package com.susanafigueroa.bodiesmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.susanafigueroa.helpers.GameInfo;

public class BodiesMap {

    private String nameCollisionLayer = "collision_layer";

    public void createStaticBodiesFromMap(TiledMap map, World world) {

        MapLayer collisionLayer = map.getLayers().get(nameCollisionLayer);
        if (collisionLayer == null) {
            Gdx.app.log("!!!!!", "LAYER NOT FOUND :(");
            return;
        }


        for (MapObject mapObject : collisionLayer.getObjects()) {
            Gdx.app.log("GOOD!!!!!", "LAYER FOUND :)");

            createStaticBody(mapObject, world);
        }
    }

    private void createStaticBody(MapObject mapObject, World world) {
        if (mapObject instanceof RectangleMapObject) {
            Gdx.app.log("GOOD!!!!!", "INSTANCEOF RectangleMapObject, im so happy");

            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            Rectangle rect = rectObject.getRectangle();

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                (rect.x + rect.width / 2)/GameInfo.PPM,
                (rect.y + rect.height / 2)/GameInfo.PPM
            );

            Body mapBody = world.createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (rect.width / 2)/GameInfo.PPM,
                (rect.height / 2)/GameInfo.PPM
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20f;

            mapBody.createFixture(fixtureDef);

            shape.dispose();
        }
    }
}
