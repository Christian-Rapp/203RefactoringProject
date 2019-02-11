import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein implements Actionable{

	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int actionPeriod;

	public Vein(String id, Point position, List<PImage> images,
		      int actionPeriod
		      ) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.actionPeriod = actionPeriod;
		
	}
	
	public Vein()
	{
		
	}
	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	public int getActionPeriod() {
		// TODO Auto-generated method stub
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
	

	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	
	
	public void executeVeinActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Point> openPt = world.findOpenAround(position);

		if (openPt.isPresent()) {
			EntityInterface ore = world.createOre(world.getORE_ID_PREFIX() + id, openPt.get(),
					world.ORE_CORRUPT_MIN
							+ Functions.getRand().nextInt(world.getORE_CORRUPT_MAX() - world.getORE_CORRUPT_MIN()),
					imageStore.getImageList(world.getORE_KEY()));
			world.addEntity(ore);
			((Actionable)ore).scheduleActions(scheduler, world, imageStore);
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeVeinActivity(world, imageStore, scheduler);	
	}
}
