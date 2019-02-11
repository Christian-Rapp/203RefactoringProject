import java.util.List;

import processing.core.PImage;

public class Ore implements Actionable{

	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int actionPeriod;

	public Ore(String id, Point position, List<PImage> images,
		      int actionPeriod
		      ) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.actionPeriod = actionPeriod;
		
	}
	
	public Ore()
	{
		
	}

	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	
	public int getActionPeriod() {
		
		return actionPeriod;
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
	}
	
	public void executeOreActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Point pos = position; // store current position before removing

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		EntityInterface blob = world.createOreBlob(id + world.getBLOB_ID_SUFFIX(), pos,
				actionPeriod / world.getBLOB_PERIOD_SCALE(),
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
	

	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	


	
}
