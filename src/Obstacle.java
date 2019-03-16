import java.util.List;

import processing.core.PImage;

public class Obstacle extends Entity{
	
	   private  final String OBSTACLE_KEY = "obstacle";
	   private  final int OBSTACLE_NUM_PROPERTIES = 4;
	   private  final int OBSTACLE_ID = 1;
	   private  final int OBSTACLE_COL = 2;
	   private  final int OBSTACLE_ROW = 3;

	public Obstacle(String id, Point position, List<PImage> images) {
		
		setId(id);
		setPosition(position);
		setImages(images);
		setImageIndex(0);
		
	}
	
	public Obstacle(String[] properties, ImageStore imageStore)
	{
		Point pt = new Point(
	            Integer.parseInt(properties[OBSTACLE_COL]),
	            Integer.parseInt(properties[OBSTACLE_ROW]));
	         setId(properties[OBSTACLE_ID]);
	         setPosition(pt);
	         setImages(imageStore.getImageList(OBSTACLE_KEY));
	         setImageIndex(0);
	}
	
	public <R> R accept(EntityVisitor<R> visitor)
	{
		return visitor.visit(this);
	}
	
	
}
