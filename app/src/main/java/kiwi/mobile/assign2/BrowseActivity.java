package kiwi.mobile.assign2;

// standard library imports
import java.util.Vector;

// android imports
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// android support imports
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;

public class BrowseActivity extends AppCompatActivity 
		implements ConvertBTCListener {
	private final static int ADD_PROD_REQ = 1;
	private Vector<Product> products = null;
	private int products_size = 0;
	private int products_i = 0;
	private Product selected = null;
	ConvertBTCTask converter = null;

	// android ui components
	private Button next_button;
	private Button prev_button;
	private Button delete_button;
	private TextView view_name;
	private TextView view_price_cad;
	private TextView view_price_btc;
	private TextView view_desc;

	@Override
	protected void onCreate( Bundle bundle){
		super.onCreate( bundle);
		this.setContentView( R.layout.activity_browse);

		// set up text views
		this.view_name = (TextView) findViewById( R.id.view_name);
		this.view_price_cad = (TextView) findViewById( R.id.view_price_cad);
		this.view_price_btc = (TextView) findViewById( R.id.view_price_btc);
		this.view_desc = (TextView) findViewById( R.id.view_desc);

		// set up buttons
		this.next_button = (Button) findViewById( R.id.next_button);
		this.prev_button = (Button) findViewById( R.id.prev_button);
		this.delete_button = (Button) findViewById( R.id.delete_button);
		this.next_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				BrowseActivity.this.next_prod();}});
		this.prev_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				BrowseActivity.this.prev_prod();}});
		this.delete_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				BrowseActivity.this.delete_prod();}});
		this.next_button.setEnabled( false);
		this.prev_button.setEnabled( false);
		this.delete_button.setEnabled( false);
		// load product data
		this.refresh_products();}

	@Override
	public boolean onCreateOptionsMenu( Menu menu){
		super.onCreateOptionsMenu( menu);
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate( R.menu.main, menu);
		return true;}

	@Override
	public boolean onOptionsItemSelected( MenuItem item){
		int id = item.getItemId();
		if( R.id.action_add_product == id){
			launchAddProductActivity();
			return true;}
		return super.onOptionsItemSelected( item);}

	public void launchAddProductActivity(){
		Util.log( "launching add product activity...");
		Intent intent = new Intent( this, AddProductActivity.class);
		startActivityForResult( intent, ADD_PROD_REQ);
		Util.log( "add product activity launched");}

	@Override
	protected void onDestroy(){
		super.onDestroy();}

	public void next_prod(){
		this.cancel_converter();
		this.show_i( this.products_i + 1);}
	public void prev_prod(){
		this.cancel_converter();
		this.show_i( this.products_i - 1);}
	public void delete_prod(){
		this.cancel_converter();
		if( this.selected == null)
			return;
		new ProductDeleter().execute( this);}
	public void cancel_converter(){
		if( this.converter != null)
			this.converter.cancel( true);}

	public void show_i( int i){
		if( i < 0 || i >= products_size){
			Util.log( "invalid i: %d", i);
			return;}
		this.products_i = i;
		this.selected = this.products.get( i);
		this.showProduct( this.selected);
		this.refresh_buttons();}
	public void showProduct( Product product){
		this.view_name.setText( product.name);
		this.view_desc.setText( product.desc);
		this.view_price_cad.setText( Util.price_str( product.price));
		this.view_price_btc.setText( R.string.loading_btc);
		this.converter = new ConvertBTCTask( this);
		this.converter.execute( product.price);}

	public void reset_ui(){
		this.view_name.setText( "");
		this.view_price_cad.setText( "");
		this.view_price_btc.setText( "");
		this.view_desc.setText( "");
		this.next_button.setEnabled( false);
		this.prev_button.setEnabled( false);
		this.delete_button.setEnabled( false);}
	public void refresh_products(){
		new ProductLoader().execute( this);}
	public void refresh_buttons(){
		this.next_button.setEnabled( this.products_size > this.products_i + 1);
		this.prev_button.setEnabled( 0 < this.products_i);
		this.delete_button.setEnabled( this.products_size > 0);}

	private class ProductLoader extends AsyncTask<Context,Void,Vector<Product>> {
		@Override
		protected Vector<Product> doInBackground( Context... params){
			ProductDBAdapter adapter = new ProductDBAdapter( params[0]);
			return adapter.list();}
		@Override
		protected void onPostExecute( Vector<Product> products){
			BrowseActivity.this.products = products;
			BrowseActivity.this.products_size = products.size();
			BrowseActivity.this.show_i( 0);}
	}
	private class ProductDeleter extends AsyncTask<Context,Void,Vector<Product>> {
		@Override
		protected Vector<Product> doInBackground( Context... params){
			ProductDBAdapter adapter = new ProductDBAdapter( params[0]);
			adapter.remove( BrowseActivity.this.selected);
			return adapter.list();}
		@Override
		protected void onPostExecute( Vector<Product> products){
			BrowseActivity.this.products = products;
			BrowseActivity.this.products_size = products.size();
			BrowseActivity.this.reset_ui();
			BrowseActivity.this.show_i( 0);}
	}

	@Override
	protected void onActivityResult( int req, int result, Intent data) {
		Util.log( "activity result: %d, %d", req, result);
		if( req == ADD_PROD_REQ && result == RESULT_OK){
			this.cancel_converter();
			Util.log( "refreshing!");
			this.reset_ui();
			this.refresh_products();}}

	@Override
	public void recv_btc( String btc) {
		String text = btc == null ? "Conversion failed..." : btc;
		this.view_price_btc.setText( text);}
}
