import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.*;

public class Turtle extends Animated{

	private final int WALK_LEFT = -2;
	private final int SHELL_LEFT = -1;
	private final int SHELL_RIGHT = 1;
	private final int WALK_RIGHT = 2;
	private int state = 0;
	public Turtle(String id, Point position, List<PImage> images)
	{
		setId(id);
		setPosition(position);
		setImages(images);
		setImageIndex( 0);
		setActionPeriod(1500);
		setAnimationPeriod(400);
	}
	
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Entity> nearest = world.findNearest(getPosition(), new Miner_Not_Full("", null, null, 0, 0, 0));
		Optional<Entity> oreblob = world.findNearest(getPosition(), new Ore_Blob("", null, imageStore, 0));
		if(nearest.isPresent() && nearest.get().getPosition().distanceSquared(getPosition()) < 5)
				{
				setActionPeriod(1500);
				executeShell();
				scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod() + 1500);
				}
		
		else if(!oreblob.isPresent())
		{
			setActionPeriod(1500);
			executeRandomWalk(world);
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
			
		}
		else if (oreblob.isPresent()) {
			setActionPeriod(500);
			executeWalk(oreblob.get().getPosition(), world);
			moveToBlob(world, oreblob.get(), scheduler);
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		}
		
		
	}
	
	public void executeShell()
	{
		if(state == WALK_LEFT || state == SHELL_LEFT)
		{
			state = SHELL_LEFT;
		}
		else
		{
			state = SHELL_RIGHT;
		}
	}
	
	public void executeWalk(Point pos, WorldModel world)
	{
		if(pathStrategyTurtle(world, pos).x > getPosition().x)
		{
			state = WALK_RIGHT;
		}
		else if(pathStrategyTurtle(world,pos).x < getPosition().x)
		{
			state = WALK_LEFT;
		}
	}
	
	public void executeRandomWalk(WorldModel world)
	{
		Point next = world.findNearest(getPosition(), new Blacksmith()).get().getPosition();
		executeWalk(next, world);
		world.moveEntity(this, pathStrategyTurtle(world, next));
	
		
	}
	
	public void nextImage() {
		if(state == WALK_LEFT)
		{
		setImageIndex((getImageIndex() + 1) % 4);
		}
		
		if(state == SHELL_LEFT)
		{
		setImageIndex(4);
		}
		if(state == SHELL_RIGHT)
		{
		setImageIndex(5);
		}
		
		if(state == WALK_RIGHT)
		{
		if(getImageIndex() > 5)
			{
			setImageIndex((((getImageIndex() + 1) - 6) % 4) + 6);
			}
		else
		{
			setImageIndex(6);
		}
		}
		
	}
	
	public boolean moveToBlob(WorldModel world, Entity target, EventScheduler scheduler) {
		if (getPosition().adjacent(target.getPosition())) {
			world.removeEntity(target);
			return true;
		} else {
			Point nextPos = pathStrategyTurtle(world, target.getPosition());

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
	
	public Point pathStrategyTurtle(WorldModel world, Point destPos) {
		Point start = getPosition();
		Point end = destPos;
		Predicate<Point> canPassThrough = point -> !world.isOccupied(point); 
		BiPredicate<Point, Point> withinReach = (begin, finish) -> begin.adjacent(finish);
		
		PathingStrategy  strategy= new AStarStrategy();
		Point point = start;
		try {
		point = strategy.computePath(start, end, canPassThrough, withinReach, strategy.CARDINAL_NEIGHBORS).get(0);}
		
		catch (IndexOutOfBoundsException e)
		{
			
		}
		
		return point;
	}

	@Override
	public <R> R accept(EntityVisitor<R> visitor) {
		// TODO Auto-generated method stub
		return visitor.visit(this);
	}

}
