package terra;

import java.awt.Rectangle;
import java.text.DecimalFormat;

import Combat.player.Position;
import construct.Reactor;
import construct.Thruster;
import construct.Tower;
import engine.Globals;
import processing.core.PApplet;

/**
 * 
 * @author Eddie Ouyang
 * Base class used in Building phase that represents the Player's land
 */
public class Base {
	/**
	 * 2d Array of Tile(s) which represent plots of land
	 */
	public static Tile[][] tiles;
	/**
	 * Size of a tile, used to calculate graphics
	 */
	public float size;
	private Tile current;
	private Rectangle[] buttons;
	private boolean move;
	private int mass;
	private float cwtorque,ccwtorque,force,inertia;
	private DecimalFormat df = new DecimalFormat("#.##");
	private int center;
	
	public Base() {
		Tower.setup();
		center = Globals.PLAYER_MODEL_COLS/2;
	}
	
	/**
	 * Sets up buttons proportional to screen dimensions, and sets up empty Tiles array
	 * @param a Width of screen
	 * @param b Height of screen
	 */
	public void reset(int a, int b) {
		buttons = new Rectangle[] {new Rectangle(a/32,(int)(b*0.24),a/8,b/9),
				   				  new Rectangle(a/25,(int)(b*0.6),a/9,b/20),
								   new Rectangle(a/25,(int)(b*0.65),a/9,b/20),
								   new Rectangle(a/25,(int)(b*0.7),a/9,b/20),
								   new Rectangle(a/25,(int)(b*0.75),a/9,b/20)};
		tiles = new Tile[Globals.PLAYER_MODEL_COLS][Globals.PLAYER_MODEL_ROWS];
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				tiles[y][x] = new Tile(100*(Math.abs(y-center)+Math.abs(x-center)),x,y);
			}
		}
		tiles[center][center].setCore();
		current = null;
		move = false;
		mass = tiles[center][center].getTower().getWeight();
		inertia = 0;
		addInertia(tiles.length/2+1,tiles.length/2+1,mass);
	}
	
	/**
	 * Used to transfer the player's ship state from Battle to Building
	 * @param p 2d array of Positions 
	 */
	public void setMap(Position[][] p) {
		tiles[center][center] = new Tile(0,center,center);
		for(int y = 0; y < tiles.length; y++) {
			for(int x = 0; x < tiles[0].length; x++) {
				if(!p[y][x].active) continue;
				tiles[y][x].active = true;
				tiles[y][x].setTower(p[y][x].tower);
				processTower(x,y,p[y][x].tower,1);
			}
		}
	}
	
	private void processTower(int x, int y, Tower t, int mode) {
		if(t != null) {
			mass += mode*t.getWeight();
			addInertia(x,y,mode*t.getWeight());
			if(t instanceof Thruster) {
				addTorque(x,y,t,mode);
			}
			if (t instanceof Reactor) 
				force += mode*((Reactor)t).getForce();
		}
	}
	
	/**
	 * Draws the Player's base
	 * @param p PApplet to draw to 
	 * @param x0 Top left X
	 * @param y0 Top Left y
	 * @param width The length and width of the map
	 * @post Contents of p are changed
	 */
	public void draw(PApplet p,float x0, float y0, float width) {
		p.push();
		p.colorMode(PApplet.HSB,360,1,1,1);
		p.rectMode(PApplet.CORNER);
		p.background(50);
		size = width/tiles.length;
		rDraw(p,center,center,x0,y0,size,new boolean[tiles.length][tiles[0].length]);
		if(current != null)current.drawInfo(x0-width/3, y0, width/4, width,buttons, p);
		
		p.stroke(0,1,1);
		p.textAlign(PApplet.CENTER);
		p.textSize(width/25);
		p.text(((int)((force+50)/mass) == 0?"OVERWEIGHT | ":df.format(force/mass) + " SPD | ") 
			 + df.format(cwtorque/inertia) +" CW | " 
			 + df.format(ccwtorque/inertia) + " CCW", x0+width/2, y0);
		p.pop();
	}
	
	private void rDraw(PApplet p, int x, int y, float x0, float y0, float width, boolean[][] drawn) {
		 
		tiles[y][x].draw(x0+x*size, y0+y*size, width, p);
		if(tiles[y][x].equals(current)) {
			p.push();
			p.fill(300, 100, 100, 0.3f);
			p.noStroke();
			p.square(x0+x*size, y0+y*size, size);
			p.pop();
		}
		drawn[y][x] = true;
		if(tiles[y][x].active) {
			for(int[] d:new int[][] {{0,1},{0,-1},{-1,0},{1,0}}) {
				int dx = x+d[0];
				int dy = y+d[1];
				if(dy < 0 || dx < 0 || dy > tiles.length - 1|| dx > tiles.length-1) continue;
				if(drawn[dy][dx] == false) rDraw(p,dx,dy,x0,y0,width,drawn);
			}
		}
	}
	
	/**
	 * Manages what happens when the player clicks on a tile [Selling] and [Moving]
	 * @param x X coordinate in Tiles
	 * @param y Y coordinate in Tiles
	 */
	public void click(int x, int y) {
		boolean attatched = false;
		for(int[] a:new int[][] {{x==0?0:-1,0},{x==tiles[0].length-1?0:1,0},{0,y==0?0:-1},{0,y==tiles.length-1?0:1}}) {
			if(a[0]+a[1] == 0) continue;
			if(tiles[y+a[1]][x+a[0]].active) {
				attatched = true;
				break;
			}
		}
		if(attatched || tiles[y][x].active) {
			if(move && !(y==5&&x==5)) {
				processTower(x,y,tiles[y][x].getTower(),-1);
				processTower(x,y,current.getTower(),-1);
				current.swap(tiles[y][x]);
				processTower(x,y,tiles[y][x].getTower(),1);
				processTower(x,y,current.getTower(),1);
				current = null;
				move = false;
			} else {
				current = tiles[y][x];
			}
		}
		else current = null;
	}
	
	/**
	 * Sets the selected tile
	 * @param x X coordinate in Tiles
	 * @param y Y coordinate in Tiles
	 */
	public void setCurrent(int x, int y) {
		current = tiles[y][x];
	}
	
	/**
	 * Clears the selected tile
	 */
	public void clearCurrent() {
		current = null;
	}
	
	/**
	 * Highlight the tile that is currently selected
	 * @param x X coordinate in Tiles
	 * @param y Y coordinate in Tiles
	 * @param p PApplet to draw to 
	 * @param x0 Top left x
	 * @param y0 Top left y
	 * @param width Size of map
	 * @post Contents of p are changed
	 */
	public void highlight(PApplet p, int x, int y,float x0, float y0, float width) {
		width = width/tiles.length;
		if(!tiles[y][x].active) return;
		p.push();
		p.fill(255,255,255,0.5f);
		p.rect(x0+x*width, y0+y*width, width, width);
		p.pop();
	}
	
	/**
	 * Buys a tower on a tile
	 * @param x X coordinate in Tiles
	 * @param y Y coordinate in Tiles
	 * @param t Tower to purchase
	 * @return True if purchase was successful, false otherwise
	 */
	public boolean buy(int x, int y, Tower t) {
		if(!tiles[y][x].active) return false;
		boolean success = tiles[y][x].build(t);
		if(success) processTower(x,y,t,1);
		return success;
	}
	
	public Tower getTower(int x, int y) {
		if(tiles[y][x].active && tiles[y][x].getTower() != null) return tiles[y][x].getTower().copySelf();
		return null;
	}
	
	/**
	 * Getter
	 * @return If a tower is being moved
	 */
	public boolean isMoving() {
		return move;
	}
	
	
	private void recurSell(int x, int y, boolean[][] a) {
		if(!tiles[y][x].active || a[y][x]) return;
		a[y][x] = true;
		for(int[] d:new int[][] {{0,1},{0,-1},{-1,0},{1,0}}) {
			int dx = x+d[0];
			int dy = y+d[1];
			if(dy < 0 || dx < 0 || dy > tiles.length - 1|| dx > tiles.length-1) continue;
			recurSell(dx,dy,a);
		}
	}
	
	private void clearFloat() {
		boolean[][] connected = new boolean[tiles.length][tiles[0].length];
		recurSell(5,5,connected);
		for(int k = 0; k < tiles.length; k++) {
			for(int j = 0; j < tiles[0].length; j++) {
				if(!connected[k][j] && tiles[k][j].active) {
					processTower(j,k,tiles[k][j].getTower(),-1);
					tiles[k][j].buy();
				}
			}
		}
	}
	
	/**
	 * Attempts to rotate the tower on the selected tile
	 */
	public void tryRotate() {
		if(current != null) {
			addTorque(current.x, current.y, current.getTower(),-1);
			current.rotate();
			addTorque(current.x, current.y, current.getTower(),1);
		}
	}
	
	/**
	 * Sells the tower on the selected tile, otherwise sells the tile
	 */
	public void trySell() {
		if(current == null) return;
		if(current.getTower() != null) {
			processTower(current.x, current.y, current.getTower(),-1);
			current.sell();
		}
		else {if(!current.buy()) clearFloat();}
	}
	
	/**
	 * Manages user input for repair,rotate,move,and sell
	 * @param x MouseX
	 * @param y MouseY
	 */
	public void tryButton(int x, int y) {
		if(current == null) return;
		for(int i = 0; i < buttons.length; i++) {
			if(buttons[i].contains(x,y)) {
				switch(i) {
				case 0: if(!current.buy()) clearFloat();	
					break;
				case 1: current.repair();
					break;
				case 2:
					addTorque(current.x, current.y, current.getTower(),-1);
					current.rotate();
					addTorque(current.x, current.y, current.getTower(),1);
					break;
				case 3: if(!current.core)move = true;
					break;
				case 4: 
					if(current.getTower() != null) processTower(current.x,current.y,current.getTower(),-1);
					current.sell();
					break;
				}
				break;
			}
		}
	}
	
	private void addInertia(int x, int y, int w) {
		float r = (float)Math.hypot(x - tiles[0].length/2f, y - tiles.length/2f);
		inertia += r * r * w;
	}
	
	private void addTorque(int x, int y, Tower t,int mode) {
		if(!(t instanceof Thruster))return;
		float angleTo = (float)(Math.atan2(y - tiles.length/2, x - tiles[0].length/2) + Math.PI);
		float curAngle = t.getDirection() * (float)Math.PI/2;
		float distTo = (float)Math.hypot(y - tiles.length/2, x - tiles[0].length/2) * Globals.POSITION_SIZE;
		
		float deltaAngle = (float)Math.atan2(Math.sin(curAngle-angleTo), Math.cos(curAngle-angleTo));
		float crossProduct = ((Thruster)t).getForce() * distTo * (float)Math.sin(deltaAngle);
		
		if (crossProduct > 0) cwtorque += mode*crossProduct;
		if (crossProduct < 0) ccwtorque += -1*mode*crossProduct;
		if(cwtorque <= 0) cwtorque = 0;
		if(ccwtorque <= 0) ccwtorque = 0;
	}
}
