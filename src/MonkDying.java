import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class MonkDying extends Monk{

	public MonkDying(String id, Point position, List<PImage> images) {
		super(id, position, images);
		setActionPeriod(600);
		
	}
	
	
	public void executeMonkActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Entity shrine) {
		
			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		Optional<Entity> shrine = world.findNearest(getPosition(), new DevitoShrine());
		executeMonkActivity(world, imageStore, scheduler, shrine.get());	
	}

}
