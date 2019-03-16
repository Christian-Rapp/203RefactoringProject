import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

public class Monk extends Animated{

	
	public Monk(String id, Point position, List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod(1000);
		setAnimationPeriod(300);
		
	}
	
	public void executeMonkActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Entity> shrine1 = world.findNearest(getPosition(), new DevitoShrine());
		Entity shrine = shrine1.get();
		Point p = new Point(shrine.getPosition().x + 2, shrine.getPosition().y);
		if(getPosition().adjacent(shrine.getPosition()) || getPosition().adjacent(p))
		{
			MonkDying die =new MonkDying(getId(), getPosition(), imageStore.getImageList("monkdie"));
			scheduler.unscheduleAllEvents(this);
			world.removeEntity(this);
		
			world.addEntity(die);
			die.scheduleActions(scheduler, world, imageStore );
		}
		
		else if (!shrine1.isPresent() || !moveToShrine(world, shrine, scheduler)) {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		}
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeMonkActivity(world, imageStore, scheduler);	
	}
	
	public boolean moveToShrine(WorldModel world, Entity target, EventScheduler scheduler) {
		if (getPosition().adjacent(target.getPosition())) {
//			world.removeEntity(target);
			scheduler.unscheduleAllEvents(this);

			return true;
		} else {
			Point nextPos = pathStrategyMonk(world, target.getPosition());

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
	
	public Point pathStrategyMonk(WorldModel world, Point destPos) {
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
