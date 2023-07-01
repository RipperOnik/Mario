import java.util.ArrayList;

import processing.core.*;
public class Program extends PApplet{
	final static float MOVE_SPEED = 5f;
	final static float SPRITE_SCALE = 50f/128f;
	final static float SPRITE_SIZE = 50f;
	final static float GRAVITY = 0.6f;
	final static float JUMP_SPEED= -16f;
	Sprite player;
	PImage snow, crate, redBrick, brownBrick, playerImage;
	ArrayList<Sprite> platforms;

	public static void main(String[] args) {
		PApplet.main("Program");
	}
	@Override
	public void settings() {
		size(800, 600);
	}
	@Override
	public void setup() {
		imageMode(CENTER);
		playerImage = loadImage("player.png");
		player = new Sprite(this, playerImage, 0.7f);
		player.centerX = 100;
		player.centerY = 300;
		player.changeX = 5;
		player.changeY = -10;
		platforms = new ArrayList<Sprite>();
		redBrick = loadImage("red_brick.png");
		brownBrick = loadImage("brown_brick.png");
		crate = loadImage("crate.png");
		snow = loadImage("snow.png");
		createPlatforms("map.csv");
	}
	@Override
	public void draw() {
		background(255);
		player.display();
		resolvePlatformCollisions(player, platforms);
		for(Sprite sprite: platforms) {
			sprite.display();
		}
	}
	@Override
	public void keyPressed() {
		if (keyCode == RIGHT) {
			player.changeX = MOVE_SPEED;
		}
		else if (keyCode == LEFT) {
			player.changeX = -MOVE_SPEED;
		}
		// jump on space
		else if (keyCode == 32 && isOnPlatform(player, platforms)) {
			player.changeY = JUMP_SPEED;
		}
		
	}
	@Override
	public void keyReleased() {
		if (keyCode == RIGHT) {
			player.changeX = 0f;
		}
		if (keyCode == LEFT) {
			player.changeX = 0f;
		}
		if (keyCode == DOWN) {
			player.changeY = 0f;
		}
		if (keyCode == UP) {
			player.changeY = 0f;
		}
	}
	void createPlatforms(String filename){
		  String[] lines = loadStrings(filename);
		  for(int row = 0; row < lines.length; row++){
		    String[] values = split(lines[row], ",");
		    for(int col = 0; col < values.length; col++){
		      if(values[col].equals("1")){
		        Sprite s = new Sprite(this, redBrick, SPRITE_SCALE);
		        s.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
		        s.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
		        platforms.add(s);
		      }
		      else if(values[col].equals("2")){
		        Sprite s = new Sprite(this, snow, SPRITE_SCALE);
		        s.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
		        s.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
		        platforms.add(s);
		      }
		      else if(values[col].equals("3")){
		        Sprite s = new Sprite(this, brownBrick, SPRITE_SCALE);
		        s.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
		        s.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
		        platforms.add(s);
		      }
		      else if(values[col].equals("4")){
		        Sprite s = new Sprite(this, crate, SPRITE_SCALE);
		        s.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
		        s.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
		        platforms.add(s);
		      }
		    }
		  }  
	}
	boolean checkCollision(Sprite s1, Sprite s2) {
		boolean noXOverlap = s1.getRight() <= s2.getLeft() || s1.getLeft() >= s2.getRight();
		boolean noYOverlap = s1.getBottom() <= s2.getTop() || s1.getTop() >= s2.getBottom();
		if (noXOverlap || noYOverlap) {
			return false;
		}
		// we have a collision when all 4 conditions are not met
		else {
			return true;
		}
	}
	ArrayList<Sprite> checkCollisionList(Sprite s, ArrayList<Sprite> list){
		ArrayList<Sprite> collisionList = new ArrayList<Sprite>();
		for(Sprite p: list) {
			if (checkCollision(s, p)) {
				collisionList.add(p);
			}
		}
		return collisionList;
	}
	void resolvePlatformCollisions(Sprite s, ArrayList<Sprite> walls) {
		// add gravity to changeY
		s.changeY += GRAVITY;
		
		// move vertically
		s.centerY += s.changeY;
		ArrayList<Sprite> collisionList = checkCollisionList(s, walls);
		if (collisionList.size() > 0) {
			Sprite collidedSprite = collisionList.get(0);
			// if falling down
			if (s.changeY > 0) {
				s.setBottom(collidedSprite.getTop());
			}
			// if jumping
			else if(s.changeY < 0) {
				s.setTop(collidedSprite.getBottom());
			}
			// stop gravitation 
			s.changeY = 0;
		}
		
		// move horizontally
		s.centerX += s.changeX;
		collisionList = checkCollisionList(s, walls);
		if (collisionList.size() > 0) {
			Sprite collidedSprite = collisionList.get(0);
			// if going right
			if (s.changeX > 0) {
				s.setRight(collidedSprite.getLeft());
			}
			// if going left
			else if(s.changeX < 0) {
				s.setLeft(collidedSprite.getRight());
			}
		}
		
	}
	boolean isOnPlatform(Sprite s, ArrayList<Sprite> walls) {
		s.centerY += 5;
		ArrayList<Sprite> collisionList = checkCollisionList(s, walls);
		s.centerY -= 5;
		return !collisionList.isEmpty();
	}

}