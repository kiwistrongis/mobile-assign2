package kiwi.mobile.assign2;

// android imports
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class AddProductActivity extends Activity {
	private EditText field_name = null;
	private EditText field_desc = null;
	private EditText field_price = null;
	private Button cancel_button = null;
	private Button save_button = null;

	@Override
	protected void onCreate( Bundle savedInstanceState){
		super.onCreate( savedInstanceState);
		this.setContentView( R.layout.activity_add_product);

		// set up text fields
		this.field_name = (EditText) findViewById( R.id.field_name);
		this.field_desc = (EditText) findViewById( R.id.field_desc);
		this.field_price = (EditText) findViewById( R.id.field_price);

		// set up buttons
		this.cancel_button = (Button) findViewById( R.id.cancel_button);
		this.save_button = (Button) findViewById( R.id.save_button);
		this.cancel_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				AddProductActivity.this.setResult( RESULT_CANCELED);
				AddProductActivity.this.finish();}});
		this.save_button.setOnClickListener( new View.OnClickListener(){
			public void onClick( View view){
				new ProductDeleter().execute( AddProductActivity.this);}});}

	private class ProductDeleter extends AsyncTask<Context,Void,Long> {
		@Override
		protected Long doInBackground( Context... params){
			Long id = (long) -1;
			try {
				String name = AddProductActivity.this.field_name.getText().toString();
				String desc = AddProductActivity.this.field_desc.getText().toString();
				String price_str =
					AddProductActivity.this.field_price.getText().toString();
				if( ! price_str.matches( "\\d+\\.\\d\\d")){
					return id;}
				price_str = price_str.replaceAll( "\\.", "");
				Long price = Long.valueOf( price_str);

				ProductDBAdapter adapter = new ProductDBAdapter( params[0]);
				Product product = adapter.add( new Product( name, desc, price));
				id = product.id;}
			catch( Exception exception){
				Util.log( "failed to add product : %s", exception);}
			return id;}

		@Override
		protected void onPostExecute( Long id){
			if( id >= 0){
				Util.log( "saved product: %d", id);
				AddProductActivity.this.setResult( RESULT_OK);
				AddProductActivity.this.finish();}
			else {
				Toast.makeText(
					AddProductActivity.this, "Invalid Price!",
					Toast.LENGTH_SHORT).show();}}
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();}
}
