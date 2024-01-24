// Known bugs:
// - The first image of any set does not fully load!
// - When the game first starts up, there's a lag in display, even though the game still runs
// - Santa's collision with the left and right walls is shaky

// Todo:
// - Give santa and the spiders death animations
// - Better graphics!
// - God mode

// Imports
import java.awt.*;
import java.awt.MediaTracker;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.Random;

// Game methods class
public class SpidersFromSpace
{
	// Constants
	private final DisplayMode POSSIBLE_MODES[] =
	{
		new DisplayMode(1024, 768, 16, 0),
		new DisplayMode(1024, 768, 32, 0),
		new DisplayMode(1024, 768, 24, 0),
		new DisplayMode(640, 480, 16, 0),
		new DisplayMode(640, 480, 32, 0),
		new DisplayMode(640, 480, 24, 0),
		new DisplayMode(800, 600, 16, 0),
		new DisplayMode(800, 600, 32, 0),
		new DisplayMode(800, 600, 24, 0),
	};
	private final int NUM_SPIDER_FILES = 3;
	private final int NUM_SANTA_FILES = 3;
	private final int NUM_BULLET_FILES = 3;
	private final int NUM_VENOM_FILES = 3;
	private final int FONT_SIZE = 24;
	private final int SPIDER_INCREASE_RATE = 5;
	private final float SLOWDOWN_DECREASE_RATE = 0.02f;
	private final float RANGE_INCREASE_RATE = 0.009f;
	private final String START_MESSAGE = "Go Santa!";
	private final String RESET_MESSAGE = "You can do it Santa!";
	private final String END_MESSAGE = "You suck Santa!";
	
	// Difficulty settings
	private int spiderMax = 10;
	private float slowdownValue = 0.05f;
	private float range1 = 0.156271f;
	private float range2 = 0.1599f;
	
	// Game settings
	private boolean cheated = false;
	private int level = 1;
	private int spiders = spiderMax;
	private int santas = 5;
	private long score = 0;
	private long scoreIncrease = 100;
	private long newlifePoint = 500;
	
	// Game data
	private Random randomNum;
	private int imagecounter = 0;
	private boolean running;
	private boolean screenMessageDisplayable;
	private String screenMessage;
	private int frame = 200;
	private String basedir;
	private String imgformat;
	private String backgroundFile;
	private String[] spiderFiles;
	private String[] venomFiles;
	private String[] santaFiles;
	private String[] bulletFiles;
	private Image backgroundImage;
	private Image[] spiderImages;
	private Image[] venomImages;
	private Image[] santaImages;
	private Image[] bulletImages;
	private Sprite[] spiderSprites;
	private Sprite[] venomSprites;
	private Sprite santaSprite;
	private Sprite bulletSprite;
	private GameAction moveLeft;
	private GameAction moveRight;
	private GameAction fireGun;
	private GameAction exitGame;
	private GameAction livesCheat;
	
	// Managers
	private ScreenManager screenmanager;
	private InputManager inputmanager;
	
	// Media tracker
	private MediaTracker tracker;
	
	// Constructor
	public SpidersFromSpace(String imagedir, String imageformat)
	{
		try
		{
			// Initialize screen
			screenmanager = new ScreenManager();
			DisplayMode displayMode = screenmanager.findFirstCompatibleMode(POSSIBLE_MODES);
			screenmanager.setFullScreen(displayMode);
			Window window = screenmanager.getFullScreenWindow();
			window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
			window.setBackground(Color.black);
			window.setForeground(Color.white);
			tracker = new MediaTracker(screenmanager.getFullScreenWindow());
			
			// Initialize input
			inputmanager = new InputManager(window);
			inputmanager.setCursor(InputManager.INVISIBLE_CURSOR);
			moveLeft = new GameAction("moveLeft");
			moveRight = new GameAction("moveRight");
			fireGun = new GameAction("fireGun", GameAction.DETECT_INITAL_PRESS_ONLY);
			exitGame = new GameAction("exitGame", GameAction.DETECT_INITAL_PRESS_ONLY);
			livesCheat = new GameAction("livesCheat", GameAction.DETECT_INITAL_PRESS_ONLY);
			
			// Map keys
			inputmanager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
			inputmanager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
			inputmanager.mapToKey(fireGun, KeyEvent.VK_SPACE);
			inputmanager.mapToKey(exitGame, KeyEvent.VK_ESCAPE);
			inputmanager.mapToKey(livesCheat, KeyEvent.VK_L);
			
			// Set up a random number generator
			randomNum = new Random();
			
			// Set base directory
			basedir = imagedir;
			
			// Set image format
			imgformat = imageformat;
			
			// Set background file
			backgroundFile = basedir + "background" + imgformat;
			
			// Set spider files
			spiderFiles = new String[NUM_SPIDER_FILES];
			for (int x = 0; x < spiderFiles.length; x++)
			{
				// Create string
				spiderFiles[x] = basedir + "spider" + x + imgformat;
			}
			
			// Set santa files
			santaFiles = new String[NUM_SANTA_FILES];
			for (int x = 0; x < santaFiles.length; x++)
			{
				// Create string
				santaFiles[x] = basedir + "santa" + x + imgformat;
			}
			
			// Set bullet files
			bulletFiles = new String[NUM_BULLET_FILES];
			for (int x = 0; x < bulletFiles.length; x++)
			{
				// Create string
				bulletFiles[x] = basedir + "bullet" + x + imgformat;
			}
			
			// Set venom files
			venomFiles = new String[NUM_VENOM_FILES];
			for (int x = 0; x < venomFiles.length; x++)
			{
				// Create string
				venomFiles[x] = basedir + "venom" + x + imgformat;
			}
			
			// Set background image
			backgroundImage = createImage(backgroundFile);
			
			// Set spider images
			spiderImages = new Image[NUM_SPIDER_FILES];
			for (int x = 0; x < spiderImages.length; x++)
			{
				// Create image
				spiderImages[x] = createImage(spiderFiles[x]);
			}
			
			// Set santa images
			santaImages = new Image[NUM_SANTA_FILES];
			for (int x = 0; x < santaImages.length; x++)
			{
				// Create image
				santaImages[x] = createImage(santaFiles[x]);
			}
			
			// Set bullet images
			bulletImages = new Image[NUM_BULLET_FILES];
			for (int x = 0; x < bulletImages.length; x++)
			{
				// Create image
				bulletImages[x] = createImage(bulletFiles[x]);
			}
			
			// Set venom images
			venomImages = new Image[NUM_VENOM_FILES];
			for (int x = 0; x < venomImages.length; x++)
			{
				// Create image
				venomImages[x] = createImage(venomFiles[x]);
			}
		}
		finally
		{
			
		}
	}
	
	// SpiderInit
	public void spiderInit(int numSpiders, int frameDuration)
	{
		// Create spiders
		spiderSprites = new Sprite[numSpiders];
		
		// Create venoms
		venomSprites = new Sprite[numSpiders];
		
		// Iterate through all spiders
		for (int x = 0; x < numSpiders; x++)
		{
			// Create sprite
			spiderSprites[x] = new Sprite(createAnimation(spiderImages, frameDuration), Sprite.SPIDER_SPRITE);
			
			// Get starting location
			float startx = (randomNum.nextFloat() * screenmanager.getWidth());
			float starty = -1;
			
			// Make sure its valid
			if (startx < 0)
			{
				startx = 0;
			}
			if (startx > screenmanager.getWidth() - spiderSprites[x].getWidth())
			{
				startx = screenmanager.getWidth() - spiderSprites[x].getWidth();
			}
			
			// Randomly set this spiders state to be swinging or still
			float swingTest = randomNum.nextFloat();
			if (swingTest < 0.3)
			{
				spiderSprites[x].setState(Sprite.STATE_STILL);
			}
			else if ((swingTest >= 0.3) && (swingTest < 0.7))
			{
				spiderSprites[x].setState(Sprite.STATE_SWINGING_RIGHT);
			}
			else if (swingTest >= 0.7)
			{
				spiderSprites[x].setState(Sprite.STATE_SWINGING_LEFT);
			}
			
			// Set starting location
			spiderSprites[x].setX(startx);
			spiderSprites[x].setY(starty);
			spiderSprites[x].setStartX(startx);
			spiderSprites[x].setStartY(starty);
			
			// Get starting velocity
			float startdx = 0;
			float startdy = randomNum.nextFloat() * slowdownValue;
			
			// Set starting velocity
			spiderSprites[x].setVelocityX(startdx);
			spiderSprites[x].setVelocityY(startdy);
		}
	}
	
	// SantaInit
	public void santaInit(int frameDuration)
	{
		// Create santa
		santaSprite = new Sprite(createAnimation(santaImages, frameDuration), Sprite.SANTA_SPRITE);
		
		// Set starting location
		santaSprite.setX((screenmanager.getWidth() / 2) - santaSprite.getWidth());
		santaSprite.setY(screenmanager.getHeight() - santaSprite.getHeight());
		
		// Set starting velocity
		santaSprite.setVelocityX(0);
		santaSprite.setVelocityY(0);
	}
	
	// BulletInit
	public void bulletInit(int frameDuration)
	{
		// Create bullet
		bulletSprite = new Sprite(createAnimation(bulletImages, frameDuration), Sprite.BULLET_SPRITE);
		
		// Set starting location
		bulletSprite.setX(santaSprite.getX() + (santaSprite.getWidth() / 2));
		bulletSprite.setY(santaSprite.getY() - bulletSprite.getHeight() / 2);
		
		// Set starting velocity
		bulletSprite.setVelocityX(0);
		bulletSprite.setVelocityY(-2f);
	}
	
	// VenomInit
	public void venomInit(int spiderNum, int frameDuration)
	{
		// Create venom
		venomSprites[spiderNum] = new Sprite(createAnimation(venomImages, frameDuration), Sprite.VENOM_SPRITE);
		
		// Set starting location
		venomSprites[spiderNum].setX(spiderSprites[spiderNum].getX() + (spiderSprites[spiderNum].getWidth() / 2));
		venomSprites[spiderNum].setY(spiderSprites[spiderNum].getY() - venomSprites[spiderNum].getHeight() / 2);
		
		// Set starting velocity
		venomSprites[spiderNum].setVelocityX(0);
		venomSprites[spiderNum].setVelocityY(0.5f);
	}
	
	// Update
	public void update(long elapsedTime)
	{
		// Check input
		checkInput();
		
		// Check collisions
		checkCollisions();
		
		// Check spider count
		checkSpiderCount();
		
		// Check santa count
		checkSantaCount();
		
		// Check score
		checkScore();
		
		// Shoot venoms
		shootVenoms();
		
		// Update the spiders (later on add stuff for spiders moving side to side, etc)
		for (int x = 0; x < spiderSprites.length; x++)
		{
			// Check if spider is alive
			if (spiderSprites[x].isDead() == false)
			{
				// Get current x velocity
				float startx = spiderSprites[x].getStartX();
				float currentx = spiderSprites[x].getX();
				
				// Check if spider is swinging to the left
				if (spiderSprites[x].getState() == Sprite.STATE_SWINGING_LEFT)
				{
					if (currentx == startx - spiderSprites[x].getArcAmount())
					{
						spiderSprites[x].setState(Sprite.STATE_SWINGING_RIGHT);
					}
					else
					{
						spiderSprites[x].setX(spiderSprites[x].getX() - 0.5f);
					}
				}
				// Check if spider is swinging to the right
				else if (spiderSprites[x].getState() == Sprite.STATE_SWINGING_RIGHT)
				{
					if (currentx == startx + spiderSprites[x].getArcAmount())
					{
						spiderSprites[x].setState(Sprite.STATE_SWINGING_LEFT);
					}
					else
					{
						spiderSprites[x].setX(spiderSprites[x].getX() + 0.5f);
					}
				}
				
				// Update position
				spiderSprites[x].update(elapsedTime);
			}
		}
		
		// Make sure santa is alive
		if (santaSprite.isDead())
		{
			// You suck santa!
			death();
		}
		
		// Update santa
		santaSprite.update(elapsedTime);
		
		// Update bullets
		if (bulletSprite != null)
		{
			bulletSprite.update(elapsedTime);
		}
		
		// Update venoms
		for (int x = 0; x < venomSprites.length; x++)
		{
			if (venomSprites[x] != null)
			{
				venomSprites[x].update(elapsedTime);
			}
		}
	}
	
	// CheckInput
	public void checkInput()
	{
		// Variables
		float velocityX = 0;
		
		// Check santa input actions
		if (santaSprite.isDead() == false)
		{
			// Check if left button is pressed
			if (moveLeft.isPressed())
			{
				// Decrease santa's x
				velocityX -= santaSprite.getMaxVelocity();
			}
			
			// Check if right button is pressed
			if (moveRight.isPressed())
			{
				// Increase santa's x
				velocityX += santaSprite.getMaxVelocity();
			}
			
			// Check if fire button is pressed
			if (fireGun.isPressed())
			{
				// Make sure bullet doesn't already exist
				if (bulletSprite == null)
				{
					// Fire away!
					bulletInit(frame);
				}
			}
			
			// Set new velocity
			santaSprite.setVelocityX(velocityX);
		}
		
		// Check if escape button is pressed
		if (exitGame.isPressed())
		{
			// Quit game
			// TODO: Make a new method called quit() with a different message
			end();
		}
		
		// Check if lives cheat button is pressed
		if (livesCheat.isPressed())
		{
			santas += 5;
			cheated = true;
		}
	}
	
	// CheckCollisions
	public void checkCollisions()
	{
		// Check each spider sprite
		for (int x = 0; x < spiderSprites.length; x++)
		{
			// Get spider
			Sprite spiderSprite = spiderSprites[x];
			
			// Check if alive
			if (spiderSprite.isDead() == false)
			{
				// Check to see if spider collided with santa
				if (spiderSprite.isCollision(santaSprite))
				{
					// Santa loses
					santaSprite.kill();
					santas--;
				}
				
				// Check to see if spider collided with a bullet
				if (bulletSprite != null)
				{
					if (spiderSprite.isCollision(bulletSprite))
					{
						spiderSprite.kill();
						spiders--;
						bulletSprite = null;
						score += scoreIncrease;
					}
				}
				
				// Check to see if spider went off horizontal edge
				if (spiderSprite.getX() <= 0)
				{
					// Correct spider
					spiderSprite.setState(Sprite.STATE_SWINGING_RIGHT);
				}
				else if (spiderSprite.getX() >= screenmanager.getWidth() - spiderSprite.getWidth())
				{
					// Correct spider
					spiderSprite.setState(Sprite.STATE_SWINGING_LEFT);
				}
				
				// Check to see if spider reached earth
				if ((spiderSprite.getY() + spiderSprite.getHeight()) >= screenmanager.getHeight())
				{
					// Santa loses
					santaSprite.kill();
					santas--;
				}
			}
		}
		
		// Check to see if santa went off the horizontal edge
		if (santaSprite.getX() <= 0)
		{
			// Correct santa
			santaSprite.setX(0);
		}
		
		// Check to see if santa went off the horizontal edge
		else if (santaSprite.getX() >= screenmanager.getWidth() - santaSprite.getWidth())
		{
			// Correct santa
			santaSprite.setX(screenmanager.getWidth() - santaSprite.getWidth());
		}
		
		// Check to see if bullet went off the vertical screen
		if (bulletSprite != null)
		{
			if (bulletSprite.getY() <= 0)
			{
				bulletSprite = null;
			}
		}
		
		// Check each venom
		for (int x = 0; x < venomSprites.length; x++)
		{
			// Check if venom exists
			if (venomSprites[x] != null)
			{
				// Check to see if venom collided with santa
				if (santaSprite.isCollision(venomSprites[x]))
				{
					// Santa loses
					santaSprite.kill();
					santas--;
				}
				
				// Check if venom reached earth
				if ((venomSprites[x].getY() + venomSprites[x].getHeight()) >= screenmanager.getHeight())
				{
					// Null out venom
					venomSprites[x] = null;
				}
			}
		}
	}
	
	// CheckSpiderCount
	public void checkSpiderCount()
	{
		// Check if spiders are at zero
		if (spiders == 0)
		{
			// Increase difficulty settings
			spiderMax += SPIDER_INCREASE_RATE;
			slowdownValue += SLOWDOWN_DECREASE_RATE;
			range2 += RANGE_INCREASE_RATE;
			
			// Reset spiders
			spiders = spiderMax;
			
			// Advance to next level
			level++;
			advance();
		}
	}
	
	// CheckSantaCount
	public void checkSantaCount()
	{
		// Check if all lives are used up
		if (santas <= 0)
		{
			// End game
			end();
		}
	}
	
	// CheckScore
	public void checkScore()
	{		
		// Check if score is at 'new life' point
		if (score == newlifePoint)
		{
			// Give a bonus score increase
			score += scoreIncrease;
			
			// Double to next newlife point
			newlifePoint = newlifePoint * 2;
			
			// Increase santa count
			santas++;
		}
	}
	
	// ShootVenoms
	public void shootVenoms()
	{
		// Randomly find a spider
		int venomSpider = randomNum.nextInt(spiders);
		
		// Make sure spider is alive
		if (spiderSprites[venomSpider].isDead() == false)
		{
			// Check if that venom is not already out
			if (venomSprites[venomSpider] == null)
			{
				// Randomly see if that spider wants to fire
				float test = randomNum.nextFloat();
				if (test > range1 && test < range2)
				//if (test > 0.156271 && test < 0.1599)
				{
					// Venom away!
					venomInit(venomSpider, frame);
				}
			}
		}
	}
	
	// Redraw
	public void redraw(long elapsedTime)
	{
		// Get graphics area
		Graphics2D graphics = screenmanager.getGraphics();
		
		// Draw image background
		graphics.setColor(Color.black);
		graphics.drawImage(backgroundImage, 0, 0, null);
		graphics.setColor(Color.white);
		
		// Draw a game info (test for score, lives, etc.)
		int width = screenmanager.getWidth();
		int spiderLoc = (int)(width * 0.005);
		int livesLoc = (int)(width * 0.15);
		int scoreLoc = (int)(width * 0.30);
		int nlifeLoc = (int)(width * 0.50);
		int levelLoc = (int)(width * 0.80);
		graphics.drawString("Spiders x" + spiders, spiderLoc, 20);
		graphics.drawString("Lives x" + santas, livesLoc, 20);
		graphics.drawString("Score:" + score, scoreLoc, 20);
		graphics.drawString("New life at: " + newlifePoint, nlifeLoc, 20);
		graphics.drawString("Level " + level, levelLoc, 20);
		
		// Draw spiders
		graphics.setColor(Color.white);
		for (int x = 0; x < spiderSprites.length; x++)
		{
			if (spiderSprites[x].isDead() == false)
			{
				int halfx = Math.round(spiderSprites[x].getWidth() / 2);
				int halfy = Math.round(spiderSprites[x].getHeight() / 2);
				int startx = Math.round(spiderSprites[x].getStartX());
				int starty = Math.round(spiderSprites[x].getStartY());
				int spiderx = Math.round(spiderSprites[x].getX());
				int spidery = Math.round(spiderSprites[x].getY());
				graphics.drawLine(startx + halfx, starty + halfy, spiderx + halfx, spidery + halfy);
				graphics.drawImage(spiderSprites[x].getImage(), spiderx, spidery, null);
			}
		}
		
		// Draw santa
		if (santaSprite.isDead() == false)
		{
			int santax = Math.round(santaSprite.getX());
			int santay = Math.round(santaSprite.getY());
			graphics.drawImage(santaSprite.getImage(), santax, santay, null);
		}
		
		// Draw bullets
		if (bulletSprite != null)
		{
			int bulletx = Math.round(bulletSprite.getX());
			int bullety = Math.round(bulletSprite.getY());
			graphics.drawImage(bulletSprite.getImage(), bulletx, bullety, null);
		}
		
		// Draw venoms
		for (int x = 0; x < venomSprites.length; x++)
		{
			if (venomSprites[x] != null)
			{
				int venomx = Math.round(venomSprites[x].getX());
				int venomy = Math.round(venomSprites[x].getY());
				graphics.drawImage(venomSprites[x].getImage(), venomx, venomy, null);
			}
		}
		
		// Dispose of graphics area
		graphics.dispose();
		
		// Update screen
		screenmanager.update();
	}
	
	// Start
	public void start()
	{
		// Create some new spiders
		spiderInit(spiders, frame);
		
		// Create our hero
		santaInit(frame);
		
		// Start game
		running = true;
	}
	
	// Advance
	public void advance()
	{
		// Null out any bullets
		bulletSprite = null;
		
		// Null out all venoms
		for (int x = 0; x < venomSprites.length; x++)
		{
			venomSprites[x] = null;
		}
		
		// Resurrect all spiders
		for (int x = 0; x < spiderSprites.length; x++)
		{
			spiderSprites[x].resurrect();
		}
		
		// Resurrect santa
		santaSprite.resurrect();
		
		// Create some new spiders
		spiderInit(spiders, frame);
		
		// Create our hero
		santaInit(frame);
	}
	
	// Death
	public void death()
	{
		// Null out any bullets
		bulletSprite = null;
		
		// Null out all venoms
		for (int x = 0; x < venomSprites.length; x++)
		{
			venomSprites[x] = null;
		}
		
		// Resurrect all spiders
		for (int x = 0; x < spiderSprites.length; x++)
		{
			spiderSprites[x].resurrect();
		}
		
		// Resurrect santa
		santaSprite.resurrect();
		
		// Create some new spiders
		spiderInit(spiders, frame);
		
		// Create our hero
		santaInit(frame);
	}
	
	// End
	public void end()
	{
		int width = screenmanager.getWidth();
		int height = screenmanager.getHeight();
		int drawxstart = (int)(width * 0.20);
		int drawystart = (int)(height * 0.30);
		while (true)
		{
			// Get graphics access
			Graphics2D graphics = screenmanager.getGraphics();
			
			// Draw image background
			graphics.setColor(Color.black);
			graphics.drawImage(backgroundImage, 0, 0, null);
			graphics.setColor(Color.white);
			
			// Draw message
			graphics.setColor(Color.white);
			graphics.drawString("Thanks for playing!", drawxstart, drawystart);
			graphics.drawString("Your high score is: " + score, drawxstart, drawystart + 30);
			graphics.drawString("You reached level: " + level, drawxstart, drawystart + 60);
			if (level >= 3 && level < 8)
			{
				graphics.drawString("You did pretty good!", drawxstart, drawystart + 90);
			}
			else if (level >= 8 && level < 16)
			{
				graphics.drawString("You did great!!", drawxstart, drawystart + 90);
			}
			else if (level >= 16)
			{
				graphics.drawString("You are a Spider Masher!", drawxstart, drawystart + 90);
			}
			if (cheated)
			{
				graphics.drawString("And you cheated!", drawxstart, drawystart + 120);
			}
			
			// Update
			graphics.dispose();
			screenmanager.update();
			
			// Check if user is ready to start playing
			if (exitGame.isPressed())
			{
				break;
			}
		}
		
		// Stop game
		running = false;
	}
	
	// Exit
	public void exit()
	{
		// Restore screen
		screenmanager.restoreScreen();
		
		// Exit program
		System.exit(0);
	}
	
	// IsRunning
	public boolean isRunning()
	{
		// Return state of game
		return running;
	}
	
	// CreateImage
	public Image createImage(String filename)
	{
		// Create an image
		try
		{
			// Get image from toolkit
			Image image = new ImageIcon(filename).getImage();
			
			// Add to tracker and wait till its loaded
			tracker.addImage(image, imagecounter);
			tracker.waitForID(imagecounter);
			
			int test = tracker.statusID(imagecounter, true);
			switch(test)
			{
				case MediaTracker.ERRORED:
					//System.out.println(filename + " had an error loading");
					break;
				case MediaTracker.COMPLETE:
					//System.out.println(filename + " loaded completely");
					break;
				case MediaTracker.ABORTED:
					//System.out.println(filename + " aborted loading");
					break;
				case MediaTracker.LOADING:
					//System.out.println(filename + " is still loading");
					break;
			}
			
			// Increase counter
			imagecounter++;
			
			// Return image
			return image;
		}
		catch (InterruptedException ie)
		{
			
		}
		return null;
	}
	
	// CreateAnimation
	public Animation createAnimation(Image[] imageList, int frameDuration)
	{
		// Create new animation
		Animation anim = new Animation();
		
		// The first image in the imageList just never gets loaded!! ARRRGGGGGGHH
		// Add frames to animation
		//for (int x = 0; x < imageList.length; x++)
		//{
		//	anim.addFrame(imageList[x], frameDuration);
		//}
		anim.addFrame(imageList[1], frameDuration);
		
		// Return animation
		return anim;
	}
}