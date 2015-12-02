package common;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class TestTimerLogger{

	public static PrintWriter writer;
	public static BufferedOutputStream outStream;

	public static void logException(Exception ex)
	{
		if(outStream == null){
			try {
				outStream = new BufferedOutputStream(new FileOutputStream("test_timer_log_stream.log", true));
			}
			catch(Exception ex1){

			}
		}

		ex.printStackTrace(new PrintStream(outStream));
		try {
			outStream.flush();
		}
		catch(Exception ex2){}
	}

	public static void log(String msg){

		if(writer == null){
			try{
				writer = new PrintWriter("test_timer_log.log", "UTF-8");
			}
			catch(Exception ex){
				
			}
		}

		writer.println(msg);
		writer.flush();
	}
}