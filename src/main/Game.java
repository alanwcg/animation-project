package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import entities.Bullet;
import entities.Enemy;
import entities.Entity;
import graphics.Spritesheet;
import graphics.UI;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private int frames = 0, maxFrames = 45;
	
	private boolean drawGameOverString = true;
	public static boolean restartGame = false;
	private boolean isRunning;
	
	private JFrame frame;
	private Thread thread;
	private BufferedImage background;
	
	public static Spritesheet spritesheet;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bullet> bullets;
	
	public static World world;
	private UI ui;
	
	public static Random random;
	
	public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	public Font newFont;
	
	public static String state = "NORMAL";
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		//this.addKeyListener(this);
		this.initializeFrame();
		
		random = new Random();
		
		background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		spritesheet = new Spritesheet("/spritesheet.png");
		
		try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(50f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		entities = new ArrayList<>();
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();
		world = new World("/level1.png");
		ui = new UI();
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	private void initializeFrame() {
		frame = new JFrame("Game");
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void tick() {
		if(state == "NORMAL") {
			for(int i = 0; i < enemies.size(); i++) {
				Enemy enemy = enemies.get(i);
				enemy.tick();
			}
			for(int i = 0; i < bullets.size(); i++) {
				Bullet bullet = bullets.get(i);
				bullet.tick();
			}
			World.player.tick();
		} else if(state == "GAME_OVER") {
			World.player.updateCamera();
			this.gameOverAnimation();
			if(restartGame) {
				state = "NORMAL";
				restartGame = false;
				World.restartGame();
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = background.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.render(g);
		}
		for(int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			bullet.render(g);
		}
		World.player.render(g);
		
		g.dispose();
		
		g = bs.getDrawGraphics();
		g.drawImage(background, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		ui.render(g, new Font("Arial", Font.BOLD, 16));
		
		if(state == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			
			g2.setFont(newFont);
			g2.setColor(Color.red);
			g2.drawString("Game Over!", 197, 200);
			
			g2.setFont(new Font("Arial", Font.BOLD, 24));
			g2.setColor(Color.white);
			if(drawGameOverString) {
				g.drawString("> Press ENTER to restart game <", 170, 300);
			}
		}
		
		bs.show();
	}
	
	private void gameOverAnimation() {
		frames++;
		if(frames == maxFrames) {
			if(drawGameOverString) {
				drawGameOverString = false;
			} else {
				drawGameOverString = true;
			}
			frames = 0;
		}
	}
	
	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double nanoSecond = 1000000000;
		double sixthOfSecond = nanoSecond / amountOfTicks;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		this.requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / sixthOfSecond;
			lastTime = now;
			if(delta >= 1) {
				this.tick();
				this.render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: " + frames);
				timer = System.currentTimeMillis();
				frames = 0;
			}
		}
		this.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			World.player.right = true;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			World.player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			World.player.up = true;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			World.player.down = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			World.player.hasShot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER && state == "GAME_OVER") {
			restartGame = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			World.player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			World.player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			World.player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			World.player.down = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			World.player.hasShot = false;
		}
	}

}
