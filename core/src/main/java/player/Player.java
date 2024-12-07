package player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {

    private World world;
    private Body body;

    public Player(World world, String name, float x, float y) {
        super(new Texture(name));
        this.world = world;
        setSize(200, 200);
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
        createBody();
    }

    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        // static -> not afected by other forces
        // kinematic -> not afected by gravity, afected by other forces
        // DynamicBody -> afected by gravity and other forces
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // initial position in meters
        bodyDef.position.set(
            getX() + getWidth() / 2,
            getY() + getHeight() / 2
        );

        // body ahora está en el world
        body = world.createBody(bodyDef);

        // defino rectángulo
        PolygonShape shape = new PolygonShape();
        // shape in meters
        shape.setAsBox(
            getWidth() / 2,
            getHeight() / 2
        );

        // defino las propiedades físicas del body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        // fijo el body al rectángulo shape
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void updatePlayer() {

        this.setPosition(
            body.getPosition().x - getWidth() / 2,
            body.getPosition().y - getHeight() / 2
        );
    }

    public Body getBody() {
        return this.body;
    }
}
