package kiwi.mobile.assign2;

// standard library imports
import java.util.Vector;

// android imports
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class ProductDBAdapter extends SQLiteOpenHelper {
	public static final int DB_VER = 2;
	public static final String DB_FILE = "products.db";
	public static final String DB_TABLE = "Products";
	public static final String[] DB_COLS = new String[] {
		"id", "name", "desc", "price"};

	public static final String SQL_CREATE = 
		String.format(
			"create table %s (" +
			"  id integer primary key autoincrement, " +
			"  name text not null," +
			"  desc text not null," +
			"  price int not null)",
			DB_TABLE);
	public static final String SQL_DESTROY =
		String.format( "drop table %s", DB_TABLE);

	public ProductDBAdapter( Context context){
		super( context, DB_FILE, null, DB_VER);
	}

	// main crud operations
	public Vector<Product> list(){
		SQLiteDatabase db = this.getWritableDatabase();
		Vector<Product> products = new Vector<Product>();

		Cursor cursor = db.query(
			DB_TABLE, DB_COLS, "", new String[]{}, "", "", "");
		if( cursor.getCount() == 0)
			return products;

		cursor.moveToFirst();
		while( ! cursor.isAfterLast()){
			long id = Long.parseLong( cursor.getString( 0));
			String name = cursor.getString( 1);
			String desc = cursor.getString( 2);
			long price = Long.parseLong( cursor.getString( 3));
			products.add( new Product(
				id, name, desc, price));
			cursor.moveToNext();}
		return products;}

	public Product search( long id){
		SQLiteDatabase database = this.getWritableDatabase();
		Product product = null;

		Cursor cursor = database.query(
			DB_TABLE, DB_COLS, String.format( "id = %d", id), null, "", "", "");
		if( cursor.getCount() > 0){
			cursor.moveToFirst();
			String name = cursor.getString( 1);
			String desc = cursor.getString( 2);
			long price = Long.parseLong( cursor.getString( 3));
			product = new Product( id, name, desc, price);}
		return product;}

	public Product add( Product product){
		SQLiteDatabase db = this.getWritableDatabase();
		product.id = db.insert( DB_TABLE, null, product.toContentValues());
		return product;}

	public Product remove( Product product){
		product.id = this.remove( product.id);
		return product;}

	public long remove( long id){
		SQLiteDatabase db = this.getWritableDatabase();
		int ret = db.delete( DB_TABLE, String.format( "id = %d", id), null);
		return ( ret > 0) ? -1 : id;}

	// super class overrides
	@Override
	public void onCreate( SQLiteDatabase db){
		db.execSQL( SQL_CREATE);}

	@Override
	public void onUpgrade( SQLiteDatabase db, int ver_old, int ver_new){
		db.execSQL( SQL_DESTROY);
		db.execSQL( SQL_CREATE);}
}