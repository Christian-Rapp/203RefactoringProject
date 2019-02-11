
public class ActivityAction implements ActionInterface{

	   public Actionable entity;
	   public WorldModel world;
	   public ImageStore imageStore;
	   public int repeatCount;

	   public ActivityAction( Actionable entity, WorldModel world,
	      ImageStore imageStore, int repeatCount)
	   {
	      this.entity = entity;
	      this.world = world;
	      this.imageStore = imageStore;
	      this.repeatCount = repeatCount;
	   }

	public void executeAction(EventScheduler scheduler)
	   {
	      executeActivityAction( scheduler);
	
	   }

	public void executeActivityAction( EventScheduler scheduler)
	   {
	      
	      entity.executeActivity(world, imageStore, scheduler);
	   }
	
}
