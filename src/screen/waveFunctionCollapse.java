package screen;
import java.util.ArrayList;
import java.util.Stack;

import Combat.player.Player;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Funny background thing
 * @author Eddie Ouyang
 *
 */
public class waveFunctionCollapse{
	private PImage[] tilesV = new PImage[36];
	private PImage[] cobble = new PImage[9];
	private PImage[] pebble = new PImage[9];
	private PImage[] lava = new PImage[9];
	private Node[] nodes;
	private Tile[][] map = new Tile[64][64];
	private int[][] tilesDown = new int[64][64];
	private Stack<Tile> active,propagation;
	
	public waveFunctionCollapse() {
		generate();
	}
	
	/**
	 * Load images
	 * @param p PApplet to load
	 */
	public void setup(PApplet p) {
		PImage sheet = p.loadImage("img/WFC/bgSheet.png");
		
		for(int z = 0; z < 4; z++) {
			for(int y = 0; y < 3; y++) {
				for(int x = 0; x < 3; x++) {
					tilesV[x + 3*y + 9*z] = sheet.get(x*80 + z*240, y*80, 80, 80);
				}
			}
		}
		
		PImage cobbleSheet = p.loadImage("img/WFC/cobble.png");
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				cobble[x + 3*y] = cobbleSheet.get(x*80, y*80, 80, 80);
			}
		}
		
		PImage pebbleSheet = p.loadImage("img/WFC/pebble.png");
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				pebble[x + 3*y] = pebbleSheet.get(x*80, y*80, 80, 80);
			}
		}
		
		PImage lavaSheet = p.loadImage("img/WFC/lava.png");
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				lava[x + 3*y] = lavaSheet.get(x*80, y*80, 80, 80);
			}
		}
	}
	
	private void loadNodes() {
		Node n0 = new Node();
		n0.top = new int[] {-2,6,7,8,33,19,35};
		n0.left = new int[] {-2,2,5,8,29,21,35};
		n0.right = new int[] {1,17,2};
		n0.bottom = new int[] {3,17,6};
		
		Node n1 = new Node();
		n1.top = new int[] {-2,6,7,8,33,19,35};
		n1.left = new int[] {0,15,1};
		n1.right = new int[] {2,17,1};
		n1.bottom = new int[] {-1,9,7,11};
		
		Node n2 = new Node();
		n2.top = new int[] {-2,6,7,8,33,19,35};
		n2.left = new int[] {1,15,0};
		n2.right = new int[] {-2,27,23,33,0,3,6};
		n2.bottom = new int[] {5,8,15};
		
		Node n3 = new Node();
		n3.top = new int[] {0,3,11};
		n3.left = new int[] {-2,2,5,8,29,21,35};
		n3.right = new int[] {-1,5,9,15};
		n3.bottom = new int[] {3,6,17};

		Node n4 = new Node();
		n4.top = new int[] {-1,15,1,17};
		n4.left = new int[] {-1,11,3,17};
		n4.right = new int[] {-1,9,5,15};
		n4.bottom = new int[] {-1,9,7,11};
		
		Node n5 = new Node();
		n5.top = new int[] {2,5,9};
		n5.left = new int[] {-1,11,3,17};
		n5.right = new int[] {-2,27,23,33,0,3,6};
		n5.bottom = new int[] {5,8,15};
		
		Node n6 = new Node();
		n6.top = new int[] {0,3,11};
		n6.left = new int[] {-2,2,5,8,29,21,35};
		n6.right = new int[] {7,8,11};
		n6.bottom = new int[] {-2,0,1,2,27,25,29};
		
		Node n7 = new Node();
		n7.top = new int[] {-1,1,15,17};
		n7.left = new int[] {6,7,9};
		n7.right = new int[] {7,8,11};
		n7.bottom = new int[] {-2,0,1,2,27,25,29};
		
		Node n8 = new Node();
		n8.top = new int[] {2,5,9};
		n8.left = new int[] {6,7,9};
		n8.right = new int[] {-2,27,23,33,0,3,6};
		n8.bottom = new int[] {-2,0,1,2,27,25,29};

		Node n9 = new Node();
		n9.top = new int[] {-1,15,1,17};
		n9.left = new int[] {-1,11,3,17};
		n9.right = new int[] {7,11,8};
		n9.bottom = new int[] {5,15,8};
		
		Node n11 = new Node();
		n11.top = new int[] {-1,15,1,17};
		n11.left = new int[] {6,7,9};
		n11.right = new int[] {-1,9,5,15};
		n11.bottom = new int[] {3,6,17};
		
		Node n13 = new Node();
		n13.top = new int[] {6,7,8,33,19,35};
		n13.left = new int[] {2,5,8,29,21,35};
		n13.right = new int[] {27,23,33,0,3,6};
		n13.bottom = new int[] {0,1,2,27,25,29};
		
		Node n15 = new Node();
		n15.top = new int[] {2,5,9};
		n15.left = new int[] {-1,11,3,17};
		n15.right = new int[] {1,17,2};
		n15.bottom = new int[] {-1,9,7,11};
		
		Node n17 = new Node();
		n17.top = new int[] {0,3,11};
		n17.left = new int[] {0,1,15};
		n17.right = new int[] {-1,9,5,15};
		n17.bottom = new int[] {-1,9,7,11};
		
		Node n18 = new Node();
		n18.top = new int[] {-3};
		n18.left = new int[] {-3};
		n18.right = new int[] {19,20,35};
		n18.bottom = new int[] {21,24,35};
		
		Node n19 = new Node();
		n19.top = new int[] {-3};
		n19.left = new int[] {18,19,33};
		n19.right = new int[] {19,20,35};
		n19.bottom = new int[] {-2,0,1,2,27,25,29};
		
		Node n20 = new Node();
		n20.top = new int[] {-3};
		n20.left = new int[] {18,19,33};
		n20.right = new int[] {-3};
		n20.bottom = new int[] {23,26,33};
		
		Node n21 = new Node();
		n21.top = new int[] {21,18,29};
		n21.left = new int[] {-3};
		n21.right = new int[] {-2,27,23,33,0,3,6};
		n21.bottom = new int[] {21,24,35};
		
		Node n23 = new Node();
		n23.top = new int[] {23,20,27};
		n23.left = new int[] {-2,2,5,8,29,21,35};
		n23.right = new int[] {-3};
		n23.bottom = new int[] {23,26,33};
		
		Node n24 = new Node();
		n24.top = new int[] {18,21,29};
		n24.left = new int[] {-3};
		n24.right = new int[] {25,26,29};
		n24.bottom = new int[] {-3};
		
		Node n25 = new Node();
		n25.top = new int[] {-2,6,7,8,33,19,35};
		n25.left = new int[] {24,25,27};
		n25.right = new int[] {25,26,29};
		n25.bottom = new int[] {-3};
		
		Node n26 = new Node();
		n26.top = new int[] {23,20,27};
		n26.left = new int[] {24,25,27};
		n26.right = new int[] {-3};
		n26.bottom = new int[] {-3};
		
		Node n27 = new Node();
		n27.top = new int[] {-2,6,7,8,33,19,35};
		n27.left = new int[] {-2,2,5,8,29,21,35};
		n27.right = new int[] {25,26,29};
		n27.bottom = new int[] {23,26,33};
		
		Node n29 = new Node();
		n29.top = new int[] {-2,6,7,8,33,19,35};
		n29.left = new int[] {24,25,27};
		n29.right = new int[] {-2,27,23,33,0,3,6};
		n29.bottom = new int[] {21,24,35};
		
		Node n31 = new Node();
		n31.top = new int[] {-3,24,25,26};
		n31.left = new int[] {-3,20,23,26};
		n31.right = new int[] {-3,18,21,24};
		n31.bottom = new int[] {-3,18,19,20};
		
		Node n33 = new Node();
		n33.top = new int[] {23,20,27};
		n33.left = new int[] {-2,2,5,8,29,21,35};
		n33.right = new int[] {19,20,35};
		n33.bottom = new int[] {-2,0,1,2,27,25,29};
		
		Node n35 = new Node();
		n35.top = new int[] {21,18,29};
		n35.left = new int[] {18,19,33};
		n35.right = new int[] {-2,27,23,33,0,3,6};
		n35.bottom = new int[] {-2,0,1,2,27,25,29};

		Node n10 = null;
		Node n12 = null;
		Node n14 = null;
		Node n16 = null;
		Node n22 = null;
		Node n28 = null;
		Node n30 = null;
		Node n32 = null;
		Node n34 = null;
		
		nodes = new Node[] {n0, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, n19, n20, n21, n22, n23, n24, n25, n26, n27, n28, n29, n30, n31, n32, n33, n34, n35};
	}
	
	private void loadTiles() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = new Tile(j,i);
			}
		}
		active = new Stack<Tile>();
		propagation = new Stack<Tile>();
	}
	
	private void lowestEntropy() {
		Tile least = active.peek();
		for(Tile t:active) {
			if(t.qStates() < least.qStates()) least = t;
			else if(t.qStates() == least.qStates() && Math.random() > 0.5) least = t;
		}
		// very cringe but cope
		if(least.possible.size() == 0) {
			generate();
			return;
		}
		least.collapse();
		tilesDown[least.y][least.x] = least.state;
		active.remove(least);
		propagation.add(least);

		while(propagation.size() > 0) {
			propagate(propagation.pop());
		}
	}
	
	private void propagate(Tile t) {
		int[][] neighbors = getNeighbor(t);
		for(int i = 0; i < 4; i++) {
			int[] delta = neighbors[i];
			Tile next = map[delta[1]][delta[0]];
			if(next.possible == null) continue;

			int current = next.qStates();
			next.mergeArray(getPossible(t,i));
			if(t.possible == null && !active.contains(next)) active.add(0, next); 
			if(next.qStates() < current) {
				propagation.add(0, next);
			}
		}
	}
	
	private int[][] getNeighbor(Tile t){
		int[][] delta = new int[][] {{0,-1},{-1,0},{1,0},{0,1}};
		int[][] neighbors = new int[4][];
		for(int i = 0; i < 4; i++) {
			int dy = t.y+delta[i][1];
			int dx = t.x+delta[i][0];

			if(dy < 0) dy = map.length-1;
			if(dy > map.length-1) dy = 0;
			if(dx < 0) dx = map[0].length-1;
			if(dx > map[0].length-1) dx = 0;
			
			neighbors[i] = new int[] {dx,dy};
		}
		return neighbors;
	}
	
	private int[] getPossible(Tile t, int direc) {
		if(t.possible == null) {
			int index = t.state;
			if(index < 0) {
				switch(index) {
				case -1: index = 4; break;
				case -2: index = 13; break;
				case -3: index = 31; break;
				}
			}
			switch(direc) {
			case 0: return nodes[index].top;
			case 1: return nodes[index].left;
			case 2: return nodes[index].right;
			case 3: return nodes[index].bottom;
			}
		}
		
		ArrayList<Integer> states = new ArrayList<Integer>();
		for(int i:t.possible) {
			if(i < 0) {
				switch(i) {
				case -1: i = 4; break;
				case -2: i = 13; break;
				case -3: i = 31; break;
				}
			}
			Node n = nodes[i];
			int[] nextSet = null;
			switch(direc) {
			case 0: nextSet = n.top;
				break;
			case 1: nextSet = n.left;
				break;
			case 2: nextSet = n.right;
				break;
			case 3: nextSet = n.bottom;
				break;
			}
			for(int s:nextSet) if(!states.contains(s))states.add(s);
		}
		int[] allStates = new int[states.size()];
		for(int i = 0; i < states.size(); i++) allStates[i] = states.get(i);
		return allStates;
	}
	
	private void setRandom() {
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[0].length; x++) {
				if (tilesDown[y][x] < 0) {
					tilesDown[y][x] = 90 - (tilesDown[y][x]*10) + (int)(Math.random()*9);
				}
			}
		}
	}
	
	/**
	 * Generates a new map
	 */
	public void generate() {
		loadTiles();
		loadNodes();
		map[6][6].possible.clear();
		map[6][6].possible.add(-1);
		active.push(map[6][6]);
		while(active.size() > 0) lowestEntropy();
		setRandom();
	}
	
	/**
	 * Draws the map
	 * @param p PApple to draw to
	 * @post Contents of p are changed
	 */
	public void draw(PApplet p, float dx, float dy) {
		p.push();
		p.background(0);
		p.translate(dx,dy);
		int size = tilesV[0].width;
		int startX = (int)(-dx/size - (p.width/2f)/size-1);
		int startY = (int)(-dy/size - (p.height/2f)/size-1);
		int endX = startX + 2*p.width/size;
		int endY = startY + 2*p.height/size;
		for(int y = startY; y < endY; y++) {
			for(int x = startX; x < endX; x++) {
				int ry = y%map.length;
				int rx = x%map[0].length;
				ry = ry>-1?ry<map.length?ry:ry-map.length:map.length+ry;
				rx = rx>-1?rx<map[0].length?rx:rx-map.length:map.length+rx;
				if(map[ry][rx].state <= -99) continue;
				int T = tilesDown[ry][rx];
				
				if(T >= 0 && T < 36) p.image(tilesV[T],x*size,y*size);
				else if(T < 110) p.image(lava[T-100],x*size,y*size);
				else if(T < 120) p.image(pebble[T-110],x*size,y*size);
				else p.image(cobble[T-120],x*size,y*size);
			}
		}
		p.pop();
	}
}

class Node{
	int[] top,bottom,left,right;
}

class Tile{
	ArrayList<Integer> possible;
	int x,y,state;
	
	public Tile(int a, int b) {
		x = a;
		y = b;
		state = -999;
		possible = new ArrayList<Integer>();
		for(int i = -3; i < 36; i++) possible.add(i);
	}
	
	public int qStates() {
		return possible == null?0:possible.size();
	}
	
	public void mergeArray(int[] other) {
		ArrayList<Integer> replace = new ArrayList<Integer>();
		for(int i:other)if(possible.contains(i)) replace.add(i);
		possible = replace;
	}
	
	public void collapse() {
		for(int i:possible) {
			if(i == -1 && Math.random()>0.1) {
				state = i;
				possible = null;
				return;
			}
		}
		state = possible.get((int)(Math.random()*possible.size()));
		possible = null;
	}
	
	public void setState(int i) {
		state = i;
		possible = null;
	}
	
	public String toString() {
		return "["+x+", "+y+"] "+state+" "+ (possible==null?"NULL":possible.toString());
	}
}
