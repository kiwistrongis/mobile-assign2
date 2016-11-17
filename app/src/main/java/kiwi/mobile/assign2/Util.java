package kiwi.mobile.assign2;

// android imports
import android.util.Log;

public final class Util {
	private static final String TAG = "Assign2";
	/** Write a format string to the logs **/
	public static void log( String fmt, Object... params){
		String content = String.format( fmt, params);
		Log.v( TAG, content);}

	public static String price_str( long price){
		return String.format(
			"%d.%d", price / 100, price % 100);}
}
