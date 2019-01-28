final class Action
{
   public ActionKind kind;
   public Entity entity;
   public WorldModel world;
   public ImageStore imageStore;
   public int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

public void executeAction(Functions functions, EventScheduler scheduler)
   {
      switch (kind)
      {
      case ACTIVITY:
         executeActivityAction(functions, scheduler);
         break;

      case ANIMATION:
         executeAnimationAction(functions, scheduler);
         break;
      }
   }

public void executeAnimationAction(Functions functions, EventScheduler scheduler)
		   {
		      entity.nextImage(functions);

		      if (repeatCount != 1)
		      {
		         scheduler.scheduleEvent(functions, entity,
		            entity.createAnimationAction(Math.max(repeatCount - 1, 0)),
		            entity.getAnimationPeriod(functions));
		      }
		   }

public void executeActivityAction(Functions functions, EventScheduler scheduler)
		   {
		      switch (entity.kind)
		      {
		      case MINER_FULL:
		         entity.executeMinerFullActivity(functions, world,
		            imageStore, scheduler);
		         break;

		      case MINER_NOT_FULL:
		         entity.executeMinerNotFullActivity(functions, world,
		            imageStore, scheduler);
		         break;

		      case ORE:
		         entity.executeOreActivity(functions, world, imageStore,
		            scheduler);
		         break;

		      case ORE_BLOB:
		         entity.executeOreBlobActivity(functions, world,
		            imageStore, scheduler);
		         break;

		      case QUAKE:
		         entity.executeQuakeActivity(functions, world, imageStore,
		            scheduler);
		         break;

		      case VEIN:
		         entity.executeVeinActivity(functions, world, imageStore,
		            scheduler);
		         break;

		      default:
		         throw new UnsupportedOperationException(
		            String.format("executeActivityAction not supported for %s",
		            entity.kind));
		      }
		   }
}
