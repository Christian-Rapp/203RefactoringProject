import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein extends Actionable{

	public Vein(String id, Point position, List<PImage> images,
		      int actionPeriod
		      ) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		
	}
	
	public Vein()
	{
		
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
	}
	
	public void executeVeinActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Point> openPt = world.findOpenAround(getPosition());

		if (openPt.isPresent()) {
			EntityInterface ore = world.createOre(world.getORE_ID_PREFIX() + getId(), openPt.get(),
					world.ORE_CORRUPT_MIN
							+ Functions.getRand().nextInt(world.getORE_CORRUPT_MAX() - world.getORE_CORRUPT_MIN()),
					imageStore.getImageList(world.getORE_KEY()));
			world.addEntity(ore);
			((Actionable)ore).scheduleActions(scheduler, world, imageStore);
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeVeinActivity(world, imageStore, scheduler);	
	}
}
