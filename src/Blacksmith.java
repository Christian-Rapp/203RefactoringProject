import java.util.List;

import processing.core.PImage;

public class Blacksmith extends Entity{
	
	private  final String SMITH_KEY = "blacksmith";
	   private  final int SMITH_NUM_PROPERTIES = 4;
	   private  final int SMITH_ID = 1;
	   private  final int SMITH_COL = 2;
	   private  final int SMITH_ROW = 3;

	public Blacksmith(String id, Point position, List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		
	}
	
	public Blacksmith()
	{
		
	}
	
	public Blacksmith(String[] properties, ImageStore imageStore)
	{
		setPosition( new Point(Integer.parseInt(properties[SMITH_COL]), Integer.parseInt(properties[SMITH_ROW])));
     	setId(properties[SMITH_ID]);
        setImages(imageStore.getImageList(SMITH_KEY));
        setImageIndex( 0);
	}
	
	
	
	
}
