import processing.core.PImage;

public class Coin extends AnimatedSprite{
	public Coin(Program parent, PImage img, float scale) {
		super(parent, img, scale);
		standNeutral = new PImage[4];
		for (int i = 0; i < standNeutral.length; i++) {
			standNeutral[i] = parent.loadImage("coin/gold" + (i+1) + ".png");
		}
		currentImages = standNeutral;
	}

}
