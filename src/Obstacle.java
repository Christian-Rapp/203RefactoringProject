import java.util.List;

import processing.core.PImage;

public class Obstacle extends EntityInterface{

	public Obstacle(String id, Point position, List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages( images);
		setImageIndex( 0);
		
	}
	
}
