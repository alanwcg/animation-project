package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main.Game;

public class Menu {
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
	}

}
