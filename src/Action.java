final class Action
{
   public ActionKind kind;
   public EntityInterface entity;
   public WorldModel world;
   public ImageStore imageStore;
   public int repeatCount;

   public Action(ActionKind kind, EntityInterface entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

public void executeAction(EventScheduler scheduler)
   {
      switch (kind)
      {
      case ACTIVITY:
         executeActivityAction( scheduler);
         break;

      case ANIMATION:
         executeAnimationAction( scheduler);
         break;
      }
   }

public void executeAnimationAction( EventScheduler scheduler)
		   {
		      entity.nextImage();

		      if (repeatCount != 1)
		      {
		         scheduler.scheduleEvent( entity,
		            ((Animated) entity).createAnimationAction(Math.max(repeatCount - 1, 0)),
		            ((Animated) entity).getAnimationPeriod() );
		      }
		   }

public void executeActivityAction( EventScheduler scheduler)
		   {
		      
		      ((Actionable)entity).executeActivity(world, imageStore, scheduler);
		   }
}
