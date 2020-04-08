package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import world.World;

public class UI {
	
	public void render(Graphics g, Font font) {
		g.setColor(Color.red);
		g.fillRect(20, 20, 150, 20);
		
		g.setColor(Color.green);
		g.fillRect(20, 20, (150 * World.player.life) / World.player.maxLife, 20);
		
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(World.player.life + " / " + World.player.maxLife, 60, 36);
		g.drawString("Ammo: " + World.player.ammo, 625, 36);
	}

}
