
public interface Animated extends Actionable{

	public abstract int getAnimationPeriod();
	
	public abstract ActionInterface createAnimationAction(int repeatCount);

	
}
