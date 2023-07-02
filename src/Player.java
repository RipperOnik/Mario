import processing.core.PImage;

public class Player extends AnimatedSprite{
	boolean onPlatform, inPlace;
	PImage[] standLeft;
	PImage[] standRight;
	PImage[] jumpLeft;
	PImage[] jumpRight;
	public Player(Program parent, PImage img, float scale) {
		super(parent, img, scale);
		direction = Program.RIGHT_FACING;
		onPlatform = false;
		inPlace = true;
		standLeft = new PImage[1];
		standLeft[0] = parent.loadImage("player/player_stand_left.png");
		standRight = new PImage[1];
		standRight[0] = parent.loadImage("player/player_stand_right.png");
		jumpLeft = new PImage[1];
		jumpLeft[0] = parent.loadImage("player/player_jump_left.png");
		jumpRight = new PImage[1];
		jumpRight[0] = parent.loadImage("player/player_jump_right.png");
		
		moveLeft = new PImage[3];
		for (int i = 0; i < moveLeft.length; i++) {			
			moveLeft[i] = parent.loadImage("player/player_walk_left"+(i+1)+".png");
		}
		moveRight = new PImage[3];
		for (int i = 0; i < moveRight.length; i++) {			
			moveRight[i] = parent.loadImage("player/player_walk_right"+(i+1)+".png");
		}
		currentImages = standRight;
	}
	
	@Override
	public void updateAnimation() {
		onPlatform = parent.isOnPlatform(this, parent.platforms);
		inPlace = changeX == 0 && changeY == 0;
		super.updateAnimation();
	}
	@Override
	public void selectDirection() {
		if (changeX > 0) {
			direction = Program.RIGHT_FACING;
		}
		else if (changeX < 0) {
			direction = Program.LEFT_FACING;
		}
	}
	@Override
	public void selectCurrentImages() {
		if (direction == Program.RIGHT_FACING) {
			if (inPlace) {
				currentImages = standRight;
			}else if (!onPlatform) {
				currentImages = jumpRight;
			}else {
				currentImages = moveRight;
			}
			
		}else if (direction == Program.LEFT_FACING) {
			if (inPlace) {
				currentImages = standLeft;
			}else if (!onPlatform) {
				currentImages = jumpLeft;
			}else {
				currentImages = moveLeft;
			}
		}
	}
	
	

}
