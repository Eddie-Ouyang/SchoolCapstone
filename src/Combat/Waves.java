package Combat;

import java.util.ArrayList;

import Combat.enemies.*;
import Combat.player.Player;
import engine.Globals;

public class Waves {
	/**
	 * Spawns a random wave 
	 * @param angleToObjective angle to the hangar
	 */
	public static void spawnRandomWave (float angleToObjective){
		int totalWaves = 5;
		int type = (int)(Math.random() * totalWaves);
		
		ArrayList<Enemy> nextWave = getWave(type, angleToObjective);
		
		for (Enemy E : nextWave) Map.enemies.add(E);
	}
	
	private static ArrayList<Enemy> getWave (int type, float angleToObjective){
		ArrayList<Enemy> curWave = new ArrayList<Enemy>();
		float spawnDist = Globals.SCREEN_W / 2;
		int additional = Globals.DIFFICULTY;
		
		
		// [STANDARD WAVE] 5 DemonEnemys surround player
		if (type == 0) {
			int spawnCount = 5 + additional;
			for (int i = 0; i < spawnCount; i++) {
				float angle = randAng();
				curWave.add(new DemonEnemy(xInDir(spawnDist, angle), yInDir(spawnDist, angle)));
			}
		}
		
		// 2 GearEnemys and 2 GateEnemys spawn in front of player
		if (type == 1) {
			int spawnCount = 2 + additional;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new GearEnemy(xInDir(spawnDist, angleToObjective), yInDir(spawnDist, angleToObjective)));
			}
			spawnCount = 2 + additional;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new GateEnemy(xInDir(spawnDist + 50, angleToObjective), yInDir(spawnDist + 50, angleToObjective)));
			}
		}
		
		// 3 DemonEnemys spawn in front of player and 2 GateEnemys flank player on left and right
		if (type == 2) {
			int spawnCount = 3 +additional;
			
			for (int i = 0; i < spawnCount; i++) curWave.add(new DemonEnemy(xInDir(spawnDist, angleToObjective), xInDir(spawnDist, angleToObjective)));
			
			curWave.add(new GateEnemy(xInDir(spawnDist, angleToObjective - (float)Math.PI/2), yInDir(spawnDist, angleToObjective - (float)Math.PI/2)));
			curWave.add(new GateEnemy(xInDir(spawnDist, angleToObjective + (float)Math.PI/2), yInDir(spawnDist, angleToObjective + (float)Math.PI/2)));
		}
		
		// 2 GearEnemys and 3 DemonEnemys spawn in front of player
		if (type == 3) {
			int spawnCount = 2 + additional;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new GearEnemy(xInDir(spawnDist, angleToObjective), yInDir(spawnDist, angleToObjective)));
			}
			spawnCount = 3 + additional;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new DemonEnemy(xInDir(spawnDist + 50, angleToObjective), yInDir(spawnDist + 50, angleToObjective)));
			}
		}
		
		// 1 DemonEnemy and 1 GateEnemy spawn in front of player and 2 DemonEnemys surround player
		if (type == 4) {
			int spawnCount = 1;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new DemonEnemy(xInDir(spawnDist, angleToObjective), yInDir(spawnDist, angleToObjective)));
			}
			spawnCount = 1 + additional;
			for (int i = 0; i < spawnCount; i++) {
				curWave.add(new GateEnemy(xInDir(spawnDist + 50, angleToObjective), yInDir(spawnDist + 50, angleToObjective)));
			}
			spawnCount = 2 + additional;
			for (int i = 0; i < spawnCount; i++) {
				float angle = randAng();
				curWave.add(new DemonEnemy(xInDir(spawnDist, angle), yInDir(spawnDist, angle)));
			}
		}
		
		
		return curWave;
	}

	private static float xInDir(float dist, float angle) { return Player.x + dist * (float)Math.cos(angle); }
	private static float yInDir(float dist, float angle) { return Player.y + dist * (float)Math.sin(angle); }
	private static float randAng() { return (float)(Math.random() * Math.PI * 2); }
}
