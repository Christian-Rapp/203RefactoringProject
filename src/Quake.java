import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Quake extends Animated{

	public Quake(String id, Point position, List<PImage> images,
		      int actionPeriod, int animationPeriod
		      ) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		setAnimationPeriod( animationPeriod);
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());	
	}
	
	public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		scheduler.unscheduleAllEvents(this);
		world.removeEntity(this);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeQuakeActivity(world, imageStore, scheduler);	
	}
}
