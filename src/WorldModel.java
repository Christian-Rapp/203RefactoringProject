import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import processing.core.PImage;

final class WorldModel
{
   public int numRows;
   public int numCols;
   public Background background[][];
   public Entity occupancy[][];
   public Set<Entity> entities;
   
   private  final String BLOB_KEY = "blob";
      
   public   final String ORE_ID_PREFIX = "ore -- ";
   public   final int ORE_CORRUPT_MIN = 20000;
   public   final int ORE_CORRUPT_MAX = 30000;
   public   final int ORE_REACH = 1;
   
   public   final String QUAKE_KEY = "quake";
   public   final String QUAKE_ID = "quake";
//   public   final int QUAKE_ACTION_PERIOD = 1100;
//   public   final int QUAKE_ANIMATION_PERIOD = 100;
//   public   final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   
   private  final String MINER_KEY = "miner";
   private  final int MINER_NUM_PROPERTIES = 7;
   
   private  final String OBSTACLE_KEY = "obstacle";
   private  final int OBSTACLE_NUM_PROPERTIES = 4;
   
   private  final String ORE_KEY = "ore";
   private  final int ORE_NUM_PROPERTIES = 5;
   
   private  final String SMITH_KEY = "blacksmith";
   private  final int SMITH_NUM_PROPERTIES = 4;

   private  final String VEIN_KEY = "vein";
   private  final int VEIN_NUM_PROPERTIES = 5;
   
   

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

public void addEntity(Entity entity)
{
if (withinBounds(entity.getPosition()))
   {
      setOccupancyCell(entity.getPosition(), entity);
      entities.add(entity);
   }
}

public void moveEntity(Entity entity, Point pos)
{
   Point oldPos = entity.getPosition();
   if (withinBounds(pos) && !pos.equals(oldPos))
   {
      setOccupancyCell(oldPos, null);
      removeEntityAt(pos);
      setOccupancyCell(pos, entity);
      entity.setPosition(pos);
   }
}

public void removeEntity( Entity entity)
{
   removeEntityAt(entity.getPosition());
}

public void removeEntityAt(Point pos)
{
   if (withinBounds(pos)
      && getOccupancyCell(pos) != null)
   {
      Entity entity = getOccupancyCell(pos);

      /* this moves the entity just outside of the grid for
         debugging purposes */
      entity.setPosition( new Point(-1, -1));
      entities.remove(entity);
      setOccupancyCell(pos, null);
   }
}

public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

public Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

public void setOccupancyCell(Point pos, Entity entity)
		   {
		      occupancy[pos.y][pos.x] = entity;
		   }

public boolean parseBackground(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == Background.BGND_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
		            Integer.parseInt(properties[Background.BGND_ROW]));
		         String id = properties[Background.BGND_ID];
		         setBackground(pt,
		            new Background(id, imageStore.getImageList(id)));
		      }

		      return properties.length == Background.BGND_NUM_PROPERTIES;
		   }

//public boolean parseBackground(String[] properties, ImageStore imageStore)
//	{
//	   if (properties.length == Background.BGND_NUM_PROPERTIES)
//	   {
//	      Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
//	         Integer.parseInt(properties[Background.BGND_ROW]));
//	      setBackground(pt,
//	         new Background(properties, imageStore));
//	   }
//	
//	   return properties.length == Background.BGND_NUM_PROPERTIES;
//	}

//public boolean parseMiner(String[] properties, ImageStore imageStore)
//		   {
//		      if (properties.length == MINER_NUM_PROPERTIES)
//		      {
//		         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
//		            Integer.parseInt(properties[MINER_ROW]));
//		         EntityInterface entity = createMinerNotFull(properties[MINER_ID],
//		            Integer.parseInt(properties[MINER_LIMIT]),
//		            pt,
//		            Integer.parseInt(properties[MINER_ACTION_PERIOD]),
//		            Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
//		            imageStore.getImageList(MINER_KEY));
//		         tryAddEntity(entity);
//		      }
//
//		      return properties.length == MINER_NUM_PROPERTIES;
//		   }

public boolean parseMiner(String[] properties, ImageStore imageStore)
	{
	   if (properties.length == MINER_NUM_PROPERTIES)
	   {
	      Entity entity = new Miner_Not_Full(properties, imageStore);
	      tryAddEntity(entity);
	   }
	
	   return properties.length == MINER_NUM_PROPERTIES;
	}

//public boolean parseObstacle(String[] properties, ImageStore imageStore)
//		   {
//		      if (properties.length == OBSTACLE_NUM_PROPERTIES)
//		      {
//		         Point pt = new Point(
//		            Integer.parseInt(properties[OBSTACLE_COL]),
//		            Integer.parseInt(properties[OBSTACLE_ROW]));
//		         EntityInterface entity = createObstacle(properties[OBSTACLE_ID],
//		            pt, imageStore.getImageList(OBSTACLE_KEY));
//		         tryAddEntity(entity);
//		      }
//
//		      return properties.length == OBSTACLE_NUM_PROPERTIES;
//		   }

public boolean parseObstacle(String[] properties, ImageStore imageStore)
	{
	   if (properties.length == OBSTACLE_NUM_PROPERTIES)
	   {
	      Entity entity = new Obstacle(properties, imageStore);
	      tryAddEntity(entity);
	   }
	
	   return properties.length == OBSTACLE_NUM_PROPERTIES;
	}

//public boolean parseOre(String[] properties, ImageStore imageStore)
//		   {
//		      if (properties.length == ORE_NUM_PROPERTIES)
//		      {
//		         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
//		            Integer.parseInt(properties[ORE_ROW]));
//		         EntityInterface entity = createOre(properties[ORE_ID],
//		            pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
//		            imageStore.getImageList(ORE_KEY));
//		         tryAddEntity(entity);
//		      }
//
//		      return properties.length == ORE_NUM_PROPERTIES;
//		   }

public boolean parseOre(String[] properties, ImageStore imageStore)
	{
	   if (properties.length == ORE_NUM_PROPERTIES)
	   {
	      Entity entity = new Ore(properties, imageStore);
	      tryAddEntity(entity);
	   }
	
	   return properties.length == ORE_NUM_PROPERTIES;
	}

//public boolean parseSmith(String[] properties, ImageStore imageStore)
//		   {
//		      if (properties.length == SMITH_NUM_PROPERTIES)
//		      {
//		         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
//		            Integer.parseInt(properties[SMITH_ROW]));
//		         EntityInterface entity = createBlacksmith(properties[SMITH_ID],
//		            pt, imageStore.getImageList(SMITH_KEY));
//		         tryAddEntity(entity);
//		      }
//
//		      return properties.length == SMITH_NUM_PROPERTIES;
//		   }

public boolean parseSmith(String[] properties, ImageStore imageStore)
	{
	   if (properties.length == SMITH_NUM_PROPERTIES)
	   {
	      Entity entity = new Blacksmith(properties, imageStore);
	      tryAddEntity(entity);
	   }
	
	   return properties.length == SMITH_NUM_PROPERTIES;
	}

//public boolean parseVein(String[] properties, ImageStore imageStore)
//	   {
//	      if (properties.length == VEIN_NUM_PROPERTIES)
//	      {
//	         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
//	            Integer.parseInt(properties[VEIN_ROW]));
//	         EntityInterface entity = createVein(properties[VEIN_ID],
//	            pt,
//	            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
//	            imageStore.getImageList(VEIN_KEY));
//	         tryAddEntity(entity);
//	      }
//
//	      return properties.length == VEIN_NUM_PROPERTIES;
//	   }

public boolean parseVein(String[] properties, ImageStore imageStore)
	{
	   if (properties.length == VEIN_NUM_PROPERTIES)
	   {
	      Entity entity = new Vein(properties, imageStore);
	      tryAddEntity(entity);
	   }
	
	   return properties.length == VEIN_NUM_PROPERTIES;
	}

public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                  lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
               lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
               lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

public boolean processLine(String line, ImageStore imageStore)
		   {
		      String[] properties = line.split("\\s");
		      if (properties.length > 0)
		      {
		         switch (properties[ImageStore.PROPERTY_KEY])
		         {
		         case Background.BGND_KEY:
		            return parseBackground(properties, imageStore);
		         case MINER_KEY:
		            return parseMiner(properties, imageStore);
		         case OBSTACLE_KEY:
		            return parseObstacle(properties, imageStore);
		         case ORE_KEY:
		            return parseOre(properties, imageStore);
		         case SMITH_KEY:
		            return parseSmith(properties, imageStore);
		         case VEIN_KEY:
		            return parseVein(properties, imageStore);
		         }
		      }

		      return false;
		   }


//public EntityInterface createBlacksmith(String id, Point position,
//      List<PImage> images)
//   {
//      return new Blacksmith(id, position, images);
//   }
//
//	public EntityInterface createMinerFull(String id, int resourceLimit,Point position,
//			int actionPeriod, int animationPeriod, List<PImage> images)
//	{
//		return new Miner_Full(id, animationPeriod, position, animationPeriod, animationPeriod, images);
//	}
//
//   public EntityInterface createMinerNotFull(String id, int resourceLimit,
//      Point position, int actionPeriod, int animationPeriod,
//      List<PImage> images)
//   {
//      return new Miner_Not_Full(id, position, images,
//         resourceLimit, actionPeriod, animationPeriod);
//   }
//
//   public EntityInterface createObstacle(String id, Point position,
//      List<PImage> images)
//   {
//      return new Obstacle(id, position, images);
//   }
//
//   public EntityInterface createOre(String id, Point position, int actionPeriod,
//      List<PImage> images)
//   {
//      return new Ore(id, position, images,
//         actionPeriod);
//   }
//
//   public EntityInterface createOreBlob(String id, Point position,
//      int actionPeriod, int animationPeriod, List<PImage> images)
//   {
//      return new Ore_Blob(id, position, images,
//             actionPeriod, animationPeriod);
//   }
//
//   public EntityInterface createQuake(Point position, List<PImage> images)
//   {
//      return new Quake(QUAKE_ID, position, images,
//         QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//   }
//
//   public EntityInterface createVein(String id, Point position, int actionPeriod,
//      List<PImage> images)
//   {
//      return new Vein( id, position, images,
//         actionPeriod);
//   }

public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
         pos.x >= 0 && pos.x < numCols;
   }

public boolean isOccupied( Point pos)
   {
      return withinBounds( pos) &&
         getOccupancyCell(pos) != null;
   }

public Optional<Entity> findNearest(Point pos, Entity ei)
		   {
		      List<Entity> ofType = new LinkedList<>();
		      for (Entity entity : entities)
		      {
		         if (entity.getClass().equals(ei.getClass()))
		         {
		            ofType.add(entity);
		         }
		      }

		      return nearestEntity(ofType, pos);
		   }

public static Optional<Entity> nearestEntity(List<Entity> entities,
	      Point pos)
	   {
	      if (entities.isEmpty())
	      {
	         return Optional.empty();
	      }
	      else
	      {
	         Entity nearest = entities.get(0);
	         int nearestDistance = nearest.getPosition().distanceSquared(null, pos);

	         for (Entity other : entities)
	         {
	            int otherDistance = other.getPosition().distanceSquared(null, pos);

	            if (otherDistance < nearestDistance)
	            {
	               nearest = other;
	               nearestDistance = otherDistance;
	            }
	         }

	         return Optional.of(nearest);
	      }
	   }

public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds( newPt) &&
               !isOccupied( newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

public Optional<PImage> getBackgroundImage( Point pos)
		   {
		      if (withinBounds( pos))
		      {
		         return Optional.of(ImageStore.getCurrentImage(getBackgroundCell(pos)));
		      }
		      else
		      {
		         return Optional.empty();
		      }
		   }

public void setBackground(Point pos, Background background)
		   {
		      if (withinBounds(pos))
		      {
		         setBackgroundCell( pos, background);
		      }
		   }

public Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

public void setBackgroundCell(Point pos, Background background)
		   {
		      this.background[pos.y][pos.x] = background;
		   }

public int getNumRows() {
	return numRows;
}

public void setNumRows(int numRows) {
	this.numRows = numRows;
}

public int getNumCols() {
	return numCols;
}

public void setNumCols(int numCols) {
	this.numCols = numCols;
}

public Background[][] getBackground() {
	return background;
}

public void setBackground(Background[][] background) {
	this.background = background;
}

public Entity[][] getOccupancy() {
	return occupancy;
}

public void setOccupancy(Entity[][] occupancy) {
	this.occupancy = occupancy;
}

public Set<Entity> getEntities() {
	return entities;
}

public void setEntities(Set<Entity> entities) {
	this.entities = entities;
}

public String getBLOB_KEY() {
	return BLOB_KEY;
}

public String getORE_ID_PREFIX() {
	return ORE_ID_PREFIX;
}

public int getORE_CORRUPT_MIN() {
	return ORE_CORRUPT_MIN;
}

public int getORE_CORRUPT_MAX() {
	return ORE_CORRUPT_MAX;
}

public int getORE_REACH() {
	return ORE_REACH;
}

public String getQUAKE_KEY() {
	return QUAKE_KEY;
}

public String getQUAKE_ID() {
	return QUAKE_ID;
}

public String getMINER_KEY() {
	return MINER_KEY;
}

public int getMINER_NUM_PROPERTIES() {
	return MINER_NUM_PROPERTIES;
}

public String getOBSTACLE_KEY() {
	return OBSTACLE_KEY;
}

public int getOBSTACLE_NUM_PROPERTIES() {
	return OBSTACLE_NUM_PROPERTIES;
}

public String getORE_KEY() {
	return ORE_KEY;
}

public int getORE_NUM_PROPERTIES() {
	return ORE_NUM_PROPERTIES;
}

public String getSMITH_KEY() {
	return SMITH_KEY;
}

public int getSMITH_NUM_PROPERTIES() {
	return SMITH_NUM_PROPERTIES;
}

public String getVEIN_KEY() {
	return VEIN_KEY;
}

public int getVEIN_NUM_PROPERTIES() {
	return VEIN_NUM_PROPERTIES;
}

}
