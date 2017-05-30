package pl.pr0gramista;

import com.badlogic.gdx.ai.pfa.Connection;

public class TileConnection implements Connection<Tile> {
    Tile from;
    Tile to;

    public TileConnection(Tile from, Tile to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public float getCost() {
        return 1;
    }

    @Override
    public Tile getFromNode() {
        return from;
    }

    @Override
    public Tile getToNode() {
        return to;
    }
}
