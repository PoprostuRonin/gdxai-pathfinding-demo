package pl.pr0gramista;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.*;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PathfindingDemo extends ApplicationAdapter {
	public static int x = 0;
	public static int y = 0;

	ShapeRenderer shapeRenderer;

	PathFinder<Tile> pathFinder;
	TileGraphPath path = new TileGraphPath();

	Tile[][] tiles = new Tile[30][30];

	int startX, startY;
	int endX, endY;
	
	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		for (int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				tiles[i][j] = new Tile(i * 10, j * 10);
			}
		}

		IndexedGraph<Tile> graph = new IndexedGraph<Tile>() {
			@Override
			public int getIndex(Tile node) {
				return node.x/10 * 30 + node.y/10;
			}

			@Override
			public int getNodeCount() {
				return 30 * 30;
			}

			@Override
			public Array<Connection<Tile>> getConnections(final Tile fromNode) {
				return fromNode.neighbours;
			}
		};
		pathFinder = new IndexedAStarPathFinder<Tile>(graph);
	}

	void makeGraph() {
		for (int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				Tile t = tiles[i][j];
				t.neighbours.clear();

				if(i + 1 <= 29 && !tiles[i+1][j].isBlocked) {
					t.neighbours.add(new TileConnection(t, tiles[i+1][j]));
				}

				if(i - 1 >= 0 && !tiles[i-1][j].isBlocked) {
					t.neighbours.add(new TileConnection(t, tiles[i-1][j]));
				}

				if(j + 1 <= 29 && !tiles[i][j+1].isBlocked) {
					t.neighbours.add(new TileConnection(t, tiles[i][j+1]));
				}

				if(j - 1 >= 0 && !tiles[i][j-1].isBlocked) {
					t.neighbours.add(new TileConnection(t, tiles[i][j-1]));
				}

			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(Gdx.input.isKeyJustPressed(Input.Keys.D))
			x += 10;
		if(Gdx.input.isKeyJustPressed(Input.Keys.W))
			y += 10;
		if(Gdx.input.isKeyJustPressed(Input.Keys.A))
			x -= 10;
		if(Gdx.input.isKeyJustPressed(Input.Keys.S))
			y -= 10;

		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			Tile t = getHoveredTile();
			t.isBlocked = !t.isBlocked;
			makeGraph();
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			Tile t = getHoveredTile();
			startX = t.x / 10;
			startY = t.y / 10;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			Tile t = getHoveredTile();
			endX = t.x / 10;
			endY = t.y / 10;
		}

		shapeRenderer.begin();
		for (int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				tiles[i][j].draw(shapeRenderer);
			}
		}

		for(int i = 0; i < path.getCount(); i++) {
			Tile to = path.get(i).getToNode();
			shapeRenderer.setColor(Color.BROWN);
			shapeRenderer.box(to.x, to.y, 0, 10, 10, 1);
		}

		shapeRenderer.setColor(Color.MAGENTA);
		shapeRenderer.box(startX * 10, startY * 10, 0, 10, 10, 1);

		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.box(endX * 10, endY * 10, 0, 10, 10, 1);

		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			makeGraph();
			Tile from = tiles[startX][startY];
			System.out.println(String.format("%d %d", from.x, from.y));
			Tile to = tiles[endX][endY];
			System.out.println(String.format("%d %d", to.x, to.y));

			Heuristic<Tile> tileHeuristic = new Heuristic<Tile>() {
				@Override
				public float estimate(Tile node, Tile endNode) {
					return (float) Math.sqrt((endNode.x-node.x) * (endNode.x-node.x) + (endNode.y - node.y) * (endNode.y - node.y));
				}
			};

			path.clear();
			boolean found = pathFinder.searchConnectionPath(from, to, tileHeuristic, path);
			System.out.println(found);
			for(int i = 0; i < path.getCount(); i++) {
				Connection<Tile> tileConnection = path.get(i);
				System.out.println(String.format("(%d, %d) -> (%d, %d",
						tileConnection.getFromNode().x/10,
						tileConnection.getFromNode().y/10,
						tileConnection.getToNode().x/10,
						tileConnection.getToNode().y/10));
			}
		}

		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		shapeRenderer.dispose();
	}

	public Tile getHoveredTile() {
		int mx = Gdx.input.getX();
		int my = Gdx.graphics.getHeight() - Gdx.input.getY();

		mx -= x;
		my -= y;

		int ix = MathUtils.clamp(mx / 10, 0, 29);
		int iy = MathUtils.clamp(my / 10, 0, 29);
		return tiles[ix][iy];
	}
}
