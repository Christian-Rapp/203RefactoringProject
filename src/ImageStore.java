import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

final class ImageStore
{
   public Map<String, List<PImage>> images;
   public List<PImage> defaultImages;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

public List<PImage> getImageList(Functions functions, String key)
   {
      return images.getOrDefault(key, defaultImages);
   }

public void loadImages(Scanner in, Functions functions, PApplet screen)
		   {
		      int lineNumber = 0;
		      while (in.hasNextLine())
		      {
		         try
		         {
		            Functions.processImageLine(images, in.nextLine(), screen);
		         }
		         catch (NumberFormatException e)
		         {
		            System.out.println(String.format("Image format error on line %d",
		               lineNumber));
		         }
		         lineNumber++;
		      }
		   }
}
