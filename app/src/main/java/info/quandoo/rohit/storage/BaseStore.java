package info.quandoo.rohit.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import info.quandoo.rohit.model.CustomerResponse;
import info.quandoo.rohit.model.TableResponse;

/**
 * Created by Rohit Pawar on 18-07-2018.
 */

public class BaseStore extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "quandoo1";

    // table names
    private static final String TABLE_CUSTOMER = "customer";
    private static final String TABLE_TABLE = "table_table";

    // Customer Table Columns names
    private static final String _ID = "_id";
    private static final String KEY_CUST_FIRSTNAME = "firstname";
    private static final String KEY_CUST_LASTNAME = "lastname";
    private static final String KEY_CUST_ID = "cust_id";


    //	Table Column names
    private static final String KEY_TABLE_ID = "tableId";
    private static final String KEY_TABLE_AVAILABLE = "table_availability";
    private static final String KEY_TABLE_CUST_ID = "table_custid";

    public BaseStore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Customer table
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_CUST_FIRSTNAME + " TEXT,"
                + KEY_CUST_LASTNAME + " TEXT,"
                + KEY_CUST_ID + " INTEGER )";


        //Create Table table
        String CREATE_TABLE_TABLE = "CREATE TABLE " + TABLE_TABLE + "("
                + KEY_TABLE_ID + " INTEGER PRIMARY KEY,"
                + KEY_TABLE_AVAILABLE + " TEXT , "
                + KEY_TABLE_CUST_ID + " INTEGER )";


        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_TABLE_TABLE);

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);

        // Create tables again
        onCreate(db);

    }

    public void addCustomer(CustomerResponse customerResponse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CUST_FIRSTNAME, customerResponse.getCustomerFirstName());
        values.put(KEY_CUST_LASTNAME, customerResponse.getCustomerLastName());
        values.put(KEY_CUST_ID, customerResponse.getId());


        // Inserting Row
        db.insert(TABLE_CUSTOMER, null, values);
        db.close(); // Closing database connection
    }

    // Getting All InterfaceEntry entry
    public ArrayList<CustomerResponse> getAllCustomers() {
        ArrayList<CustomerResponse> customerResponseArrayList = new ArrayList<>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CustomerResponse customerResponse = new CustomerResponse();
                customerResponse.setCustomerFirstName((cursor.getString(1)));
                customerResponse.setCustomerLastName(cursor.getString(2));
                customerResponse.setId(cursor.getInt(3));

                // Adding interface to list
                customerResponseArrayList.add(customerResponse);
            } while (cursor.moveToNext());
        }

        // return interface list
        return customerResponseArrayList;
    }

    public void deleteAllCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER, null, null);
        db.close();
    }


    public void addTable(String availality) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TABLE_AVAILABLE, availality);
        values.put(KEY_TABLE_CUST_ID, 999);

        // Inserting Row
        db.insert(TABLE_TABLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Backout entry
    public ArrayList<TableResponse> getAllTables() {
        ArrayList<TableResponse> tableResponseArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TableResponse tableResponse = new TableResponse();
                tableResponse.setTable_Id(cursor.getInt(0));
                tableResponse.setAvailible(Boolean.valueOf(cursor.getString(1)));
                tableResponse.setCustId(cursor.getInt(2));


                // Adding table to list
                tableResponseArrayList.add(tableResponse);
            } while (cursor.moveToNext());
        }

        // return interface list
        return tableResponseArrayList;
    }


    public boolean updateTable(TableResponse tableResponse) {
        SQLiteDatabase mDb = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(KEY_TABLE_AVAILABLE, "false");
        args.put(KEY_TABLE_CUST_ID, tableResponse.getCustId());
        return mDb.update(TABLE_TABLE, args, KEY_TABLE_ID + "=" + tableResponse.getTable_Id(), null) > 0;
    }


    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TABLE, null, null);
        db.close();
    }

    public TableResponse getTableBycusrID(int custId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TableResponse tableResponse = null;
        Cursor cursor = db.query(TABLE_TABLE, new String[]{KEY_TABLE_ID,
                        KEY_TABLE_AVAILABLE, KEY_TABLE_CUST_ID}, KEY_TABLE_CUST_ID + "=?",
                new String[]{String.valueOf(custId)}, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

             tableResponse = new TableResponse(
                    cursor.getInt(0),
                    Boolean.valueOf(cursor.getString(1)),
                    cursor.getInt(2));
        }
        // return interface
        return tableResponse;
    }

}
