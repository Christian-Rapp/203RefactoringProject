import java.util.List;

import processing.core.PImage;

public class Ore extends Actionable{
	
	private  final String ORE_KEY = "ore";
	public   final String ORE_ID_PREFIX = "ore -- ";
	   public   final int ORE_CORRUPT_MIN = 20000;
	   public   final int ORE_CORRUPT_MAX = 30000;
	   public   final int ORE_REACH = 1;
	   private  final int ORE_NUM_PROPERTIES = 5;
	   private  final int ORE_ID = 1;
	   private  final int ORE_COL = 2;
	   private  final int ORE_ROW = 3;
	   private  final int ORE_ACTION_PERIOD = 4;
	   
	
	public Ore(String id, Point position, List<PImage> images
		      ) {
		
		setId(ORE_ID_PREFIX + id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod(ORE_CORRUPT_MIN
				+ Functions.getRand().nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN));
		
	}
	
	public Ore(String[] properties, ImageStore imageStore)
	{
		setPosition( new Point(Integer.parseInt(properties[ORE_COL]), Integer.parseInt(properties[ORE_ROW])));
     	setId(properties[ORE_ID]);
        setActionPeriod(Integer.parseInt(properties[ORE_ACTION_PERIOD]));
        setImages(imageStore.getImageList(ORE_KEY));
        setImageIndex(0);
	}
	
	public Ore()
	{
		
	}
	
	public void executeOreActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Point pos = getPosition(); // store current position before removing

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		Entity blob = new Ore_Blob(getId(), pos, imageStore, getActionPeriod());

		world.addEntity(blob);
		((Actionable)blob).scheduleActions(scheduler, world, imageStore);
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeOreActivity(world, imageStore, scheduler);	
	}
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
	
	


	
}
