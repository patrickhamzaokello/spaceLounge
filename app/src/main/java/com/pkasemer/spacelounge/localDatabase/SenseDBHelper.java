package com.pkasemer.spacelounge.localDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pkasemer.spacelounge.Models.FoodDBModel;

import java.util.ArrayList;
import java.util.List;

public class SenseDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "zodongo";
    private static final int DB_VERSION = 7;
    private static final String TABLE_NAME = "CART";
    public static final String COLUMN_id = "_id";
    public static final String COLUMN_menuId = "menuId";
    public static final String COLUMN_menuName = "menuName";
    public static final String COLUMN_price = "price";
    public static final String COLUMN_description = "description";
    public static final String COLUMN_menuTypeId = "menuTypeId";
    public static final String COLUMN_menuImage = "menuImage";
    public static final String COLUMN_backgroundImage = "backgroundImage";
    public static final String COLUMN_ingredients = "ingredients";
    public static final String COLUMN_menuStatus = "menuStatus";
    public static final String COLUMN_created = "created";
    public static final String COLUMN_modified = "modified";
    public static final String COLUMN_rating = "rating";
    public static final String COLUMN_quantity = "quantity";
    public static final String COLUMN_order_status = "order_status";



    List<FoodDBModel> foodDBModelList;

    public SenseDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE CART (" +
                COLUMN_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_menuId + " INTEGER," +
                COLUMN_menuName + " TEXT," +
                COLUMN_price + " INTEGER," +
                COLUMN_description + " TEXT," +
                COLUMN_menuTypeId +" INTEGER," +
                COLUMN_menuImage + " TEXT," +
                COLUMN_backgroundImage + " TEXT," +
                COLUMN_ingredients + " TEXT," +
                COLUMN_menuStatus + " INTEGER," +
                COLUMN_created + " TEXT," +
                COLUMN_modified + " TEXT," +
                COLUMN_rating + " INTEGER," +
                COLUMN_quantity + " INTEGER DEFAULT 1," +
                COLUMN_order_status + " TINYINT )";

        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }


    public List<FoodDBModel> listTweetsBD() {
        String sql = "select * from " + TABLE_NAME + " order by " + COLUMN_id + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        foodDBModelList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                int menuId = Integer.parseInt(cursor.getString(1));
                String menuname = cursor.getString(2);
                int price = Integer.parseInt(cursor.getString(3));
                String description = cursor.getString(4);
                int menutypid = Integer.parseInt(cursor.getString(5));
                String menuimage = cursor.getString(6);
                String backgroundimage = cursor.getString(7);
                String ingredients = cursor.getString(8);
                int menuStatus = Integer.parseInt(cursor.getString(9));
                String created = cursor.getString(10);
                String modified = cursor.getString(11);
                int rating = Integer.parseInt(cursor.getString(12));
                int quantity = Integer.parseInt(cursor.getString(13));
                int order_status = Integer.parseInt(cursor.getString(14));

                foodDBModelList.add(new FoodDBModel(menuId, menuname, price,description, menutypid, menuimage,backgroundimage, ingredients,menuStatus, created,modified, rating,quantity,order_status));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return foodDBModelList;
    }

    public void addTweet(
            int menuId,
            String menuname,
            int price,
            String description,
            int menutypeid,
            String menuImage,
            String backgroundImage,
            String ingredients,
            int menustatus,
            String created,
            String modified,
            int rating,
            int quantity,
            int order_status
    ) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_menuId, menuId);
        values.put(COLUMN_menuName, menuname);
        values.put(COLUMN_price, price);
        values.put(COLUMN_description, description);
        values.put(COLUMN_menuTypeId, menutypeid);
        values.put(COLUMN_menuImage, menuImage);
        values.put(COLUMN_backgroundImage, backgroundImage);
        values.put(COLUMN_ingredients, ingredients);
        values.put(COLUMN_menuStatus, menustatus);
        values.put(COLUMN_created, created);
        values.put(COLUMN_modified, modified);
        values.put(COLUMN_rating, rating);
        values.put(COLUMN_quantity, quantity);
        values.put(COLUMN_order_status, order_status);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


   public void updateMenuCount(Integer qtn, Integer menuID) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_quantity, qtn);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COLUMN_menuId + " = ?", new String[]{String.valueOf(menuID)});
        db.close();
    }


    public void deleteTweet(String id_str) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_menuId + " = ?", new String[]{id_str});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public boolean checktweetindb(String id_str) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_menuId, COLUMN_menuName, COLUMN_description},
                COLUMN_menuId + " = ?",
                new String[]{id_str},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            //recordexist
            cursor.close();
            db.close();
            return false;
        } else {
            //record not existing
            cursor.close();
            db.close();

            return true;
        }
    }

    public int countCart(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount= db.rawQuery("select count(*) from " + TABLE_NAME, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        db.close();

        return count;
    }


    public int getMenuQtn(String id_str) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_quantity},
                COLUMN_menuId + " = ?",
                new String[]{id_str},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            //recordexist
            int count= cursor.getInt(0);
            cursor.close();
            db.close();
            return count;
        } else {
            //record not existing
            int count= 1;
            cursor.close();
            db.close();
            return count;
        }
    }

    public int sumPriceCartItems() {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum("+ COLUMN_price + " * " + COLUMN_quantity + ") from " + TABLE_NAME, null);
        if (cursor.moveToFirst()) result = cursor.getInt(0);
        cursor.close();
        db.close();
        return result;
    }


    public Cursor getUnsyncedMenuItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_order_status + " = 0";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


}
