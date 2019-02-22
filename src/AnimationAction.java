
public class AnimationAction extends Action{

	   

	   public AnimationAction(Animated entity, WorldModel world,
	      ImageStore imageStore, int repeatCount)
	   {
		   super(world, imageStore, repeatCount);
		   setEntity(entity);
	      
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
			           ((Animated)entity).createAnimationAction(Math.max(repeatCount - 1, 0)),
			            ((Animated)entity).getAnimationPeriod() );
			      }
			   }
}
