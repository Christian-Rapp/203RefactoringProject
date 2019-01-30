import java.util.List;
import processing.core.PImage;

final class Background
{
   public String id;
   public List<PImage> images;
   public int imageIndex;
   

   public  static final String BGND_KEY = "background";
   public  static final int BGND_NUM_PROPERTIES = 4;
   public  static final int BGND_ID = 1;
   public  static final int BGND_COL = 2;
   public  static final int BGND_ROW = 3;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }
}
