//import java.util.List;
//import java.util.Optional;
//
//import processing.core.PImage;
//
//final class Entity {
//	public EntityKind kind;
//	public String id;
//	public Point position;
//	public List<PImage> images;
//	public int imageIndex;
//	public int resourceLimit;
//	public int resourceCount;
//	public int actionPeriod;
//	public int animationPeriod;
//
//
//	public Entity(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
//			int actionPeriod, int animationPeriod) {
//		this.kind = kind;
//		this.id = id;
//		this.position = position;
//		this.images = images;
//		this.imageIndex = 0;
//		this.resourceLimit = resourceLimit;
//		this.resourceCount = resourceCount;
//		this.actionPeriod = actionPeriod;
//		this.animationPeriod = animationPeriod;
//	}
//
//	public void nextImage() {
//		imageIndex = (imageIndex + 1) % images.size();
//	}
//	
//
//
//
//	public EntityKind getKind() {
//		return kind;
//	}
//
//	public void setKind(EntityKind kind) {
//		this.kind = kind;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public Point getPosition() {
//		return position;
//	}
//
//	public void setPosition(Point position) {
//		this.position = position;
//	}
//
//	public List<PImage> getImages() {
//		return images;
//	}
//
//	public void setImages(List<PImage> images) {
//		this.images = images;
//	}
//
//	public int getImageIndex() {
//		return imageIndex;
//	}
//
//	public void setImageIndex(int imageIndex) {
//		this.imageIndex = imageIndex;
//	}
//
//	public int getResourceLimit() {
//		return resourceLimit;
//	}
//
//	public void setResourceLimit(int resourceLimit) {
//		this.resourceLimit = resourceLimit;
//	}
//
//	public int getResourceCount() {
//		return resourceCount;
//	}
//
//	public void setResourceCount(int resourceCount) {
//		this.resourceCount = resourceCount;
//	}
//
//	public int getActionPeriod() {
//		return actionPeriod;
//	}
//
//	public void setActionPeriod(int actionPeriod) {
//		this.actionPeriod = actionPeriod;
//	}
//
//	public int getAnimationPeriod1() {
//		return animationPeriod;
//	}
//
//	public void setAnimationPeriod(int animationPeriod) {
//		this.animationPeriod = animationPeriod;
//	}
//
////	public String getBLOB_KEY() {
////		return BLOB_KEY;
////	}
////	
////	public String getBLOB_ID_SUFFIX() {
////		return BLOB_ID_SUFFIX;
////	}
////	
////	public int getBLOB_PERIOD_SCALE() {
////		return BLOB_PERIOD_SCALE;
////	}
////	
////	public int getBLOB_ANIMATION_MIN() {
////		return BLOB_ANIMATION_MIN;
////	}
////	
////	public int getBLOB_ANIMATION_MAX() {
////		return BLOB_ANIMATION_MAX;
////	}
////	
////	public String getORE_ID_PREFIX() {
////		return ORE_ID_PREFIX;
////	}
////	
////	public int getORE_CORRUPT_MIN() {
////		return ORE_CORRUPT_MIN;
////	}
////	
////	public int getORE_CORRUPT_MAX() {
////		return ORE_CORRUPT_MAX;
////	}
////	
////	public int getORE_REACH() {
////		return ORE_REACH;
////	}
////	
////	public String getQUAKE_KEY() {
////		return QUAKE_KEY;
////	}
////	
////	public String getQUAKE_ID() {
////		return QUAKE_ID;
////	}
////	
////	public int getQUAKE_ACTION_PERIOD() {
////		return QUAKE_ACTION_PERIOD;
////	}
////	
////	public int getQUAKE_ANIMATION_PERIOD() {
////		return QUAKE_ANIMATION_PERIOD;
////	}
////	
////	public int getQUAKE_ANIMATION_REPEAT_COUNT() {
////		return QUAKE_ANIMATION_REPEAT_COUNT;
////	}
//
//}
////	
////	public String getMINER_KEY() {
////		return MINER_KEY;
////	}
////	
////	public int getMINER_NUM_PROPERTIES() {
////		return MINER_NUM_PROPERTIES;
////	}
////	
////	public int getMINER_ID() {
////		return MINER_ID;
////	}
////	
////	public int getMINER_COL() {
////		return MINER_COL;
////	}
////	
////	public int getMINER_ROW() {
////		return MINER_ROW;
////	}
////	
////	public int getMINER_LIMIT() {
////		return MINER_LIMIT;
////	}
////	
////	public int getMINER_ACTION_PERIOD() {
////		return MINER_ACTION_PERIOD;
////	}
////	
////	public int getMINER_ANIMATION_PERIOD() {
////		return MINER_ANIMATION_PERIOD;
////	}
////	
////	public String getOBSTACLE_KEY() {
////		return OBSTACLE_KEY;
////	}
////	
////	public int getOBSTACLE_NUM_PROPERTIES() {
////		return OBSTACLE_NUM_PROPERTIES;
////	}
////	
////	public int getOBSTACLE_ID() {
////		return OBSTACLE_ID;
////	}
////	
////	public int getOBSTACLE_COL() {
////		return OBSTACLE_COL;
////	}
////	
////	public int getOBSTACLE_ROW() {
////		return OBSTACLE_ROW;
////	}
////	
////	public String getORE_KEY() {
////		return ORE_KEY;
////	}
////	
////	public int getORE_NUM_PROPERTIES() {
////		return ORE_NUM_PROPERTIES;
////	}
////	
////	public int getORE_ID() {
////		return ORE_ID;
////	}
////	
////	public int getORE_COL() {
////		return ORE_COL;
////	}
////	
////	public int getORE_ROW() {
////		return ORE_ROW;
////	}
////	
////	public int getORE_ACTION_PERIOD() {
////		return ORE_ACTION_PERIOD;
////	}
////	
////	public String getSMITH_KEY() {
////		return SMITH_KEY;
////	}
////	
////	public int getSMITH_NUM_PROPERTIES() {
////		return SMITH_NUM_PROPERTIES;
////	}
////	
////	public int getSMITH_ID() {
////		return SMITH_ID;
////	}
////	
////	public int getSMITH_COL() {
////		return SMITH_COL;
////	}
////	
////	public int getSMITH_ROW() {
////		return SMITH_ROW;
////	}
////	
////	public String getVEIN_KEY() {
////		return VEIN_KEY;
////	}
////	
////	public int getVEIN_NUM_PROPERTIES() {
////		return VEIN_NUM_PROPERTIES;
////	}
////	
////	public int getVEIN_ID() {
////		return VEIN_ID;
////	}
////	
////	public int getVEIN_COL() {
////		return VEIN_COL;
////	}
////	
////	public int getVEIN_ROW() {
////		return VEIN_ROW;
////	}
////	
////	public int getVEIN_ACTION_PERIOD() {
////		return VEIN_ACTION_PERIOD;
////	}
////   
////}
