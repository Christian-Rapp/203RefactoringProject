
public abstract class Action {
	public Entity entity;
	   public WorldModel world;
	   public ImageStore imageStore;
	   public int repeatCount;
	   
	  public Action(WorldModel world,
	      ImageStore imageStore,
	      int repeatCount)
	  {
		  this.world = world;
	      this.imageStore = imageStore;
	      this.repeatCount = repeatCount;
	  }
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public WorldModel getWorld() {
		return world;
	}

	public void setWorld(WorldModel world) {
		this.world = world;
	}

	public ImageStore getImageStore() {
		return imageStore;
	}

	public void setImageStore(ImageStore imageStore) {
		this.imageStore = imageStore;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public abstract void executeAction(EventScheduler eventScheduler);
	//public abstract ActionInterface createAction();
}
