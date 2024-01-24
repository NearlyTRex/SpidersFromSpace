// Spiders From Space
// Waterstone Productions
// CS 497 Student Project

// Imports
import java.io.*;

// Main class
public class Main
{
	// Main method
	public static void main(String args[])
	{
		// Game methods
		SpidersFromSpace game = new SpidersFromSpace("./images/", ".png");
		
		// Timer
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		long elapsedTime;
		
		// Attempt at running
		try
		{
			// Start out level
			game.start();
			
			// Run game loop
			while(game.isRunning())
			{
				// Update elapsed time
				elapsedTime = System.currentTimeMillis() - currTime;
				currTime += elapsedTime;
				
				// Update game
				game.update(elapsedTime);
				
				// Redraw screen
				game.redraw(elapsedTime);
			}
		}
		finally
		{
			// Exit game
			game.exit();
		}
	}
}