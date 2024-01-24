// Imports
import java.awt.Image;
import java.util.Random;

// Sprite class
public class Sprite
{
	// Private data
	protected Animation anim;
	private float x;
	private float y;
	private float startx;
	private float starty;
	private float dx;
	private float dy;
	private boolean deathstatus;
	private int type;
	private int state;
	private float arcamount;
	
	// Constants
	public static final float MAX_VELOCITY = 0.5f;
	public static final int SPIDER_SPRITE = 0;
	public static final int SANTA_SPRITE = 1;
	public static final int BULLET_SPRITE = 2;
	public static final int VENOM_SPRITE = 3;
	public static final int STATE_STILL = 0;
	public static final int STATE_SWINGING_RIGHT = 1;
	public static final int STATE_SWINGING_LEFT = 2;
	
	// Creates a new Sprite object with the specified Animation.
	public Sprite(Animation anim, int type)
	{
		// Get a random arc amount
		Random rand = new Random();
		this.arcamount = rand.nextFloat() * 2f;
		
		// Set animation
		this.anim = anim;
		
		// Set type
		this.type = type;
		
		// Set to alive
		this.deathstatus = false;
	}
	
	// Gets this Sprite's arc amount
	public float getArcAmount()
	{
		return 100f;
		//return arcamount;
	}
	// Updates this Sprite's Animation and its position based on the velocity.
	public void update(long elapsedTime)
	{
		x += dx * elapsedTime;
		y += dy * elapsedTime;
		anim.update(elapsedTime);
	}
	
	// Gets this Sprite's current x position.
	public float getX()
	{
		return x;
	}
	
	// Gets this Sprite's current y position.
	public float getY()
	{
		return y;
	}
	
	// Sets this Sprite's current x position.
	public void setX(float x)
	{
		this.x = x;
	}
	
	// Sets this Sprite's current y position.
	public void setY(float y)
	{
		this.y = y;
	}
	
	// Gets this Sprite's start x position
	public float getStartX()
	{
		return startx;
	}
	
	// Gets this Sprite's start y position
	public float getStartY()
	{
		return starty;
	}
	
	// Sets this Sprite's start x position
	public void setStartX(float x)
	{
		this.startx = x;
	}
	
	// Sets this Sprite's start y position
	public void setStartY(float y)
	{
		this.starty = y;
	}
	
	// Gets this Sprite's state
	public int getState()
	{
		return state;
	}
	
	// Sets this Sprite's state
	public void setState(int newstate)
	{
		this.state = newstate;
	}
	
	// Gets this Sprite's width, based on the size of the current image.
	public int getWidth()
	{
		return anim.getImage().getWidth(null);
	}
	
	// Gets this Sprite's height, based on the size of the current image.
	public int getHeight()
	{
		return anim.getImage().getHeight(null);
	}
	
	// Gets the horizontal velocity of this Sprite in pixels per millisecond.
	public float getVelocityX()
	{
		return dx;
	}
	
	// Gets the vertical velocity of this Sprite in pixels per millisecond.
	public float getVelocityY()
	{
		return dy;
	}
	
	// Sets the horizontal velocity of this Sprite in pixels per millisecond.
	public void setVelocityX(float dx)
	{
		this.dx = dx;
	}
	
	// Sets the vertical velocity of this Sprite in pixels per millisecond.
	public void setVelocityY(float dy)
	{
		this.dy = dy;
	}
	
	// Gets this Sprite's current image.
	public Image getImage()
	{
		return anim.getImage();
	}
	
	// Sets the sprite type
	public void setType(int type)
	{
		this.type = type;
	}
	
	// Gets the sprite type
	public int getType()
	{
		return type;
	}
	
	// Gets dead status
	public boolean isDead()
	{
		return deathstatus;
	}
	
	// Sets deathstatus to dead
	public void kill()
	{
		deathstatus = true;
	}
	
	// Sets deathstatus to alive
	public void resurrect()
	{
		deathstatus = false;
	}
	
	// Checks if two Sprites collide with one another. Returns false if the two Sprites are the same.
	// Returns false if one of the Sprites is a Creature that is not alive.
	public boolean isCollision(Sprite otherSprite)
	{
		// If the sprites are the same, return false
		if (otherSprite == this)
		{
			return false;
		}
		
		// If one of the sprites is dead, return false
		if ((otherSprite.isDead()) || (this.isDead()))
		{
			return false;
		}
		
		// Get the pixel location of the sprites
		int otherx = Math.round(otherSprite.getX());
		int othery = Math.round(otherSprite.getY());
		int thisx = Math.round(this.getX());
		int thisy = Math.round(this.getY());
		
		// Check if the two sprites' boundaries intersect
		boolean intersect1 = otherx < thisx + this.getWidth();
		boolean intersect2 = othery < thisy + this.getHeight();
		boolean intersect3 = thisx < otherx + otherSprite.getWidth();
		boolean intersect4 = thisy < othery + otherSprite.getHeight();
		return (intersect1 && intersect2 && intersect3 && intersect4);
	}
	
	// Gets the Sprite that collides with the specified Sprite, or null if no Sprite collides with the specified Sprite.
	public Sprite getSpriteCollision(Sprite colSprite)
	{
		// Run through list of sprites
		//for (int x = 0; x < spriteList.length; x++)
		//{
			// Check if collided
			if (isCollision(colSprite))
			{
				// Return colliding sprite
				return colSprite;
			}
		//}
		// Otherwise, return null
		return null;
	}
	
	// Collide horizontally
	public void collideHorizontal()
	{
		// Reset x velocity
		setVelocityX(0);
	}
	
	// Collide vertically
	public void collideVertical()
	{
		// Reset y velocity
		setVelocityY(0);
	}
	
	// Gets max speed
	public float getMaxVelocity()
	{
		// Return maximum velocity
		return MAX_VELOCITY;
	}
}
