package com.trinitystudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.trinitystudios.entities.Entity;
import com.trinitystudios.entities.Player;
import com.trinitystudios.graficos.Spritesheet;
import com.trinitystudios.world.World;


public class Game extends Canvas implements Runnable, KeyListener {
	

	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int width = 240;
	private final int height = 160;
	private final int scale = 3;
	
	private BufferedImage image;

	private Player player;
	
	public static World world;
	
	

	public static Spritesheet spritesheet;
	
	public List<Entity> entities;
	
	public Game() {
		
		
		addKeyListener(this);
		setPreferredSize(new Dimension(width*scale, height*scale));
		initFrame();
		
		
		//inicializando objetos
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		world = new World("/map.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0,16,16));
		entities.add(player);
		
		
		
	
	}
	
	
	
	public void initFrame() {
		
		frame = new JFrame("Game1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
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
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void update() {

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
		}
	
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0, 0, width, height); // renderizar retangulo por exemplo 
		
		/*Renderização do jogo */
		
		//Graphics2D g2 = (Graphics2D) graphics;
		world.render(graphics);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(graphics);
		}

		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, width*scale, height*scale, null);
		bs.show();
		
	}

	@Override
	public void run() { // gamelooping feito profissionalmente
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfUpdates = 60.0;
		double ns = 1000000000 / amountOfUpdates;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now  = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
				
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: " + frames);
				frames =0;
				timer += 1000;
			}
			
		}
		
		stop();
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
			
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				 e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}

	}



	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
			
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				 e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}




	

}

