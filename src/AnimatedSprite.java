import processing.core.PImage;

public class AnimatedSprite extends Sprite{
	PImage[] currentImages;
	PImage[] standNeutral;
	PImage[] moveLeft;
	PImage[] moveRight;
	int direction;
	int index;
	int frame;
	public AnimatedSprite(Program parent, PImage img, float scale) {
		super(parent, img, scale);
		direction = Program.NEUTRAL_FACING;
		index = 0;
		frame = 0;
	}
	public void updateAnimation() {
		frame++;
		// each 5th frame, select a new image to display depending on direction
		if (frame % 5 == 0) {
			selectDirection();
			selectCurrentImages();
			advanceToNextImage();
		}
		
	}
	public void selectDirection() {
		if (changeX > 0) {
			direction = Program.RIGHT_FACING;
		}
		else if (changeX < 0) {
			direction = Program.LEFT_FACING;
		}else {
			direction = Program.NEUTRAL_FACING;
		}
		
	}
	public void selectCurrentImages() {
		if (direction == Program.RIGHT_FACING) {
			currentImages = moveRight;			
		}
		else if (direction == Program.LEFT_FACING) {
			currentImages = moveLeft;			
		}else {
			currentImages = standNeutral;
		}
		
	}
	// cycle through images
	public void advanceToNextImage(){
		if (index >= currentImages.length) {
			index = 0;
		}
		image = currentImages[index];
		index++;
	}

}
