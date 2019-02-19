import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Miner_Not_Full extends Animated{

	public int resourceLimit;
	public int resourceCount;

	public Miner_Not_Full(String id, Point position, List<PImage> images, int resourceLimit,
		      int actionPeriod, int animationPeriod) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		setAnimationPeriod( animationPeriod);
		this.resourceLimit = resourceLimit;
		this.resourceCount = 0;
		
	}
	
	
	public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> notFullTarget = world.findNearest(getPosition(), new Ore());

		if (!notFullTarget.isPresent() || !moveToNotFull(world, notFullTarget.get(), scheduler)
				|| !transformNotFull(world, scheduler, imageStore)) {
			scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		}
	}
	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeMinerNotFullActivity(world, imageStore, scheduler);	
	}
	
	public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		if (resourceCount >= resourceLimit) {
			EntityInterface miner = world.createMinerFull(getId(), resourceLimit, getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());

			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);

			world.addEntity(miner);
			((Actionable)miner).scheduleActions(scheduler, world, imageStore);

			return true;
		}

		return false;
	}
	
	public boolean moveToNotFull(WorldModel world, EntityInterface target, EventScheduler scheduler) {
		if (getPosition().adjacent(target.getPosition())) {
			resourceCount += 1;
			world.removeEntity(target);
			scheduler.unscheduleAllEvents(target);

			return true;
		} else {
			Point nextPos = nextPositionMiner(world, target.getPosition());

			if (!getPosition().equals(nextPos)) {
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
}
