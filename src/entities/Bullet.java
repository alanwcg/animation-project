package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class Bullet extends AnimatedEntity {

	public Bullet(double x, double y, BufferedImage sprite, double speed) {
		super(x, y, sprite, speed);
		
		maxFrames = 600;
	}
	
	@Override
	public void tick() {
		if(this.getX() > World.player.getX()) {
			x += speed;
			if(isCollidingWithWall(this.getX(), this.getY(), this)) {
				Game.bullets.remove(this);
			}
		} else {
			x -= speed;
			if(isCollidingWithWall(this.getX(), this.getY(), this)) {
				Game.bullets.remove(this);
			}
		}
		this.lifeSpan();
	}
	
	private void lifeSpan() {
		frames++;
		if(frames == maxFrames) {
			Game.bullets.remove(this);
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
		
		//g.setColor(Color.blue);
		//g.fillOval(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}
}
