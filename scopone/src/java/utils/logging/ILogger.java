package utils.logging;

public interface ILogger {
	public enum LogLevel{
		ALL,
		ERROR,
		GENERIC,
		SPECIFIC
	}
	
	//void log(String text, LogLevel logLevel);
}
