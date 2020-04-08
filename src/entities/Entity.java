package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.WallTile;
import world.World;

public abstract class Entity {
	
	public static final BufferedImage WEAPON = Game.spritesheet.getSprite(7 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	public static final BufferedImage WEAPON_RIGHT = Game.spritesheet.getSprite(8 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	public static final BufferedImage D_WEAPON_RIGHT = Game.spritesheet.getSprite(0 * World.TILE_SIZE, 2 * World.TILE_SIZE);
	public static final BufferedImage WEAPON_LEFT = Game.spritesheet.getSprite(9 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	public static final BufferedImage D_WEAPON_LEFT = Game.spritesheet.getSprite(1 * World.TILE_SIZE, 2 * World.TILE_SIZE);
	public static final BufferedImage AMMO = Game.spritesheet.getSprite(6 * World.TILE_SIZE, 1 * World.TILE_SIZE);
	public static final BufferedImage LIFE_PACK = Game.spritesheet.getSprite(6 * World.TILE_SIZE, 0 * World.TILE_SIZE);
	
	public double x;
	public double y;

	public int maskX, maskY, maskWidth, maskHeight;
	
	private BufferedImage sprite;
	
	public Entity(double x, double y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void tick() {}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}
	
	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskWidth = maskWidth;
		this.maskHeight = maskHeight;
	}
	
	public static boolean isCollision(Entity e1, Entity e2) {
		Rectangle rec1 = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskWidth, e1.maskHeight);
		Rectangle rec2 = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskWidth, e2.maskHeight);
		
		return rec1.intersects(rec2);
	}
	
	public static boolean isCollidingWithWall(int x, int y, Entity e) {
		Rectangle entity = new Rectangle(x + e.maskX, y + e.maskY, e.maskWidth, e.maskHeight);
		for(int i = 0; i < World.walls.size(); i++) {
			WallTile w = World.walls.get(i);
			
			Rectangle wall = new Rectangle(w.getX(), w.getY(), World.TILE_SIZE, World.TILE_SIZE);
			if(entity.intersects(wall)) {
				return true;
			}
		}
		return false;
	}
	
	public static double getDistance(Entity e1, Entity e2) {
		double dx = e1.x - e2.x;
		double dy = e1.y - e2.y;
		
		return Math.sqrt(dx * dx + dy * dy);
	}

}
