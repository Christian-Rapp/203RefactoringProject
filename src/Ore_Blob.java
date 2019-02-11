import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Ore_Blob implements Animated{
	public String id;
	public Point position;
	public List<PImage> images;
	public int imageIndex;
	public int actionPeriod;
	public int animationPeriod;

	public Ore_Blob(String id, Point position, List<PImage> images,
		      int actionPeriod, int animationPeriod
		      ) {
		
		this.id = id;
		this.position = position;
		this.images = images;
		this.imageIndex = 0;
		this.actionPeriod = actionPeriod;
		this.animationPeriod = animationPeriod;
	}

	
	public int getAnimationPeriod() {
		return animationPeriod;
	}


	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}
	
	public int getActionPeriod() {
		return actionPeriod;
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
	
	public void executeOreBlobActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<EntityInterface> blobTarget = world.findNearest(position, new Vein());
		long nextPeriod = actionPeriod;

		if (blobTarget.isPresent()) {
			Point tgtPos = blobTarget.get().getPosition();

			if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
				EntityInterface quake = world.createQuake(tgtPos, imageStore.getImageList(world.QUAKE_KEY));

				world.addEntity(quake);
				nextPeriod += actionPeriod;
				((Actionable)quake).scheduleActions(scheduler, world, imageStore);
			}
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
	}
	
	public boolean moveToOreBlob(WorldModel world, EntityInterface entityInterface, EventScheduler scheduler) {
		if (position.adjacent(entityInterface.getPosition())) {
			world.removeEntity(entityInterface);
			scheduler.unscheduleAllEvents(entityInterface);
			return true;
		} else {
			Point nextPos = nextPositionOreBlob(world, entityInterface.getPosition());

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
	
	public Point nextPositionOreBlob(WorldModel world, Point destPos) {
		int horiz = Integer.signum(destPos.x - position.x);
		Point newPos = new Point(position.x + horiz, position.y);

		Optional<EntityInterface> occupant = world.getOccupant(newPos);

		if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(new Ore().getClass())))) {
			int vert = Integer.signum(destPos.y - position.y);
			newPos = new Point(position.x, position.y + vert);
			occupant = world.getOccupant(newPos);

			if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(new Ore().getClass())))) {
				newPos = position;
			}
		}

		return newPos;
	}


	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
	{
		executeOreBlobActivity(world, imageStore, scheduler);	
	}
}


