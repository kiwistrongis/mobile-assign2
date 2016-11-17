package kiwi.mobile.assign2;

// standard library imports


// android imports
import android.content.ContentValues;

public class Product {
	public long id;
	public String name;
	public String desc;
	public long price;

	public Product(){
		this( -1, "Unnamed Product", "No Description Available.", 0);}
	public Product( String name, String desc, long price){
		this( -1, name, desc, price);}
	public Product( long id, String name, String desc, long price){
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.price = price;}

	public ContentValues toContentValues(){
		ContentValues values = new ContentValues();
		values.put( "name", this.name);
		values.put( "desc", this.desc);
		values.put( "price", this.price);
		return values;}
}