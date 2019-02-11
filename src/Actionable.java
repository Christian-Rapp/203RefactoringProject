
public interface Actionable extends EntityInterface{

	public abstract int getActionPeriod();
	
	public abstract void scheduleActions(EventScheduler scheduler, 
			WorldModel world, ImageStore imageStore);
	
	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore);
	
	public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
	
	}
	

