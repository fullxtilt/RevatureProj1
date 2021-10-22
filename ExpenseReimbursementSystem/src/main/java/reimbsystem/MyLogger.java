package reimbsystem;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Simple log4j utility class. 
 * @author Ryan Moss
 *
 */
public class MyLogger {
	
	private static Level logLevel = Level.ALL;
	
	/**
	 * Retrieves a logger that will log using the provided object's class name.
	 * @param clazz the class where you are logging from
	 * @return
	 */
	public static Logger getLoggerForClass(Object clazz) {
		
		Logger myLogger = Logger.getLogger(clazz.getClass());
		myLogger.setLevel(logLevel);
		
		return myLogger;
	}
	
	/**
	 * Wraps around Logger's .getLogger() method.
	 */
	public static <T> Logger getLoggerForClass(Class<T> clazz) {
		Logger myLogger = Logger.getLogger(clazz);
		myLogger.setLevel(logLevel);
		
		return myLogger;
	}
}