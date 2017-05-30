package pl.pr0gramista;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * size = (10, 10)
 */
public class Tile {
    public int x;
    public int y;

    public boolean isBlocked = false;

    public Array<Connection<Tile>> neighbours = new Array<Connection<Tile>>();

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(ShapeRenderer shapeRenderer) {

        if(isBlocked)
            shapeRenderer.setColor(Color.RED);
        else
            shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.box(PathfindingDemo.x + x, PathfindingDemo.y + y, 0, 10, 10, 1);
    }
}
