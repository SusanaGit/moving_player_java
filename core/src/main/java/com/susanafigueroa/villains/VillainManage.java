package com.susanafigueroa.villains;

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

import java.util.ArrayList;
import java.util.List;

public class VillainManage {

    private List<Villain> listVillains = new ArrayList<>();
    private String nameCollisionLayer = "villains_layer";

    public List<Villain> getListVillains() {
        return listVillains;
    }

    public void createStaticSpriteVillains(TiledMap map, World world) {
        MapLayer collisionLayer = map.getLayers().get(nameCollisionLayer);
        if (collisionLayer == null) {
            Gdx.app.log("ERROR VILLAINS!!!!!", "LAYER NOT FOUND :(");
            return;
        }

        for (MapObject mapObject : collisionLayer.getObjects()) {
            Gdx.app.log("GOOD VILLAINS!!!!!", "LAYER FOUND :)");
            Body newVillainBody = createStaticVillainBodyFromMap(mapObject, world);

            Villain newVillain = new Villain(world, "villains/villain.png", newVillainBody.getPosition().x, newVillainBody.getPosition().y);
            newVillain.addBody(newVillainBody);
            listVillains.add(newVillain);
        }
    }

    private Body createStaticVillainBodyFromMap(MapObject villainObject, World world) {
        if (villainObject instanceof RectangleMapObject) {
            Gdx.app.log("GOOD!!!!!", "INSTANCEOF RectangleMapObject, im so happy");
            RectangleMapObject rectVillainObject = (RectangleMapObject) villainObject;
            Rectangle rectVillain = rectVillainObject.getRectangle();
            BodyDef bodyDefVillain = new BodyDef();
            bodyDefVillain.type = BodyDef.BodyType.StaticBody;
            bodyDefVillain.position.set(
                (rectVillain.x + rectVillain.width / 2)/ GameInfo.PPM,
                (rectVillain.y + rectVillain.height / 2)/GameInfo.PPM
            );
            Body villainBody = world.createBody(bodyDefVillain);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (rectVillain.width / 2)/GameInfo.PPM,
                (rectVillain.height / 2)/GameInfo.PPM
            );
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20f;
            villainBody.createFixture(fixtureDef);
            shape.dispose();

            return villainBody;
        } else {
            return null;
        }
    }
}
