import java.util.List;

import processing.core.PImage;

public class Ore extends Actionable{
	

	public Ore(String id, Point position, List<PImage> images,
		      int actionPeriod
		      ) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		
	}
	
	public Ore()
	{
		
	}
	
	public void executeOreActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Point pos = getPosition(); // store current position before removing

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		EntityInterface blob = world.createOreBlob(getId() + world.getBLOB_ID_SUFFIX(), pos,
				getActionPeriod() / world.getBLOB_PERIOD_SCALE(),
				world.getBLOB_ANIMATION_MIN()
						+ Functions.getRand().nextInt(world.getBLOB_ANIMATION_MAX() - world.getBLOB_ANIMATION_MIN()),
				imageStore.getImageList(world.getBLOB_KEY()));

		world.addEntity(blob);
		((Actionable)blob).scheduleActions(scheduler, world, imageStore);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeOreActivity(world, imageStore, scheduler);	
	}
	

	
	


	
}
