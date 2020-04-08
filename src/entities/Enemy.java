package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class Enemy extends AnimatedEntity {
	
	private static final BufferedImage DAMAGE_SPRITE = Game.spritesheet.getSprite(9 * World.TILE_SIZE, 1 * World.TILE_SIZE);
	
	private BufferedImage[] sprites;
	
	private int life = 5;

	public Enemy(double x, double y, BufferedImage sprite, double speed) {
		super(x, y, sprite, speed);
		
		maxIndex = 1;
		maxFrames = 30;
		
		sprites = new BufferedImage[2];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite((i + 7) * World.TILE_SIZE, World.TILE_SIZE);
		}
	}
	
	@Override
	public void tick() {
		this.animation();
		this.getShot();
		this.damageAnimation();
		this.hunt();
	}
	
	private void getShot() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Bullet bullet = Game.bullets.get(i);
			if(Entity.isCollision(this, bullet)) {
				isDamaged = true;
				life--;
				Game.bullets.remove(bullet);
				
				if(life == 0) {
					Game.enemies.remove(this);
				}
			}
		}
	}
	
	private void hunt() {
		if(!Entity.isCollision(this, World.player) && getDistance(this, World.player) < 100) {
			if(this.getX() < World.player.getX() && !isCollidingWithEnemy((int) (x + speed), this.getY()) &&
					!isCollidingWithWall((int) (x + speed), this.getY(), this)) {
				x += speed;
			} else if(this.getX() > World.player.getX() && !isCollidingWithEnemy((int) (x - speed), this.getY()) &&
					!isCollidingWithWall((int) (x - speed), this.getY(), this)) {
				x -= speed;
			}
			
			if(this.getY() < World.player.getY() && !isCollidingWithEnemy(this.getX(), (int) (y + speed)) &&
					!isCollidingWithWall(this.getX(), (int) (y + speed), this)) {
				y += speed;
			} else if(this.getY() > World.player.getY() && !isCollidingWithEnemy(this.getX(), (int) (y - speed)) &&
					!isCollidingWithWall(this.getX(), (int) (y - speed), this)) {
				y -= speed;
			}
		}
	}
	
	private boolean isCollidingWithEnemy(int x, int y) {
		Rectangle enemy1 = new Rectangle(x + maskX, y + maskY, maskWidth, maskHeight);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;
			}
			
			Rectangle enemy2 = new Rectangle(e.getX() + e.maskX, e.getY() + e.maskY, e.maskWidth, e.maskHeight);
			if(enemy1.intersects(enemy2)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void render(Graphics g) {
		if(isDamaged) {
			g.drawImage(DAMAGE_SPRITE, this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}

}
