import java.util.List;
import processing.core.PImage;

final class Background extends Drawable
{
   
   public List<PImage> images;
   public int imageIndex;
   

   public  static final String BGND_KEY = "background";
   public  static final int BGND_NUM_PROPERTIES = 4;
   public  static final int BGND_ID = 1;
   public  static final int BGND_COL = 2;
   public  static final int BGND_ROW = 3;

   public Background(String id, List<PImage> images)
   {
      setId( id);
      setImages(images);
   }
   
   public Background(String[] properties,ImageStore imageStore)
   {
	   Point pt = new Point(Integer.parseInt(properties[Background.BGND_COL]),
	            Integer.parseInt(properties[Background.BGND_ROW]));
	  String id = properties[Background.BGND_ID];
      setId( id);
      setImages(images);
      setImageIndex(0);
   }
  
   
}
