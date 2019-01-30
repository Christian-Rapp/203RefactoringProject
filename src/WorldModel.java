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
   private  final String BLOB_ID_SUFFIX = " -- blob";
   private  final int BLOB_PERIOD_SCALE = 4;
   private  final int BLOB_ANIMATION_MIN = 50;
   private  final int BLOB_ANIMATION_MAX = 150;
      
   public   final String ORE_ID_PREFIX = "ore -- ";
   public   final int ORE_CORRUPT_MIN = 20000;
   public   final int ORE_CORRUPT_MAX = 30000;
   public   final int ORE_REACH = 1;
   
   public   final String QUAKE_KEY = "quake";
   public   final String QUAKE_ID = "quake";
   public   final int QUAKE_ACTION_PERIOD = 1100;
   public   final int QUAKE_ANIMATION_PERIOD = 100;
   public   final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   
   private  final String MINER_KEY = "miner";
   private  final int MINER_NUM_PROPERTIES = 7;
   private  final int MINER_ID = 1;
   private  final int MINER_COL = 2;
   private  final int MINER_ROW = 3;
   private  final int MINER_LIMIT = 4;
   private  final int MINER_ACTION_PERIOD = 5;
   private  final int MINER_ANIMATION_PERIOD = 6;
   
   private  final String OBSTACLE_KEY = "obstacle";
   private  final int OBSTACLE_NUM_PROPERTIES = 4;
   private  final int OBSTACLE_ID = 1;
   private  final int OBSTACLE_COL = 2;
   private  final int OBSTACLE_ROW = 3;
   
   private  final String ORE_KEY = "ore";
   private  final int ORE_NUM_PROPERTIES = 5;
   private  final int ORE_ID = 1;
   private  final int ORE_COL = 2;
   private  final int ORE_ROW = 3;
   private  final int ORE_ACTION_PERIOD = 4;
   
   private  final String SMITH_KEY = "blacksmith";
   private  final int SMITH_NUM_PROPERTIES = 4;
   private  final int SMITH_ID = 1;
   private  final int SMITH_COL = 2;
   private  final int SMITH_ROW = 3;
   
   private  final String VEIN_KEY = "vein";
   private  final int VEIN_NUM_PROPERTIES = 5;
   private  final int VEIN_ID = 1;
   private  final int VEIN_COL = 2;
   private  final int VEIN_ROW = 3;
   private  final int VEIN_ACTION_PERIOD = 4;
   
   

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
if (withinBounds(entity.position))
   {
      setOccupancyCell(entity.position, entity);
      entities.add(entity);
   }
}

public void moveEntity(Entity entity, Point pos)
{
   Point oldPos = entity.position;
   if (withinBounds(pos) && !pos.equals(oldPos))
   {
      setOccupancyCell(oldPos, null);
      removeEntityAt(pos);
      setOccupancyCell(pos, entity);
      entity.position = pos;
   }
}

public void removeEntity( Entity entity)
{
   removeEntityAt(entity.position);
}

public void removeEntityAt(Point pos)
{
   if (withinBounds(pos)
      && getOccupancyCell(pos) != null)
   {
      Entity entity = getOccupancyCell(pos);

      /* this moves the entity just outside of the grid for
         debugging purposes */
      entity.position = new Point(-1, -1);
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

public boolean parseMiner(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == MINER_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
		            Integer.parseInt(properties[MINER_ROW]));
		         Entity entity = createMinerNotFull(properties[MINER_ID],
		            Integer.parseInt(properties[MINER_LIMIT]),
		            pt,
		            Integer.parseInt(properties[MINER_ACTION_PERIOD]),
		            Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
		            imageStore.getImageList(MINER_KEY));
		         tryAddEntity(entity);
		      }

		      return properties.length == MINER_NUM_PROPERTIES;
		   }

public boolean parseObstacle(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == OBSTACLE_NUM_PROPERTIES)
		      {
		         Point pt = new Point(
		            Integer.parseInt(properties[OBSTACLE_COL]),
		            Integer.parseInt(properties[OBSTACLE_ROW]));
		         Entity entity = createObstacle(properties[OBSTACLE_ID],
		            pt, imageStore.getImageList(OBSTACLE_KEY));
		         tryAddEntity(entity);
		      }

		      return properties.length == OBSTACLE_NUM_PROPERTIES;
		   }

public boolean parseOre(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == ORE_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
		            Integer.parseInt(properties[ORE_ROW]));
		         Entity entity = createOre(properties[ORE_ID],
		            pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
		            imageStore.getImageList(ORE_KEY));
		         tryAddEntity(entity);
		      }

		      return properties.length == ORE_NUM_PROPERTIES;
		   }

public boolean parseSmith(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == SMITH_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
		            Integer.parseInt(properties[SMITH_ROW]));
		         Entity entity = createBlacksmith(properties[SMITH_ID],
		            pt, imageStore.getImageList(SMITH_KEY));
		         tryAddEntity(entity);
		      }

		      return properties.length == SMITH_NUM_PROPERTIES;
		   }

public boolean parseVein(String[] properties, ImageStore imageStore)
		   {
		      if (properties.length == VEIN_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
		            Integer.parseInt(properties[VEIN_ROW]));
		         Entity entity = createVein(properties[VEIN_ID],
		            pt,
		            Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
		            imageStore.getImageList(VEIN_KEY));
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


public Entity createBlacksmith(String id, Point position,
      List<PImage> images)
   {
      return new Entity(EntityKind.BLACKSMITH, id, position, images,
         0, 0, 0, 0);
   }


   public Entity createMinerFull(String id, int resourceLimit,
      Point position, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new Entity(EntityKind.MINER_FULL, id, position, images,
         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   public Entity createMinerNotFull(String id, int resourceLimit,
      Point position, int actionPeriod, int animationPeriod,
      List<PImage> images)
   {
      return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
         resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public Entity createObstacle(String id, Point position,
      List<PImage> images)
   {
      return new Entity(EntityKind.OBSTACLE, id, position, images,
         0, 0, 0, 0);
   }

   public Entity createOre(String id, Point position, int actionPeriod,
      List<PImage> images)
   {
      return new Entity(EntityKind.ORE, id, position, images, 0, 0,
         actionPeriod, 0);
   }

   public Entity createOreBlob(String id, Point position,
      int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Entity(EntityKind.ORE_BLOB, id, position, images,
            0, 0, actionPeriod, animationPeriod);
   }

   public Entity createQuake(Point position, List<PImage> images)
   {
      return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
         0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public Entity createVein(String id, Point position, int actionPeriod,
      List<PImage> images)
   {
      return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
         actionPeriod, 0);
   }

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

public Optional<Entity> findNearest(Point pos, EntityKind kind)
		   {
		      List<Entity> ofType = new LinkedList<>();
		      for (Entity entity : entities)
		      {
		         if (entity.kind == kind)
		         {
		            ofType.add(entity);
		         }
		      }

		      return Functions.nearestEntity(ofType, pos);
		   }

public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.position))
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

public String getBLOB_ID_SUFFIX() {
	return BLOB_ID_SUFFIX;
}

public int getBLOB_PERIOD_SCALE() {
	return BLOB_PERIOD_SCALE;
}

public int getBLOB_ANIMATION_MIN() {
	return BLOB_ANIMATION_MIN;
}

public int getBLOB_ANIMATION_MAX() {
	return BLOB_ANIMATION_MAX;
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

public int getQUAKE_ACTION_PERIOD() {
	return QUAKE_ACTION_PERIOD;
}

public int getQUAKE_ANIMATION_PERIOD() {
	return QUAKE_ANIMATION_PERIOD;
}

public int getQUAKE_ANIMATION_REPEAT_COUNT() {
	return QUAKE_ANIMATION_REPEAT_COUNT;
}

public String getMINER_KEY() {
	return MINER_KEY;
}

public int getMINER_NUM_PROPERTIES() {
	return MINER_NUM_PROPERTIES;
}

public int getMINER_ID() {
	return MINER_ID;
}

public int getMINER_COL() {
	return MINER_COL;
}

public int getMINER_ROW() {
	return MINER_ROW;
}

public int getMINER_LIMIT() {
	return MINER_LIMIT;
}

public int getMINER_ACTION_PERIOD() {
	return MINER_ACTION_PERIOD;
}

public int getMINER_ANIMATION_PERIOD() {
	return MINER_ANIMATION_PERIOD;
}

public String getOBSTACLE_KEY() {
	return OBSTACLE_KEY;
}

public int getOBSTACLE_NUM_PROPERTIES() {
	return OBSTACLE_NUM_PROPERTIES;
}

public int getOBSTACLE_ID() {
	return OBSTACLE_ID;
}

public int getOBSTACLE_COL() {
	return OBSTACLE_COL;
}

public int getOBSTACLE_ROW() {
	return OBSTACLE_ROW;
}

public String getORE_KEY() {
	return ORE_KEY;
}

public int getORE_NUM_PROPERTIES() {
	return ORE_NUM_PROPERTIES;
}

public int getORE_ID() {
	return ORE_ID;
}

public int getORE_COL() {
	return ORE_COL;
}

public int getORE_ROW() {
	return ORE_ROW;
}

public int getORE_ACTION_PERIOD() {
	return ORE_ACTION_PERIOD;
}

public String getSMITH_KEY() {
	return SMITH_KEY;
}

public int getSMITH_NUM_PROPERTIES() {
	return SMITH_NUM_PROPERTIES;
}

public int getSMITH_ID() {
	return SMITH_ID;
}

public int getSMITH_COL() {
	return SMITH_COL;
}

public int getSMITH_ROW() {
	return SMITH_ROW;
}

public String getVEIN_KEY() {
	return VEIN_KEY;
}

public int getVEIN_NUM_PROPERTIES() {
	return VEIN_NUM_PROPERTIES;
}

public int getVEIN_ID() {
	return VEIN_ID;
}

public int getVEIN_COL() {
	return VEIN_COL;
}

public int getVEIN_ROW() {
	return VEIN_ROW;
}

public int getVEIN_ACTION_PERIOD() {
	return VEIN_ACTION_PERIOD;
}
}
