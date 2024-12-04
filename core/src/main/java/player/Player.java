package player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    public Player(String name, float x, float y) {
        super(new Texture(name));
        setSize(200, 200);
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }
}
