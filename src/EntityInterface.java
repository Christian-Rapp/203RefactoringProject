import processing.core.PImage;

public interface EntityInterface extends Drawable{

	public abstract void nextImage();
	
	public abstract Point getPosition();
	
	public abstract void setPosition(Point point);
	
	
	
	
}
