package kiwi.mobile.assign2;

// standard library imports
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

// android imports
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// javatuples imports
//import org.javatuples.Triplet;

// json-simple imports
//import org.json.simple.*;
//import org.json.simple.parser.*;

// json-simpler imports
//import kuro.json.JSONAdapter;

public class Assign2Activity extends Activity {
	// members
	private Button launch_button;
	private TextView log_view;

	@Override
	protected void onCreate( Bundle savedInstanceState){
		super.onCreate( savedInstanceState);
		this.setContentView( R.layout.activity_assign2);

		// set up log
		this.log_view = (TextView) findViewById( R.id.log_view);
		this.log_view.setMovementMethod( new ScrollingMovementMethod());
		this.log_view.setTypeface( Typeface.MONOSPACE);

		// set up button
		this.launch_button = (Button) findViewById( R.id.launch_button);
		this.launch_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				Assign2Activity.this.launchBrowseActivity();}
		});}

	public void launchBrowseActivity(){
		this.log( "launching browse activity...");
		Intent intent = new Intent( this, BrowseActivity.class);
		startActivity( intent);
		this.log( "browse activity launched");}

	@Override
	protected void onDestroy(){
		super.onDestroy();}

	/** Write a format string to the logs **/
	private void log( String fmt, Object... params){
		String content = String.format( fmt, params);
		Util.log( content);
		log_view.append( "\n");
		log_view.append( content);

		// scroll log view to bottom
		int scrollAmount = log_view.getLayout().getLineTop(
			log_view.getLineCount()) - log_view.getHeight();
		if( scrollAmount > 0)
			log_view.scrollTo( 0, scrollAmount);
		else
			log_view.scrollTo( 0, 0);}

	protected static class Datetime {
		public int year;
		public int day;
		public long milli;

		public Datetime( int year, int day, long milli){
			this.year = year;
			this.day = day;
			this.milli = milli;}

		public static final Datetime nowUtc(){
			TimeZone utc = TimeZone.getTimeZone( "UTC");
			Calendar calendar = new GregorianCalendar( utc);
			int year = calendar.get( Calendar.YEAR);
			int day = calendar.get( Calendar.DAY_OF_YEAR);
			long hour = calendar.get( Calendar.HOUR_OF_DAY);
			long minute = 60*hour + calendar.get( Calendar.MINUTE);
			long second = 60*minute + calendar.get( Calendar.SECOND);
			long milli = 1000*second + calendar.get( Calendar.MILLISECOND);
			return new Datetime( year, day, milli);}

		public String toString(){
			return String.format(
				"%03x.%03x.%07x",
				this.year, this.day, this.milli);}
	}
}
