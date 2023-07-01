import processing.core.PApplet;
import processing.core.PImage;

public class Coin extends AnimatedSprite{
	public Coin(PApplet parent, PImage img, float scale) {
		super(parent, img, scale);
		standNeutral = new PImage[4];
		for (int i = 0; i < standNeutral.length; i++) {
			standNeutral[i] = parent.loadImage("gold" + (i+1) + ".png");
		}
		currentImages = standNeutral;
	}

}
