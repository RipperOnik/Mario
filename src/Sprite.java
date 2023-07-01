import processing.core.PApplet;
import processing.core.PImage;

public class Sprite {
	PImage image;
	float centerX, centerY;
	float changeX, changeY;
	float w, h;
	PApplet parent;
	
	public Sprite(PApplet parent, String filename, float scale, float x, float y) {
		this.parent = parent;
		image = parent.loadImage(filename);
		w = image.width * scale;
		h = image.height * scale;
		centerX = x;
		centerY = y;
		changeX = 0;
		changeY = 0;
	}
	public Sprite(PApplet parent, String filename, float scale) {
		this(parent, filename, scale, 0, 0);
	}
	public Sprite(PApplet parent, PImage img, float scale) {
		this.parent = parent;
		image = img;
		w = image.width * scale;
		h = image.height * scale;
		centerX = 0;
		centerY = 0;
		changeX = 0;
		changeY = 0;
	}
	public void display() {
		parent.image(image, centerX, centerY, w, h);
	}
	public void update() {
		centerX += changeX;
		centerY += changeY;
	}
	
	
	public float getLeft() {
		return centerX - w/2;
	}
	public void setLeft(float newLeft) {
		centerX = newLeft + w/2;
	}
	public float getRight() {
		return centerX + w/2;
	}
	public void setRight(float newRight) {
		centerX = newRight - w/2;
	}
	public float getTop() {
		return centerY - h/2;
	}
	public void setTop(float newTop) {
		centerY = newTop + h/2;
	}
	public float getBottom() {
		return centerY + h/2;
	}
	public void setBottom(float newBottom) {
		centerY = newBottom - h/2; 
	}
	
	
	

}
