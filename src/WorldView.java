import java.util.Optional;

import processing.core.PApplet;
import processing.core.PImage;

final class WorldView
{
   public PApplet screen;
   public WorldModel world;
   public int tileWidth;
   public int tileHeight;
   public Viewport viewport;
   
   public final int COLOR_MASK = 0xffffff;
   public final int KEYED_IMAGE_MIN = 5;
   private final int KEYED_RED_IDX = 2;
   private final int KEYED_GREEN_IDX = 3;
   private final int KEYED_BLUE_IDX = 4;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

public void drawViewport(Functions functions)
   {
      drawBackground(functions);
      drawEntities(functions);
   }

public void drawBackground(Functions functions)
   {
      for (int row = 0; row < viewport.numRows; row++)
      {
         for (int col = 0; col < viewport.numCols; col++)
         {
            Point worldPoint = viewport.viewportToWorld(functions, col, row);
            Optional<PImage> image = world.getBackgroundImage(functions,
               worldPoint);
            if (image.isPresent())
            {
               screen.image(image.get(), col * tileWidth,
                  row * tileHeight);
            }
         }
      }
   }

	public void drawEntities(Functions functions)
	   {
	      for (Entity entity : world.entities)
	      {
	         Point pos = entity.position;
	
	         if (viewport.contains(functions, pos))
	         {
	            Point viewPoint = viewport.worldToViewport(functions, pos.x, pos.y);
	            screen.image(Functions.getCurrentImage(entity),
	               viewPoint.x * tileWidth, viewPoint.y * tileHeight);
	         }
	      }
	   }

	public void shiftView(Functions functions, int colDelta, int rowDelta)
	   {
	      int newCol = clamp(viewport.col + colDelta, 0,
	         world.numCols - viewport.numCols);
	      int newRow = clamp(viewport.row + rowDelta, 0,
	         world.numRows - viewport.numRows);
	
	      viewport.shift(functions, newCol, newRow);
	   }

	public int clamp(int value, int low, int high)
	{
		return Math.min(high, Math.max(value, low));
	}

}
