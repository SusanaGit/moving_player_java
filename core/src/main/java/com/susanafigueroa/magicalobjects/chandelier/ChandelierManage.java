package com.susanafigueroa.magicalobjects.chandelier;


import com.badlogic.gdx.graphics.Texture;
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

public class ChandelierManage {

    private List<Chandelier> listChandeliers = new ArrayList<>();
    private List<Chandelier> listChandeliersToRemove = new ArrayList<>();
    private String nameCollisionLayer = "chandeliers_layer";
    public List<Chandelier> getListChandeliers() {
        return listChandeliers;
    }

    public void createStaticSpriteChandeliers(TiledMap map, World world) {
        MapLayer collisionLayer = map.getLayers().get(nameCollisionLayer);
        if (collisionLayer == null) {
            return;
        }

        for (MapObject mapObject : collisionLayer.getObjects()) {
            Body newChandelierBody = createStaticChandelierBodyFromMap(mapObject, world);

            Chandelier newChandelier = new Chandelier(world, "magicalobjects/chandelier.png", newChandelierBody.getPosition().x, newChandelierBody.getPosition().y);
            newChandelier.setBody(newChandelierBody);
            listChandeliers.add(newChandelier);
        }
    }

    private Body createStaticChandelierBodyFromMap(MapObject chandelierObject, World world) {
        if (chandelierObject instanceof RectangleMapObject) {
            RectangleMapObject rectChandelierObject = (RectangleMapObject) chandelierObject;
            Rectangle rectChandelier = rectChandelierObject.getRectangle();
            BodyDef bodyDefChandelier = new BodyDef();
            bodyDefChandelier.type = BodyDef.BodyType.StaticBody;
            bodyDefChandelier.position.set(
                (rectChandelier.x + rectChandelier.width / 2)/ GameInfo.PPM,
                (rectChandelier.y + rectChandelier.height / 2)/GameInfo.PPM
            );
            Body chandelierBody = world.createBody(bodyDefChandelier);
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                (rectChandelier.width / 2)/GameInfo.PPM,
                (rectChandelier.height / 2)/GameInfo.PPM
            );
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 20f;
            chandelierBody.createFixture(fixtureDef);

            shape.dispose();

            return chandelierBody;
        } else {
            return null;
        }
    }

    public void removeChandelier(Chandelier chandelierToRemove) {
        if (!listChandeliersToRemove.contains(chandelierToRemove)) {
            listChandeliersToRemove.add(chandelierToRemove);
        }
    }

    public void updateListChandeliers() {
        for (Chandelier chandelierToRemove : listChandeliersToRemove) {
            Texture chandelierTextureToRemove = chandelierToRemove.getTexture();
            Body chandelierBodyToRemove = chandelierToRemove.getChandelierBody();

            if (chandelierBodyToRemove != null) {
                chandelierBodyToRemove.getWorld().destroyBody(chandelierBodyToRemove);
            }

            if (chandelierTextureToRemove != null) {
                chandelierTextureToRemove.dispose();
            }

            listChandeliers.remove(chandelierToRemove);
        }
        listChandeliersToRemove.clear();
    }
}
