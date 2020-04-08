package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public abstract class Tile {
	
	public static final BufferedImage FLOOR_TILE = Game.spritesheet.getSprite(0 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	public static final BufferedImage WALL_TILE = Game.spritesheet.getSprite(1 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	
	private int x, y;
	
	private BufferedImage tile;
	
	public Tile(int x, int y, BufferedImage tile) {
		this.x = x;
		this.y = y;
		this.tile = tile;
	}
	
	public void render(Graphics g) {
		g.drawImage(tile, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
