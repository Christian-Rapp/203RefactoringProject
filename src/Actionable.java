
public abstract class Actionable extends Entity{

	private int actionPeriod;
	
	protected void setActionPeriod(int actionPeriod) {
		this.actionPeriod = actionPeriod;
	}

	public int getActionPeriod() {
		
		return actionPeriod;
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
	}
	
	
	public Action createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	
	public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
	
	}
	

