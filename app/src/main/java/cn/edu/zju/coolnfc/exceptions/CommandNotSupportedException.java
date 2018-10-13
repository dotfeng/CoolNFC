package cn.edu.zju.coolnfc.exceptions;

@SuppressWarnings("serial")
/**
 * Should be thrown when Dynamic Lock bits are set
 * @author NXP67729
 *
 */
public class CommandNotSupportedException extends Exception

{
	// Parameterless Constructor
	public CommandNotSupportedException() {
	}

	// Constructor that accepts a message
	public CommandNotSupportedException(String message) {
		super(message);
	}

}