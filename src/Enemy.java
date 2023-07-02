import processing.core.PApplet;
import processing.core.PImage;

public class Enemy extends AnimatedSprite{
	float boundaryLeft, boundaryRight;
	public Enemy(PApplet parent, PImage img, float scale, float bLeft, float bRight) {
		super(parent, img, scale);
		boundaryLeft = bLeft;
		boundaryRight = bRight;
		moveLeft = new PImage[3];
		for (int i = 0; i < moveLeft.length; i++) {
			moveLeft[i] = parent.loadImage("spider_walk_left"+(i+1)+".png");
		}
		moveRight = new PImage[3];
		for (int i = 0; i < moveRight.length; i++) {
			moveRight[i] = parent.loadImage("spider_walk_right"+(i+1)+".png");
		}
		currentImages = moveRight;
		direction = Program.RIGHT_FACING;
		changeX = 2;
	}
	public void update() {
		super.update();
		if (getLeft() <= boundaryLeft) {
			setLeft(boundaryLeft);
			changeX *= -1;
		}else if (getRight() >= boundaryRight) {
			setRight(boundaryRight);
			changeX *= -1;
		}
	}
}
