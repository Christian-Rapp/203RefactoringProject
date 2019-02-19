import java.util.List;

import processing.core.PImage;

public abstract class Drawable {

	

	private String id;
	private List<PImage> images;
	private int imageIndex;
	
	public PImage getCurrentImage() {
		return images.get(imageIndex);
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected void setImages(List<PImage> images) {
		this.images = images;
	}

	protected String getId() {
		return id;
	}

	protected List<PImage> getImages() {
		return images;
	}

	protected int getImageIndex() {
		return imageIndex;
	}
	
	protected void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}
	
}
