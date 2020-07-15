package com.trinitystudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int width = 240;
	private final int height = 160;
	private final int scale = 3;
	
	private BufferedImage image;
	

	private BufferedImage[] player;

	
	public Game() {
	
		setPreferredSize(new Dimension(width*scale, height*scale));
		initFrame();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
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
	
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(19,19,19));
		graphics.fillRect(0, 0, width, height); // renderizar retangulo por exemplo 
		
		/*Renderização do jogo */
		
		//Graphics2D g2 = (Graphics2D) graphics;

		/**/
		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, width*scale, height*scale, null);
		bs.show();
		
	}

	@Override
	public void run() { // gamelooping feito profissionalmente
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
				System.out.println("FPS: " + frames);
				frames =0;
				timer += 1000;
			}
			
		}
		
		stop();
	}

}

