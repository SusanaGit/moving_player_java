package player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {

    private World world;
    private Body body;

    public Player(String name, float x, float y) {
        super(new Texture(name));
        setSize(200, 200);
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    public void move (Vector2 direction, float speed) {
        setPosition(getX() + direction.x * speed, getY() + direction.y * speed);
    }
}
