package Mundo;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entidades.Cenoura;
import entidades.Ceu;
import entidades.Check;
import entidades.Entity;
import entidades.Grama;
import entidades.Inimigo;
import entidades.Player;
import entidades.Solido;
import graficos.Spritsheet;
import main.Game;

public class Mundo {
	
	public static int WIDTH, HEIGHT;
	public Tile[] tiles;
	
	public Mundo(String path) {
		try {
			BufferedImage level = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[level.getWidth() * level.getHeight()];
			tiles = new Tile[level.getWidth() * level.getHeight()];
			WIDTH = level.getWidth();
			HEIGHT = level.getHeight();
			level.getRGB(0, 0, level.getWidth(), level.getHeight(), pixels, 0, level.getWidth());
			
			for(int x = 0; x < level.getWidth();x++) {
				for(int y = 0; y < level.getHeight();y++) {
					int pixelAtual = pixels[x + (y*level.getWidth())];
					tiles[x + (y*WIDTH)] = new Empty(x*16,y*16,Entity.empty);
					if(pixelAtual == 0xFF7bff00) {
						//player
						Game.player.setX(x*16);
						Game.player.setY(y*16);
						
					}else if(pixelAtual == 0xFF562500) {
						//chao
						Solido solido = new Solido(x*16,y*16,16,16,Entity.chao);
						Game.entidades.add(solido);
						
					}else if(pixelAtual == 0xFF4b692f) {
						//chaoGrama
						Solido solido = new Solido(x*16,y*16,16,16,Entity.chaoGRAMA);
						Game.entidades.add(solido);
						
					}else if(pixelAtual == 0xFF004eff) {
						Ceu ceu = new Ceu(x*16,y*16,16,16,Entity.ceu);
						Game.ceuvetor.add(ceu);
						
					}else if(pixelAtual == 0xFFff0000) {
						Inimigo a = new Inimigo(x*16,y*16,16,16,Entity.inimigo);
						Game.inimigo.add(a);
						
					}else if(pixelAtual == 0xFFff5e00) {
						Cenoura a = new Cenoura(x*16,y*16,16,16,Entity.cenoura);
						Game.cenoura.add(a);
						
					}else if(pixelAtual == 0xFF143e0d) {
						Grama a = new Grama(x*16,y*16,16,16,Entity.grama);
						Game.entidades.add(a);
						
					}else if(pixelAtual == 0xFF4e0c0c) {
						Check a = new Check(x*16,y*16,16,16,Entity.save);
						Game.entidades.add(a);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xi = Camera.x/16;
		int yi = Camera.y/16;
		int xf = xi + (Game.WIDTH/16);
		int yf = yi + (Game.HEIGHT/16);
		for(int x = xi; x <= xf; x++) {
			for(int y = yi; y <= yf; y++) {
				if(x < 0|| y < 0|| x >= WIDTH|| y >= HEIGHT)
					continue;
				Tile tile = tiles[x + (y*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void newlevel(String level) {
		Game.entidades = new ArrayList<Entity>();
		Game.sprite = new Spritsheet("/spritesheet.png");
		Game.ceuvetor = new ArrayList<Ceu>();
		Game.cenoura = new ArrayList<Cenoura>();
		Game.inimigo = new ArrayList<Inimigo>();
		Game.ceu = new Spritsheet("/ceu2.jpg");
		Game.player = new Player(0,0,16,16,Game.sprite.getSprite(32, 0, 16, 16));
		Game.entidades.add(Game.player);
		Game.Mundo = new Mundo("/"+level);
	}
	
}
