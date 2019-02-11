
public class AnimationAction implements ActionInterface{

	   public Animated entity;
	   public WorldModel world;
	   public ImageStore imageStore;
	   public int repeatCount;

	   public AnimationAction( EntityInterface entity, WorldModel world,
	      ImageStore imageStore, int repeatCount)
	   {
	      this.entity = (Animated)entity;
	      this.world = world;
	      this.imageStore = imageStore;
	      this.repeatCount = repeatCount;
	   }
	
	public void executeAction(EventScheduler scheduler)
	   {
	         executeAnimationAction( scheduler);
	      }

	public void executeAnimationAction( EventScheduler scheduler)
			   {
			      entity.nextImage();

			      if (repeatCount != 1)
			      {
			         scheduler.scheduleEvent( entity,
			           entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
			            entity.getAnimationPeriod() );
			      }
			   }


	
//	public ActionInterface createAction(EntityInterface entity, WorldModel world, ImageStore imageStore, int repeatCount) {
//		return new AnimationAction(entity, world, imageStore, repeatCount)
//	}
}
