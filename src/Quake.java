import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Quake extends Animated{
	
	   public   final String QUAKE_KEY = "quake";
	   public   final String QUAKE_ID = "quake";
	   public   final int QUAKE_ACTION_PERIOD = 1100;
	   public   final int QUAKE_ANIMATION_PERIOD = 100;
	   public   final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

	public Quake(String id, Point position, List<PImage> images,
		      int actionPeriod, int animationPeriod
		      ) {
		
		setId(QUAKE_ID);
		setPosition(position);
		setImages(images);
		setImageIndex( 0);
		setActionPeriod(QUAKE_ACTION_PERIOD);
		setAnimationPeriod(QUAKE_ANIMATION_PERIOD);
	}
	
	public Quake(Point position, List<PImage> images) {
		
		setId(QUAKE_ID);
		setPosition(position);
		setImages(images);
		setImageIndex( 0);
		setActionPeriod(QUAKE_ACTION_PERIOD);
		setAnimationPeriod(QUAKE_ANIMATION_PERIOD);
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
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
}
