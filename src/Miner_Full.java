import java.util.List;
import java.util.Optional;
import java.util.function.*;

import processing.core.PImage;

public class Miner_Full extends Animated{

	public int resourceLimit;
	public int resourceCount;
	private  final String MINER_KEY = "miner";
	private  final int MINER_NUM_PROPERTIES = 7;
	private  final int MINER_ID = 1;
	private  final int MINER_COL = 2;
	private  final int MINER_ROW = 3;
	private  final int MINER_LIMIT = 4;
	private  final int MINER_ACTION_PERIOD = 5;
	private  final int MINER_ANIMATION_PERIOD = 6;
  

	public Miner_Full(String id, int resourceLimit,
		      Point position, int actionPeriod, int animationPeriod,
		      List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages(images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		setAnimationPeriod( animationPeriod);
		this.resourceLimit = resourceLimit;
		this.resourceCount = resourceLimit;
		
	}
	
	public Miner_Full(String[] properties, ImageStore imageStore)
	{
		setPosition( new Point(Integer.parseInt(properties[MINER_COL]), Integer.parseInt(properties[MINER_ROW])));
     	setId(properties[MINER_ID]);
        resourceLimit = Integer.parseInt(properties[MINER_LIMIT]);
        setActionPeriod(Integer.parseInt(properties[MINER_ACTION_PERIOD]));
        setAnimationPeriod(Integer.parseInt(properties[MINER_ANIMATION_PERIOD]));
        setImages(imageStore.getImageList(MINER_KEY));
        this.resourceCount = resourceLimit;
        setImageIndex(0);
	}
	


	
	public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Entity> fullTarget = world.findNearest(getPosition(), new Blacksmith());

		if (fullTarget.isPresent() && moveToFull(world, fullTarget.get(), scheduler)) {
			transformFull(world, scheduler, imageStore);
		} else {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		}
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeMinerFullActivity(world, imageStore, scheduler);	
	}
	
	public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		Entity miner = new Miner_Not_Full(getId(), getPosition(), getImages(), resourceLimit , getActionPeriod(), getAnimationPeriod());

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		world.addEntity(miner);
		((Actionable)miner).scheduleActions(scheduler, world, imageStore);
	}
	
	public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
		if (getPosition().adjacent(target.getPosition())) {
			return true;
		} else {
			Point nextPos = pathStrategyMiner(world, target.getPosition());

			if (!getPosition().equals(nextPos)) {
				Optional<Entity> occupant = world.getOccupant(nextPos);
				if (occupant.isPresent()) {
					scheduler.unscheduleAllEvents(occupant.get());
				}

				world.moveEntity(this, nextPos);
			}
			return false;
		}
	}
	
	public Point nextPositionMiner(WorldModel world, Point destPos) {
		int horiz = Integer.signum(destPos.x - getPosition().x);
		Point newPos = new Point(getPosition().x + horiz, getPosition().y);

		if (horiz == 0 || world.isOccupied(newPos)) {
			int vert = Integer.signum(destPos.y - getPosition().y);
			newPos = new Point(getPosition().x, getPosition().y + vert);

			if (vert == 0 || world.isOccupied(newPos)) {
				newPos = getPosition();
			}
		}

		return newPos;
	}
	
	public Point pathStrategyMiner(WorldModel world, Point destPos) {
		Point start = getPosition();
		Point end = destPos;
		Predicate<Point> canPassThrough = point -> !world.isOccupied(point); 
		BiPredicate<Point, Point> withinReach = (begin, finish) -> begin.adjacent(finish);
		
//		PathingStrategy  strategy= new SingleStepPathingStrategy();
		PathingStrategy  strategy= new AStarStrategy();
		Point point = start;
		try {
		point = strategy.computePath(start, end, canPassThrough, withinReach, strategy.CARDINAL_NEIGHBORS).get(0);}
		
		catch (IndexOutOfBoundsException e)
		{
			
		}
		
		return point;	
		
	}
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
}
