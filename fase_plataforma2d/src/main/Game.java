package main;

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

import Mundo.Mundo;
import entidades.Cenoura;
import entidades.Ceu;
import entidades.Entity;
import entidades.Inimigo;
import entidades.Player;
import graficos.Spritsheet;


public class Game extends Canvas implements Runnable,KeyListener{

	private static final long serialVersionUID = 1L;
	
	public JFrame jframe;
	private Thread thread;
	private boolean isRunning = true;
	
	public static int WIDTH = 240;
	public static int HEIGHT = 160; 
	private static int SCALE = 4;

	private BufferedImage fundo;
	public static List<Entity> entidades;
	public static Spritsheet sprite;
	public static Mundo Mundo;
	
	public static Player player;
	
	public static List<Ceu> ceuvetor;
	public static Spritsheet ceu;
	
	public static List<Cenoura> cenoura;
	public static List<Inimigo> inimigo;
	
	public static UserInterface ui;
	
	public int level = 1, levelmaximo = 2;
	
	public Game() {
		Sound.urro.play();
		Sound.musicBackground.loop();
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		ui = new UserInterface();
		fundo = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entidades = new ArrayList<Entity>();
		sprite = new Spritsheet("/spritesheet.png");
		ceuvetor = new ArrayList<Ceu>();
		cenoura = new ArrayList<Cenoura>();
		inimigo = new ArrayList<Inimigo>();
		ceu = new Spritsheet("/ceu2.jpg");
		player = new Player(0,0,16,16,sprite.getSprite(32, 0, 16, 16));
		entidades.add(player);
		Mundo = new Mundo("/level1.png");
	}
	
	public void initFrame() {
		jframe = new JFrame("Shrek salute");
		jframe.add(this);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
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
	
	public void tick() {
	
		if(inimigo.size() == 0) {
			level++;
			if(level > levelmaximo) {
				level = 1;
			}
			String Level = "level"+level+".png";
			Mundo.newlevel(Level);
		}
		
		for(int i = 0; i < entidades.size(); i++) {
			Entity entidade = entidades.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < ceuvetor.size(); i++) {
			Ceu entidade = ceuvetor.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < cenoura.size(); i++) {
			Cenoura entidade = cenoura.get(i);
			entidade.tick();
		}
		
		for(int i = 0; i < inimigo.size(); i++) {
			Inimigo entidade = inimigo.get(i);
			entidade.tick();
		}
	}
	
	
	
	public void render() {
		BufferStrategy buffer = this.getBufferStrategy();
		if(buffer == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = fundo.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		Mundo.render(g);
		
		for(int i = 0; i < ceuvetor.size(); i++) {
			Ceu entidade = ceuvetor.get(i);
			entidade.render(g);
		}
		
		for(int i = 0; i < entidades.size(); i++) {
			Entity entidade = entidades.get(i);
			entidade.render(g);
		}
		
		for(int i = 0; i < cenoura.size(); i++) {
			Cenoura entidade = cenoura.get(i);
			entidade.render(g);
		}
		
		for(int i = 0; i < inimigo.size(); i++) {
			Inimigo entidade = inimigo.get(i);
			entidade.render(g);
		}
		
		ui.render(g);
		
		g = buffer.getDrawGraphics();
		g.drawImage(fundo, 0, 0, WIDTH*SCALE,HEIGHT*SCALE, null);
		buffer.show();
	}
	
	public void run() {
		long lastTime  = System.nanoTime();
		double amountOfTicks = 60.0f;
		double ms = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ms;
			lastTime = now;
			if(delta > 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS : " + frames );
				frames = 0;
				timer += 1000;
			}
		}
		stop();
		
	}

	public void keyTyped(KeyEvent e) {
		// NAO VAMOS TRABALHAR NELA
		
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}

	

}
