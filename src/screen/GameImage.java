package screen;

import java.util.HashMap;

import engine.Globals;
import processing.core.PImage;
import processing.core.PApplet;

/**
 * Storage for images
 * @author Eddie Ouyang
 *
 */
public class GameImage {
	private static HashMap<String,PImage> getImage,smolImage;
	
	/**
	 * Get an Image 
	 * @param s Name of image
	 * @return PImage
	 */
	public static PImage get(String s) {
		return getImage.get(s);
	}
	
	/**
	 * Get an Icon 
	 * @param s Name of icon
	 * @return PImage
	 */
	public static PImage getSmol(String s) {
		return smolImage.get(s);
	}
	
	/**
	 * Loads images in setup
	 * @param p PApplet to load images
	 */
	public static void loadImage(PApplet p) {
		getImage = new HashMap<String, PImage>();
		smolImage = new HashMap<String, PImage>();
		String[][] keys = new String[][] {
			{"Core","core"},
			{"Grass","grass"},
			{"Farm", "farm/farm"},
			{"Farm0", "farm/farmChicken"},
			{"Farm1", "farm/farmPig"},
			{"Farm2", "farm/farmCow"},
			{"ThrusterN", "thruster/thrusterNorth"},
			{"ThrusterS", "thruster/thrusterSouth"},
			{"ThrusterE", "thruster/thrusterEast"},
			{"ThrusterW", "thruster/thrusterWest"},
			{"EggN", "egg/eggNorth"},
			{"EggS", "egg/eggSouth"},
			{"EggE", "egg/eggEast"},
			{"EggW", "egg/eggWest"},
			{"BaconN", "bacon/baconNorth"},
			{"BaconS", "bacon/baconSouth"},
			{"BaconE", "bacon/baconEast"},
			{"BaconW", "bacon/baconWest"},
			{"MilkN", "milk/milkNorth"},
			{"MilkS", "milk/milkSouth"},
			{"MilkE", "milk/milkEast"},
			{"MilkW", "milk/milkWest"},
			{"Reactor", "powerCell"},
			{"Shield", "shield"},
			{"Hand", "enemyDemon"},
			{"Gate", "enemyGate"},
			{"Gear", "enemyGear"},
			{"Bullet3","bulletAngel"},
			{"Bullet2","bulletGate"},
			{"Bullet1", "bulletGear"},
			{"Bullet0","bulletHand"},
			{"EggShell","egg/eggShell"},
			{"BaconPiece","bacon/baconPiece"},
			{"Calf","milk/calf"},
			{"Bone","milk/bone"},
			{"Arrow","arrow"},
			{"Angel","enemyAngel"}};

		int scale = (int)(1.0*Globals.PLAYER_SIZE/Globals.PLAYER_MODEL_COLS);
		for(String [] s: keys) {
			getImage.put(s[0], p.loadImage("img/" + s[1] + ".png"));
		}
		
		PImage sSheet = p.loadImage("img/miniSheet.png");
		String[][] miniKey = new String[][] {{"EggW","EggN","EggE","EggS"},
											{"MilkW","MilkN","MilkE","MilkS"},
											{"BaconW","BaconN","BaconE","BaconS"},
											{"ThrusterW","ThrusterN","ThrusterE","ThrusterS"},
											{"Farm","Farm0","Farm1","Farm2"},
											{"Shield","Grass","Core","Reactor"}};
		for(int y = 0; y < 6; y++) {
			for(int x = 0; x < 4; x++) {
				PImage i = sSheet.get(x*48, y*48, 48, 48);
				i.resize(scale, scale);
				smolImage.put(miniKey[y][x],i);
			}
		}
	}
}
