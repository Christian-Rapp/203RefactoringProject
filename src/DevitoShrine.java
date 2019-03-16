import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class DevitoShrine extends Actionable{

	private final String DEVITO = "shrine";
	public   final int SHRINE_ACTION_PERIOD = 800;
	//public   final int SHRINE_ANIMATION_PERIOD = 100;
	public int stage = 0;

	
	
	public DevitoShrine(String id, Point position, List<PImage> images)
	{
		setId(id);
		setPosition(position);
		setImages(images);
		setActionPeriod(SHRINE_ACTION_PERIOD);
	}
	
	
	public DevitoShrine() {
		// TODO Auto-generated constructor stub
	}


	public PImage getCurrentImage() {
		return null;
	}

	public void setPosition(Point position)
	{
		super.setPosition(position);
	}
			
	
	public <R> R accept(EntityVisitor<R> visitor) {
		return visitor.visit(this);
	}

	
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		
		if(stage < 3)
		{
			Optional<Entity> target = world.findNearest(getPosition(), new Vein());
			long nextPeriod = getActionPeriod();

			if (target.isPresent() && target.get().getPosition().distanceSquared(getPosition()) < 17) {
				Point tgtPos = target.get().getPosition();
				Entity quake = new Quake(tgtPos, imageStore.getImageList(world.QUAKE_KEY));
				world.removeEntityAt(tgtPos);
				world.addEntity(quake);
				nextPeriod += getActionPeriod();
				((Actionable)quake).scheduleActions(scheduler, world, imageStore);
				}
		}
//		if(stage == 1)
//		{
//			
//			world.removeEntityAt(new Point(0,0));
//			Monk monk =new Monk("MONK1", new Point(0,0), imageStore.getImageList("monk"));
//			world.tryAddEntity(monk);
//			monk.scheduleActions(scheduler, world, imageStore);
//			
//			world.removeEntityAt(new Point(0,40));
//			Monk monk2 =new Monk("MONK2", new Point(0,40), imageStore.getImageList("monk"));
//			world.tryAddEntity(monk2);
//			monk2.scheduleActions(scheduler, world, imageStore);
//			
//			world.removeEntityAt(new Point(30,42));
//			Monk monk3 =new Monk("MONK3", new Point(30,42), imageStore.getImageList("monk"));
//			world.tryAddEntity(monk3);
//			monk3.scheduleActions(scheduler, world, imageStore);
//			
//			world.removeEntityAt(new Point(30,0));
//			Monk monk4 =new Monk("MONK4", new Point(30,0), imageStore.getImageList("monk"));
//			world.tryAddEntity(monk4);
//			monk4.scheduleActions(scheduler, world, imageStore);
//		}
		if(stage < 5) {
			Optional<Entity> target = world.findNearest(getPosition(), new Miner_Not_Full("", null, null, 0, 0, 0));
			if(target.isPresent())
			{
				Entity miner = target.get();
				Monk monk = new Monk("Monk", miner.getPosition(), imageStore.getImageList("monk"));
				world.removeEntity(miner);
				world.addEntity(monk);
				monk.scheduleActions(scheduler, world, imageStore);
			}
			
			stage++;
			world.stageDanny(getPosition().x + 1, getPosition().y + 1, getImages(), stage);
			this.scheduleActions(scheduler, world, imageStore);
			
//			Entity quake1 = new Quake(new Point(getPosition().x - 1, getPosition().y + 1), imageStore.getImageList(world.QUAKE_KEY));
//			Entity quake2 = new Quake(new Point(getPosition().x - 1, getPosition().y), imageStore.getImageList(world.QUAKE_KEY));
//			Entity quake3 = new Quake(new Point(getPosition().x + 1, getPosition().y + 2), imageStore.getImageList(world.QUAKE_KEY));
//			world.addEntity(quake1);
//			world.addEntity(quake2);
//			world.addEntity(quake3);
	}
	}
	
	public Action createActivityAction(WorldModel world, ImageStore imageStore) {
		return new ActivityAction( this, world, imageStore, 4);
	}
	

}
