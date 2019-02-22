
public class ActivityAction extends Action{


	   public ActivityAction( Actionable entity, WorldModel world,
	      ImageStore imageStore, int repeatCount)
	   {
		  super(world, imageStore, repeatCount);
	      setEntity(entity);
	      
	   }

	public void executeAction(EventScheduler scheduler)
	   {
	      executeActivityAction( scheduler);
	
	   }

	public void executeActivityAction( EventScheduler scheduler)
	   {
	      
	      ((Actionable)entity).executeActivity(world, imageStore, scheduler);
	   }
	
}
