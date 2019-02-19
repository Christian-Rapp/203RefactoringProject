import java.util.List;

import processing.core.PImage;

public abstract class EntityInterface extends Drawable{

	// protected String id;
	private Point position;
	
//	protected List<PImage> images;
//	protected int imageIndex;
	
	public void nextImage() {
		setImageIndex((getImageIndex() + 1) % getImages().size());
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public void setPosition(Point point)
	{
		this.position = point;
	}
	
	
	
}
