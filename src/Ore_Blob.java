import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Ore_Blob extends Animated{

	private  final String BLOB_KEY = "blob";
  private  final String BLOB_ID_SUFFIX = " -- blob";
  private  final int BLOB_PERIOD_SCALE = 4;
  private  final int BLOB_ANIMATION_MIN = 50;
  private  final int BLOB_ANIMATION_MAX = 150;
	
	public Ore_Blob(String id, Point position, ImageStore imageStore,
		      int actionPeriod
		      ) {
		
		setId(id + BLOB_ID_SUFFIX);
		setPosition(position);
		setImages(imageStore.getImageList(BLOB_KEY));
		setImageIndex(0);
		setActionPeriod( actionPeriod / BLOB_PERIOD_SCALE);
		setAnimationPeriod(BLOB_ANIMATION_MIN + Functions.getRand().nextInt(BLOB_ANIMATION_MAX -BLOB_ANIMATION_MIN));
	}

	
	
	public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
		scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());	
}
	
	public void executeOreBlobActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		Optional<Entity> blobTarget = world.findNearest(getPosition(), new Vein());
		long nextPeriod = getActionPeriod();

		if (blobTarget.isPresent()) {
			Point tgtPos = blobTarget.get().getPosition();

			if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
				Entity quake = new Quake(tgtPos, imageStore.getImageList(world.QUAKE_KEY));

				world.addEntity(quake);
				nextPeriod += getActionPeriod();
				((Actionable)quake).scheduleActions(scheduler, world, imageStore);
			}
		}

		scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
	}
	
	public boolean moveToOreBlob(WorldModel world, Entity entityInterface, EventScheduler scheduler) {
		if (getPosition().adjacent(entityInterface.getPosition())) {
			world.removeEntity(entityInterface);
			scheduler.unscheduleAllEvents(entityInterface);
			return true;
		} else {
			Point nextPos = nextPositionOreBlob(world, entityInterface.getPosition());

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
	
	public Point nextPositionOreBlob(WorldModel world, Point destPos) {
		int horiz = Integer.signum(destPos.x - getPosition().x);
		Point newPos = new Point(getPosition().x + horiz, getPosition().y);

		Optional<Entity> occupant = world.getOccupant(newPos);

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
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
}


