package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import entities.Ammo;
import entities.Enemy;
import entities.Entity;
import entities.LifePack;
import entities.Player;
import entities.Weapon;
import main.Game;

public class World {
	
	public static final int TILE_SIZE = 16;
	public static int WIDTH;
	public static int HEIGHT;
	
	private BufferedImage level;
	public static Tile[] tiles;
	private int[] pixels;
	public static List<WallTile> walls;
	
	public static Player player;
	
	public World(String path) {
		try {
			level = ImageIO.read(this.getClass().getResource(path));
			
			WIDTH = level.getWidth();
			HEIGHT = level.getHeight();
			
			tiles = new Tile[WIDTH * HEIGHT];
			pixels = new int[WIDTH * HEIGHT];
			level.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
			
			walls = new ArrayList<>();
			
			for(int x = 0; x < WIDTH; x++) {
				for(int y = 0; y < HEIGHT; y++) {
					int index = x + (y * WIDTH);
					int xx = x * TILE_SIZE;
					int yy = y * TILE_SIZE;
					
					tiles[index] = new FloorTile(xx, yy, Tile.FLOOR_TILE);
					
					int pixel = pixels[index];
					switch(pixel) {
						case 0xFFFFFFFF:
							WallTile wall = new WallTile(xx, yy, Tile.WALL_TILE);
							tiles[index] = wall;
							walls.add(wall);
							break;
						case 0xFFFF6A00:
							Weapon weapon = new Weapon(xx, yy, Entity.WEAPON);
							weapon.setMask(1, 8, 14, 6);
							Game.entities.add(weapon);
							break;
						case 0xFFFFD800:
							Ammo ammo = new Ammo(xx, yy, Entity.AMMO);
							ammo.setMask(6, 7, 4, 9);
							Game.entities.add(ammo);
							break;
						case 0xFFFF7F7F:
							LifePack lifePack = new LifePack(xx, yy, Entity.LIFE_PACK);
							lifePack.setMask(2, 6, 11, 10);
							Game.entities.add(lifePack);
							break;
						case 0xFFFF0000:
							Enemy enemy = new Enemy(xx, yy, null, 0.5);
							enemy.setMask(3, 3, 10, 13);
							Game.enemies.add(enemy);
							break;
						case 0xFF0026FF:
							player = new Player(xx, yy, null, 1.0);
							player.setMask(4, 0, 8, 16);
							break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int x = xStart; x <= xFinal; x++) {
			for(int y = yStart; y <= yFinal; y++) {
				if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[x + (y * WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void restartGame() {
		Game.entities.clear();
		Game.enemies.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<>();
		Game.enemies = new ArrayList<>();
		Game.bullets = new ArrayList<>();
		Game.world = new World("/level1.png");
		//Game.ui = new UI();
	}
	
	/*public static boolean checkMoveToRight(int x, int y) {
		//Coordinates to check move to the right
		int xr = (x + 13) / TILE_SIZE;
		int yh1 = (y + 15) / TILE_SIZE;
		int yh2 = y /TILE_SIZE;
		
		return !(tiles[xr + (yh1 * WIDTH)] instanceof WallTile ||
				tiles[xr + (yh2 * WIDTH)] instanceof WallTile);
	}
	
	public static boolean checkMoveToLeft(int x, int y) {
		//Coordinates to check move to the left
		int xl = (x + 2) / TILE_SIZE;
		int yh1 = (y + 15) / TILE_SIZE;
		int yh2 = y / TILE_SIZE;
		
		return !(tiles[xl + (yh1 * WIDTH)] instanceof WallTile ||
				tiles[xl + (yh2 * WIDTH)] instanceof WallTile);
	}
	
	public static boolean checkMoveToUp(int x, int y) {
		//Coordinates to check move to up
		int xv1 = (x + 11) / TILE_SIZE;
		int xv2 = (x + 4) / TILE_SIZE;
		int yv = (y - 1) / TILE_SIZE;
		
		return !(tiles[xv1 + (yv * WIDTH)] instanceof WallTile ||
				tiles[xv2 + (yv * WIDTH)] instanceof WallTile);
	}
	
	public static boolean checkMoveToDown(int x, int y) {
		//Coordinates to check move to down
		int xv1 = (x + 11) / TILE_SIZE;
		int xv2 = (x + 4) / TILE_SIZE;
		int yv = (y + 17) / TILE_SIZE;
		
		return !(tiles[xv1 + (yv * WIDTH)] instanceof WallTile ||
				tiles[xv2 + (yv * WIDTH)] instanceof WallTile);
	}*/

}
