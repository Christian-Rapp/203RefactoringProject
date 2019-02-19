import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Ore_Blob extends Animated{

	public Ore_Blob(String id, Point position, List<PImage> images,
		      int actionPeriod, int animationPeriod
		      ) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		setActionPeriod( actionPeriod);
		setAnimationPeriod( animationPeriod);
	}

	
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());	
}
	
	public void executeOreBlobActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> blobTarget = world.findNearest(getPosition(), new Vein());
		long nextPeriod = getActionPeriod();

		if (blobTarget.isPresent()) {
			Point tgtPos = blobTarget.get().getPosition();

			if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
				EntityInterface quake = world.createQuake(tgtPos, imageStore.getImageList(world.QUAKE_KEY));

				world.addEntity(quake);
				nextPeriod += getActionPeriod();
				((Actionable)quake).scheduleActions(scheduler, world, imageStore);
			}
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
	}
	
	public boolean moveToOreBlob(WorldModel world, EntityInterface entityInterface, EventScheduler scheduler) {
		if (getPosition().adjacent(entityInterface.getPosition())) {
			world.removeEntity(entityInterface);
			scheduler.unscheduleAllEvents(entityInterface);
			return true;
		} else {
			Point nextPos = nextPositionOreBlob(world, entityInterface.getPosition());

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
	
	public Point nextPositionOreBlob(WorldModel world, Point destPos) {
		int horiz = Integer.signum(destPos.x - getPosition().x);
		Point newPos = new Point(getPosition().x + horiz, getPosition().y);

		Optional<EntityInterface> occupant = world.getOccupant(newPos);

		if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(new Ore().getClass())))) {
			int vert = Integer.signum(destPos.y - getPosition().y);
			newPos = new Point(getPosition().x, getPosition().y + vert);
			occupant = world.getOccupant(newPos);

			if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(new Ore().getClass())))) {
				newPos = getPosition();
			}
		}

		return newPos;
	}


	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeOreBlobActivity(world, imageStore, scheduler);	
	}
}


