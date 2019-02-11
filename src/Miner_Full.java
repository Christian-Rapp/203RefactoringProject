import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Miner_Full implements Animated{

	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int resourceLimit;
	public int resourceCount;
	public int actionPeriod;
	public int animationPeriod;

	public Miner_Full(String id, int resourceLimit,
		      Point position, int actionPeriod, int animationPeriod,
		      List<PImage> images) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.resourceLimit = resourceLimit;
		this.resourceCount = resourceLimit;
		this.actionPeriod = actionPeriod;
		this.animationPeriod = animationPeriod;
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
	
	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
			scheduler.scheduleEvent(this, createAnimationAction(0), animationPeriod);	
	}
	
	public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> fullTarget = world.findNearest(position, new Blacksmith());

		if (fullTarget.isPresent() && moveToFull(world, fullTarget.get(), scheduler)) {
			transformFull(world, scheduler, imageStore);
		} else {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
		}
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeMinerFullActivity(world, imageStore, scheduler);	
	}
	
	public ActionInterface createAnimationAction(int repeatCount) {
		return new AnimationAction( this, null, null, repeatCount);
	}

	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	
	public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		EntityInterface miner = world.createMinerNotFull(id, resourceLimit, position, actionPeriod, animationPeriod, images);

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		world.addEntity(miner);
		((Actionable)miner).scheduleActions(scheduler, world, imageStore);
	}
	
	public boolean moveToFull(WorldModel world, EntityInterface target, EventScheduler scheduler) {
		if (position.adjacent(target.getPosition())) {
			return true;
		} else {
			Point nextPos = nextPositionMiner(world, target.getPosition());

			if (!position.equals(nextPos)) {
				Optional<EntityInterface> occupant = world.getOccupant(nextPos);
				if (occupant.isPresent()) {
					scheduler.unscheduleAllEvents(occupant.get());
				}

				world.moveEntity(this, nextPos);
			}
			return false;
		}
	}
	
	public Point nextPositionMiner(WorldModel world, Point destPos) {
		int horiz = Integer.signum(destPos.x - position.x);
		Point newPos = new Point(position.x + horiz, position.y);

		if (horiz == 0 || world.isOccupied(newPos)) {
			int vert = Integer.signum(destPos.y - position.y);
			newPos = new Point(position.x, position.y + vert);

			if (vert == 0 || world.isOccupied(newPos)) {
				newPos = position;
			}
		}

		return newPos;
	}
}
