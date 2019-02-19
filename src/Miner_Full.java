import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Miner_Full extends Animated{

	public int resourceLimit;
	public int resourceCount;

	public Miner_Full(String id, int resourceLimit,
		      Point position, int actionPeriod, int animationPeriod,
		      List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		setAnimationPeriod( animationPeriod);
		this.resourceLimit = resourceLimit;
		this.resourceCount = resourceLimit;
	}


	
	public void executeMinerFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> fullTarget = world.findNearest(getPosition(), new Blacksmith());

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
		EntityInterface miner = world.createMinerNotFull(getId(), resourceLimit, getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());

		world.removeEntity(this);
		scheduler.unscheduleAllEvents(this);

		world.addEntity(miner);
		((Actionable)miner).scheduleActions(scheduler, world, imageStore);
	}
	
	public boolean moveToFull(WorldModel world, EntityInterface target, EventScheduler scheduler) {
		if (getPosition().adjacent(target.getPosition())) {
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
