import java.util.ArrayList;

import processing.core.*;
public class Program extends PApplet{
	final static float MOVE_SPEED = 5f;
	final static float SPRITE_SCALE = 50f/128f;
	final static float SPRITE_SIZE = 50f;
	final static float GRAVITY = 0.6f;
	final static float JUMP_SPEED = -16f;
	
	final static float RIGHT_MARGIN = 400;
	final static float LEFT_MARGIN = 60;
	final static float VERTICAL_MARGIN = 40;
	
	public final static int NEUTRAL_FACING = 0;
	public final static int RIGHT_FACING = 1;
	public final static int LEFT_FACING = 2;
	
	final static float WIDTH = SPRITE_SIZE * 16;
	final static float HEIGHT = SPRITE_SIZE * 12;
	final static float GROUND_LEVEL = HEIGHT - SPRITE_SIZE;
	
	ArrayList<Sprite> coins;
	ArrayList<Sprite> enemies;
	int score;
	Player player;
	PImage snow, crate, redBrick, brownBrick, playerImage, gold, enemyImage;
	ArrayList<Sprite> platforms;
	float viewX, viewY;
	boolean won, lost;
	

	public static void main(String[] args) {
		PApplet.main("Program");
	}
	@Override
	public void settings() {
		size(800, 600);
	}
	@Override
	public void setup() {
		viewX = 0;
		viewY = 0;
		score = 0;
		won = false;
		lost = false;
		imageMode(CENTER);
		playerImage = loadImage("player/player_stand_right.png");
		player = new Player(this, playerImage, 100f/128f);
		player.centerX = 100;
		player.setBottom(GROUND_LEVEL);
		platforms = new ArrayList<Sprite>();
		enemies = new ArrayList<Sprite>();
		coins = new ArrayList<Sprite>();
		enemyImage = loadImage("enemy/spider_walk_right1.png");
		redBrick = loadImage("red_brick.png");
		brownBrick = loadImage("brown_brick.png");
		crate = loadImage("crate.png");
		snow = loadImage("snow.png");
		gold = loadImage("coin/gold1.png");
		createPlatforms("map.csv");
	}
	@Override
	public void draw() {
		if (won || lost) {
			displayGameOver();
		}else {
			background(255);
			scroll();
			player.display();
			player.updateAnimation();
			resolvePlatformCollisions(player, platforms);
			resolveCoinCollection(player, coins);
			checkDeath();
			for(Sprite sprite: platforms) {
				sprite.display();
			}
			for(Sprite coin: coins) {
				coin.display();
				((AnimatedSprite)coin).updateAnimation();
			}
			displayScore();
			for(Sprite enemy: enemies) {
				((Enemy)enemy).display();
				((Enemy)enemy).update();
				((Enemy)enemy).updateAnimation();
			}
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
		      else if(values[col].equals("5")){
			    Coin coin = new Coin(this, gold, SPRITE_SCALE);
			    coin.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
			    coin.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
			    coins.add(coin);
			  }
		      else if(values[col].equals("6")){
		    	  Float bLeft = col * SPRITE_SIZE;
		    	  Float bRight = bLeft + 4*SPRITE_SIZE; 
				  Enemy enemy = new Enemy(this, enemyImage, 60f/72f, bLeft, bRight);
				  enemy.centerX = SPRITE_SIZE/2 + col * SPRITE_SIZE;
				  enemy.centerY = SPRITE_SIZE/2 + row * SPRITE_SIZE;
				  enemies.add(enemy);
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
	void resolveCoinCollection(Sprite s, ArrayList<Sprite> collectedCoins) {
		s.changeY += GRAVITY;
		s.centerY += s.changeY;
		s.centerX += s.changeX;
		ArrayList<Sprite> collisionList = checkCollisionList(s, collectedCoins);
		
		// delete collected coins from array
		for (int i = 0; i < collisionList.size(); i++) {
			score++;
			coins.remove(collisionList.get(i));
		}
		if (coins.isEmpty()) {
			won = true;
		}
		s.changeY -= GRAVITY;
		s.centerY -= s.changeY;
		s.centerX -= s.changeX;
	}
	void checkDeath() {
		player.changeY += GRAVITY;
		player.centerY += player.changeY;
		player.centerX += player.changeX;
		ArrayList<Sprite> collisionList = checkCollisionList(player, enemies);
		
		if (collisionList.size() > 0) {
			lost = true;
		}
		player.changeY -= GRAVITY;
		player.centerY -= player.changeY;
		player.centerX -= player.changeX;
		
		if (player.getTop() > GROUND_LEVEL) {
			lost = true;
		}
	}
	public boolean isOnPlatform(Sprite s, ArrayList<Sprite> walls) {
		s.centerY += 5;
		ArrayList<Sprite> collisionList = checkCollisionList(s, walls);
		s.centerY -= 5;
		return !collisionList.isEmpty();
	}
	void scroll() {
		float rightBoundary = viewX + width - RIGHT_MARGIN;
		if (player.getRight() > rightBoundary) {
			viewX += player.getRight() - rightBoundary;
		}
		float leftBoundary = viewX + LEFT_MARGIN;
		if (player.getLeft() < leftBoundary) {
			viewX -= leftBoundary - player.getLeft();
		}
		float bottomBoundary = viewY + height - VERTICAL_MARGIN;
		if (player.getBottom() > bottomBoundary) {
			viewY += player.getBottom() - bottomBoundary;
		}
		float topBoundary = viewY + VERTICAL_MARGIN;
		if (player.getTop() < topBoundary) {
			viewY -= topBoundary - player.getTop();
		}
		translate(-viewX, -viewY);
	}
	void displayScore() {
		textSize(32);
		fill(255,0,0);
		text("Coins: " + score, viewX + 50, viewY + 50);
	}
	void displayGameOver() {
		fill(0,0,255);
		scroll();
		float textX = viewX + width/2 - 100;
		float textY = viewY + height/2;
		text("GAME OVER", textX, textY);
		if (lost) {
			text("YOU LOSE", textX, textY + 50);
		}else if(won) {
			text("YOU WIN", textX, textY + 50);
		}
		text("PRESS SPACE TO RESTART", textX, textY + 100);
		if (keyCode == 32) {
			setup();
		}
		
	}
}
