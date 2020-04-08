package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class Player extends AnimatedEntity {
	
	private static final BufferedImage D_RIGHT_PLAYER = Game.spritesheet.getSprite(0 * World.TILE_SIZE, 1 * World.TILE_SIZE);
	private static final BufferedImage D_LEFT_PLAYER = Game.spritesheet.getSprite(1 * World.TILE_SIZE, 1 * World.TILE_SIZE);
	
	public static final int rightDirection = 0;
	public static final int leftDirection = 1;
	public int direction = rightDirection;
	public int life = 100, maxLife = 100;
	public int ammo = 0;
	
	public boolean right, left, up, down;
	public boolean hasShot;
	private boolean hasWeapon;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] damagePlayer;

	public Player(double x, double y, BufferedImage sprite, double speed) {
		super(x, y, sprite, speed);
		
		maxIndex = 3;
		maxFrames = 10;
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		damagePlayer = new BufferedImage[2];
		int aux = 5;
		for(int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite((i + 2)*World.TILE_SIZE, 0);
			leftPlayer[i] = Game.spritesheet.getSprite((i + aux)*World.TILE_SIZE, World.TILE_SIZE);
			aux -= 2;
		}
		damagePlayer[0] = D_RIGHT_PLAYER;
		damagePlayer[1] = D_LEFT_PLAYER;
	}
	
	@Override
	public void tick() {
		this.move();
		if(isMoving) {
			this.animation();
		} else {
			index = 0;
		}
		this.updateCamera();
		this.collectObject();
		this.shoot();
		this.getHit();
		this.damageAnimation();
	}
	
	private void move() {
		isMoving = false;
		if(right && !isCollidingWithWall((int) (x + speed), this.getY(), this)) {
			isMoving = true;
			direction = rightDirection;
			x += speed;
		} else if(left && !isCollidingWithWall((int) (x - speed), this.getY(), this)) {
			isMoving = true;
			direction = leftDirection;
			x -= speed;
		}
		
		if(up && !isCollidingWithWall(this.getX(), (int) (y - speed), this)) {
			isMoving = true;
			y -= speed;
		} else if(down && !isCollidingWithWall(this.getX(), (int) (y + speed), this)) {
			isMoving = true;
			y += speed;
		}
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - Game.WIDTH/2 + 8, World.WIDTH*World.TILE_SIZE - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - Game.HEIGHT/2 + 8, World.HEIGHT*World.TILE_SIZE - Game.HEIGHT);
	}
	
	private void collectObject() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity entity = Game.entities.get(i);
			if(Entity.isCollision(this, entity)) {
				if(entity instanceof Weapon) {
					hasWeapon = true;
					ammo += 10;
					Game.entities.remove(entity);
				} else if(entity instanceof Ammo) {
					ammo += 10;
					Game.entities.remove(entity);
				} else if(entity instanceof LifePack) {
					life += 20;
					if(life >= maxLife) {
						life = maxLife;
					}
					Game.entities.remove(entity);
				}
			}
		}
	}
	
	private void shoot() {
		if(hasShot && ammo > 0) {
			hasShot = false;
			ammo--;
			int py = 8;
			int dx;
			if(direction == rightDirection) {
				dx = 10;
			} else {
				dx = -12;
			}
			Bullet bullet = (new Bullet(this.getX() + dx, this.getY() + py, null, 3.5));
			bullet.setMask(0, 0, 3, 3);
			Game.bullets.add(bullet);
		}
	}
	
	private void getHit() {
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy enemy = Game.enemies.get(i);
			if(Entity.isCollision(this, enemy)) {
				if(Game.random.nextInt(100) < 25) {
					isDamaged = true;
					life --;
				}
				
				if(life == 0) {
					Game.state = "GAME_OVER";
				}
			}
		}
	}
	
	/*private boolean isCollidingWithEnemy(int x, int y) {
		Rectangle player = new Rectangle(x + maskX, y + maskY, maskWidth, maskHeight);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			Rectangle enemy = new Rectangle(e.getX() + e.maskX, e.getY() + e.maskY, e.maskWidth, e.maskHeight);
			if(player.intersects(enemy)) {
				return true;
			}
		}
		return false;
	}*/
	
	@Override
	public void render(Graphics g) {
		if(!isDamaged) {
			if(direction == rightDirection) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasWeapon) {
					g.drawImage(Entity.WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() - Camera.y, null);
				}
			} else {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasWeapon) {
					g.drawImage(Entity.WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		} else {
			if(direction == rightDirection) {
				g.drawImage(damagePlayer[0], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasWeapon) {
					g.drawImage(Entity.D_WEAPON_RIGHT, this.getX() + 10 - Camera.x, this.getY() - Camera.y, null);
				}
			} else {
				g.drawImage(damagePlayer[1], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasWeapon) {
					g.drawImage(Entity.D_WEAPON_LEFT, this.getX() - 10 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}

}
