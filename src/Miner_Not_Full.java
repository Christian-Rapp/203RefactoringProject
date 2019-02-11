import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Miner_Not_Full implements Animated{

	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int resourceLimit;
	public int resourceCount;
	public int actionPeriod;
	public int animationPeriod;

	public Miner_Not_Full(String id, Point position, List<PImage> images, int resourceLimit,
		      int actionPeriod, int animationPeriod) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.resourceLimit = resourceLimit;
		this.resourceCount = 0;
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
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
		scheduler.scheduleEvent(this, createAnimationAction(0), animationPeriod);	

	
	}
	
	public ActionInterface createAnimationAction(int repeatCount) {
		return new AnimationAction( this, null, null, repeatCount);
	}

	public ActionInterface createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 0);
	}
	
	public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> notFullTarget = world.findNearest(position, new Ore());

		if (!notFullTarget.isPresent() || !moveToNotFull(world, notFullTarget.get(), scheduler)
				|| !transformNotFull(world, scheduler, imageStore)) {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
		}
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeMinerNotFullActivity(world, imageStore, scheduler);	
	}
	
	public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		if (resourceCount >= resourceLimit) {
			EntityInterface miner = world.createMinerFull(id, resourceLimit, position, actionPeriod, animationPeriod, images);

			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);

			world.addEntity(miner);
			((Actionable)miner).scheduleActions(scheduler, world, imageStore);

			return true;
		}

		return false;
	}
	
	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	
	public boolean moveToNotFull(WorldModel world, EntityInterface target, EventScheduler scheduler) {
		if (position.adjacent(target.getPosition())) {
			resourceCount += 1;
			world.removeEntity(target);
			scheduler.unscheduleAllEvents(target);

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
