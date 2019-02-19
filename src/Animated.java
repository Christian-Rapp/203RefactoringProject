
public abstract class Animated extends Actionable{
	
	private int animationPeriod;

	public int getAnimationPeriod() {
		return animationPeriod;
	}	
	
	public ActionInterface createAnimationAction(int repeatCount) {
		return new AnimationAction( this, null, null, repeatCount);
	}

	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		scheduler.scheduleEvent(this, createAnimationAction(0), animationPeriod);	
	
	}

	protected void setAnimationPeriod(int animationPeriod) {
		this.animationPeriod = animationPeriod;
	}
	
}
