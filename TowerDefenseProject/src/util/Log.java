package util;

public class Log {
	
	private static boolean debug_mode = true;

	public static void i (String msg)
	{
		System.out.printf("[Info.] %s\n", msg);
	}
	
	public static void e (String msg)
	{
		System.out.printf("[ERROR] %s\n", msg);
	}
	
	public static void d (String msg)
	{
		if(debug_mode) System.out.printf("[Debug] %s\n", msg);
	}
	
	
}
