package construct;

import engine.Globals;

public class Farm extends Tower{
	private int resource, amt;
	
	public Farm() {
		image = "Farm";
		cost = 200;
		hp = 75;
		weight = 10;
		resource = -1;
		direction = -1;
	}
	

	@Override
	public String description() {
		return "Generates ["+ (resource==0?"EGG":resource==1?"BACON":"MILK")+"] every\n"+(resource==0?0.5:resource==1?2:3.5)+" seconds";
	}
	
	public Farm(int i) {
		cost = 200;
		hp = 75;
		direction = -1;
		weight = 10;
		setResource(i);
	}

	/**
	 * Generate Resources
	 * @post resources is changed
	 */
	public void farm() {
		amt++;
		switch(resource) {
		case 0: 
			if(amt == 30) {
				Globals.resources[0]++;
				amt = 0;
			}
			break;
		case 1: 
			if(amt == 100) {
				Globals.resources[1]++;
				amt = 0;
			}
			break;
		case 2: 
			if(amt == 180) {
				Globals.resources[2]++;
				amt = 0;
			}
			break;
		
		}
	}
	
	@Override
	public String name() {
		return resource==0?"Chicken":resource==1?"Pig":"Cow";
	}

	@Override
	public void rotate() {
		setResource((resource+1)%3);
	}
	
	public void setResource(int i) {
		resource = i;
		image = "Farm" + i;
	}

	@Override
	public Tower copySelf() {
		return new Farm(resource<0?0:resource);
	}
}
