package kiwi.mobile.assign2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class ConvertBTCTask extends AsyncTask<Long,Void,String> {
	private static final String url_str = "https://blockchain.info/tobtc?currency=CAD&value=%s";
	private Exception exception = null;
	private ConvertBTCListener listener = null;

	public ConvertBTCTask( ConvertBTCListener listener){
		this.listener = listener;
	}

	protected String doInBackground( Long... cad){
		String btc = null;
		try {
			URL url = new URL( String.format( url_str, Util.price_str( cad[0])));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if( conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				BufferedReader reader = new BufferedReader(
					new InputStreamReader( conn.getInputStream()));
				btc = reader.readLine();}}
		catch( Exception ex){
			Util.log( "conversion failed: %s", ex);}
		return btc;}

	protected void onPostExecute( String btc){
		this.listener.recv_btc( btc);}
}
