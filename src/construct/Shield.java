package construct;
/**
 * A shield
 * @author Eddie Ouyang
 *
 */
public class Shield extends Tower{
	public Shield() {
		image = "Shield";
		cost = 100;
		hp = 350;
		weight = 10;
		direction = -1;
	}

	@Override
	public String description() {
		return "Abosorbs damage from\nnearby Towers";
	}

	@Override
	public Tower copySelf() {
		return new Shield();
	}
}
