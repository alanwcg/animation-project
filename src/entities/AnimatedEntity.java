package entities;

import java.awt.image.BufferedImage;

public abstract class AnimatedEntity extends Entity {
	
	public int frames = 0, maxFrames;
	public int index = 0, maxIndex;
	public int damageFrames = 0, maxDamageFrames = 10;
	
	public double speed;
	
	public boolean isMoving;
	public boolean isDamaged;

	public AnimatedEntity(double x, double y, BufferedImage sprite, double speed) {
		super(x, y, sprite);
		this.speed = speed;
	}
	
	public void animation() {
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	
	public void damageAnimation() {
		if(isDamaged) {
			damageFrames++;
			if(damageFrames == maxDamageFrames) {
				isDamaged = false;
				damageFrames = 0;
			}
		}
	}

	public double getSpeed() {
		return speed;
	}

}
