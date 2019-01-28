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

public void addEntity(Functions funtions, Entity entity)
{
if (withinBounds(null, entity.position))
   {
      setOccupancyCell(null, entity.position, entity);
      entities.add(entity);
   }
}

public void moveEntity(Functions functions, Entity entity, Point pos)
{
   Point oldPos = entity.position;
   if (withinBounds(functions, pos) && !pos.equals(oldPos))
   {
      setOccupancyCell(functions, oldPos, null);
      removeEntityAt(functions, pos);
      setOccupancyCell(functions, pos, entity);
      entity.position = pos;
   }
}

public void removeEntity(Functions functions, Entity entity)
{
   removeEntityAt(functions, entity.position);
}

public void removeEntityAt(Functions functions, Point pos)
{
   if (withinBounds(functions, pos)
      && getOccupancyCell(functions, pos) != null)
   {
      Entity entity = getOccupancyCell(functions, pos);

      /* this moves the entity just outside of the grid for
         debugging purposes */
      entity.position = new Point(-1, -1);
      entities.remove(entity);
      setOccupancyCell(functions, pos, null);
   }
}

public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(null, pos))
      {
         return Optional.of(getOccupancyCell(null, pos));
      }
      else
      {
         return Optional.empty();
      }
   }

public Entity getOccupancyCell(Functions functions, Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

public void setOccupancyCell(Functions functions, Point pos, Entity entity)
		   {
		      occupancy[pos.y][pos.x] = entity;
		   }

public boolean parseBackground(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.BGND_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
		            Integer.parseInt(properties[Functions.BGND_ROW]));
		         String id = properties[Functions.BGND_ID];
		         setBackground(functions, pt,
		            new Background(id, imageStore.getImageList(functions, id)));
		      }

		      return properties.length == Functions.BGND_NUM_PROPERTIES;
		   }

public boolean parseMiner(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.MINER_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Functions.MINER_COL]),
		            Integer.parseInt(properties[Functions.MINER_ROW]));
		         Entity entity = Functions.createMinerNotFull(properties[Functions.MINER_ID],
		            Integer.parseInt(properties[Functions.MINER_LIMIT]),
		            pt,
		            Integer.parseInt(properties[Functions.MINER_ACTION_PERIOD]),
		            Integer.parseInt(properties[Functions.MINER_ANIMATION_PERIOD]),
		            imageStore.getImageList(functions, Functions.MINER_KEY));
		         tryAddEntity(functions, entity);
		      }

		      return properties.length == Functions.MINER_NUM_PROPERTIES;
		   }

public boolean parseObstacle(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES)
		      {
		         Point pt = new Point(
		            Integer.parseInt(properties[Functions.OBSTACLE_COL]),
		            Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
		         Entity entity = Functions.createObstacle(properties[Functions.OBSTACLE_ID],
		            pt, imageStore.getImageList(functions, Functions.OBSTACLE_KEY));
		         tryAddEntity(functions, entity);
		      }

		      return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
		   }

public boolean parseOre(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.ORE_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Functions.ORE_COL]),
		            Integer.parseInt(properties[Functions.ORE_ROW]));
		         Entity entity = Functions.createOre(properties[Functions.ORE_ID],
		            pt, Integer.parseInt(properties[Functions.ORE_ACTION_PERIOD]),
		            imageStore.getImageList(functions, Functions.ORE_KEY));
		         tryAddEntity(functions, entity);
		      }

		      return properties.length == Functions.ORE_NUM_PROPERTIES;
		   }

public boolean parseSmith(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.SMITH_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Functions.SMITH_COL]),
		            Integer.parseInt(properties[Functions.SMITH_ROW]));
		         Entity entity = Functions.createBlacksmith(properties[Functions.SMITH_ID],
		            pt, imageStore.getImageList(functions, Functions.SMITH_KEY));
		         tryAddEntity(functions, entity);
		      }

		      return properties.length == Functions.SMITH_NUM_PROPERTIES;
		   }

public boolean parseVein(String[] properties, Functions functions, ImageStore imageStore)
		   {
		      if (properties.length == Functions.VEIN_NUM_PROPERTIES)
		      {
		         Point pt = new Point(Integer.parseInt(properties[Functions.VEIN_COL]),
		            Integer.parseInt(properties[Functions.VEIN_ROW]));
		         Entity entity = Functions.createVein(properties[Functions.VEIN_ID],
		            pt,
		            Integer.parseInt(properties[Functions.VEIN_ACTION_PERIOD]),
		            imageStore.getImageList(functions, Functions.VEIN_KEY));
		         tryAddEntity(functions, entity);
		      }

		      return properties.length == Functions.VEIN_NUM_PROPERTIES;
		   }

public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), null, imageStore))
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

public boolean processLine(String line, Functions functions, ImageStore imageStore)
		   {
		      String[] properties = line.split("\\s");
		      if (properties.length > 0)
		      {
		         switch (properties[Functions.PROPERTY_KEY])
		         {
		         case Functions.BGND_KEY:
		            return parseBackground(properties, functions, imageStore);
		         case Functions.MINER_KEY:
		            return parseMiner(properties, functions, imageStore);
		         case Functions.OBSTACLE_KEY:
		            return parseObstacle(properties, functions, imageStore);
		         case Functions.ORE_KEY:
		            return parseOre(properties, functions, imageStore);
		         case Functions.SMITH_KEY:
		            return parseSmith(properties, functions, imageStore);
		         case Functions.VEIN_KEY:
		            return parseVein(properties, functions, imageStore);
		         }
		      }

		      return false;
		   }

public boolean withinBounds(Functions functions, Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
         pos.x >= 0 && pos.x < numCols;
   }

public boolean isOccupied(Functions functions, Point pos)
   {
      return withinBounds(functions, pos) &&
         getOccupancyCell(functions, pos) != null;
   }

public Optional<Entity> findNearest(Functions functions, Point pos, EntityKind kind)
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

public void tryAddEntity(Functions functions, Entity entity)
   {
      if (isOccupied(functions, entity.position))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(functions, entity);
   }

public Optional<Point> findOpenAround(Functions functions, Point pos)
   {
      for (int dy = -Functions.ORE_REACH; dy <= Functions.ORE_REACH; dy++)
      {
         for (int dx = -Functions.ORE_REACH; dx <= Functions.ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(functions, newPt) &&
               !isOccupied(functions, newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

public Optional<PImage> getBackgroundImage(Functions functions, Point pos)
		   {
		      if (withinBounds(functions, pos))
		      {
		         return Optional.of(Functions.getCurrentImage(getBackgroundCell(functions, pos)));
		      }
		      else
		      {
		         return Optional.empty();
		      }
		   }

public void setBackground(Functions functions, Point pos, Background background)
		   {
		      if (withinBounds(functions, pos))
		      {
		         setBackgroundCell(functions, pos, background);
		      }
		   }

public Background getBackgroundCell(Functions functions, Point pos)
   {
      return background[pos.y][pos.x];
   }

public void setBackgroundCell(Functions functions, Point pos, Background background)
		   {
		      this.background[pos.y][pos.x] = background;
		   }
}
