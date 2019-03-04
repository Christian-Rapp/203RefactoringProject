import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Vein extends Actionable{

   private  final String VEIN_KEY = "vein";
   private  final int VEIN_NUM_PROPERTIES = 5;
   private  final int VEIN_ID = 1;
   private  final int VEIN_COL = 2;
   private  final int VEIN_ROW = 3;
   private  final int VEIN_ACTION_PERIOD = 4;
	
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
	
	public Vein(String[] properties, ImageStore imageStore)
	{
		setPosition( new Point(Integer.parseInt(properties[VEIN_COL]), Integer.parseInt(properties[VEIN_ROW])));
     	setId(properties[VEIN_ID]);
        setActionPeriod(Integer.parseInt(properties[VEIN_ACTION_PERIOD]));
        setImages(imageStore.getImageList(VEIN_KEY));
        setImageIndex(0);
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
	}
	
	public void executeVeinActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Point> openPt = world.findOpenAround(getPosition());

		if (openPt.isPresent()) {
			Entity ore = new Ore(getId(), openPt.get(),
					imageStore.getImageList(world.getORE_KEY())
					);
			world.addEntity(ore);
			((Actionable)ore).scheduleActions(scheduler, world, imageStore);
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeVeinActivity(world, imageStore, scheduler);	
	}
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
}
