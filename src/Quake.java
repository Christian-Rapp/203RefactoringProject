import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Quake implements Animated{

	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int actionPeriod;
	public int animationPeriod;

	public Quake(String id, Point position, List<PImage> images,
		      int actionPeriod, int animationPeriod
		      ) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.actionPeriod = actionPeriod;
		this.animationPeriod = animationPeriod;
	}
	

	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	
	public int getActionPeriod() {
		// TODO Auto-generated method stub
		return actionPeriod;
	}

	
	public int getAnimationPeriod() {
		// TODO Auto-generated method stub
		return animationPeriod;
	}
	
	public void nextImage() {
		imageIndex = (imageIndex + 1) % images.size();
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public void setPosition(Point point)
	{
		this.position = point;
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
		scheduler.scheduleEvent(this, createAnimationAction(0), animationPeriod);	
	}
	
	public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		scheduler.unscheduleAllEvents(this);
		world.removeEntity(this);
	}

	public ActionInterface createAnimationAction(int repeatCount) {
		return new AnimationAction( this, null, null, repeatCount);
	}

	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeQuakeActivity(world, imageStore, scheduler);	
	}
}
