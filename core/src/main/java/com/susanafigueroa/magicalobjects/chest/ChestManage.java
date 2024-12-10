package com.susanafigueroa.magicalobjects.chest;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.susanafigueroa.helpers.GameInfo;

import java.util.ArrayList;
import java.util.List;

public class ChestManage {
    private List<Chest> listChests = new ArrayList<>();
    private String nameCollisionLayer = "chests_layer";

    public List<Chest> getListChests() {
        return listChests;
    }


    public void createStaticSpriteChests(TiledMap map, World world) {
        MapLayer collisionLayer = map.getLayers().get(nameCollisionLayer);
        if (collisionLayer == null) {
            return;
        }

        for (MapObject mapObject : collisionLayer.getObjects()) {
            Body newChestBody = createStaticChestBodyFromMap(mapObject, world);

            Chest newChest = new Chest(world, "magicalobjects/chest.png", newChestBody.getPosition().x, newChestBody.getPosition().y);
            newChest.addBody(newChestBody);

            listChests.add(newChest);
        }
    }

    private Body createStaticChestBodyFromMap(MapObject chestObject, World world) {
        if (chestObject instanceof RectangleMapObject) {
            RectangleMapObject rectChestObject = (RectangleMapObject) chestObject;
            Rectangle rectChest = rectChestObject.getRectangle();
            BodyDef bodyDefChest = new BodyDef();
            bodyDefChest.type = BodyDef.BodyType.StaticBody;
            bodyDefChest.position.set(
                (rectChest.x + rectChest.width / 2)/ GameInfo.PPM,
                (rectChest.y + rectChest.height / 2)/GameInfo.PPM
            );
            Body chestBody = world.createBody(bodyDefChest);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (rectChest.width / 2)/GameInfo.PPM,
                (rectChest.height / 2)/GameInfo.PPM
            );
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20f;

            Fixture chestFixture = chestBody.createFixture(fixtureDef);
            chestFixture.setUserData(this);

            shape.dispose();

            return chestBody;
        } else {
            return null;
        }
    }

    public void removeChest(Chest chestToRemove) {
        chestToRemove.getChestBody().getWorld().destroyBody(chestToRemove.getChestBody());
        listChests.remove(chestToRemove);
    }

}
