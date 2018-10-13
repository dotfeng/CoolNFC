package cn.edu.zju.coolnfc.exceptions;

@SuppressWarnings("serial")
/**
 * Should be thrown when trying to perform NTAG I2C Plus operation
 * on non NTAG I2C Plus tag
 * @author NXP67729
 *
 */
public class NotPlusTagException extends Exception

{
	// Parameterless Constructor
	public NotPlusTagException() {
	}

	// Constructor that accepts a message
	public NotPlusTagException(String message) {
		super(message);
	}

}