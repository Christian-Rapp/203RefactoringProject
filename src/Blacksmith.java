import java.util.List;

import processing.core.PImage;

public class Blacksmith extends EntityInterface{
	

	public Blacksmith(String id, Point position, List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		
	}
	
	public Blacksmith()
	{
		
	}
	
	
	
	
}
