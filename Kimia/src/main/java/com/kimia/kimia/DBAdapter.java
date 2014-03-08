package com.kimia.kimia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    private Cursor cursor;
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "kimia-db";
    private static final int DATABASE_VERSION = 85;
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /*******************************************************Table Accounts*************************/

    private static final String Accounts = "Accounts";
    private static final String AccountID = "AccountID";
    private static final String AccountGroupID = "AccountGroupID";
    private static final String AccountCode = "AccountCode";
    private static final String AccountName = "AccountName";
    private static final String AccountTelephone = "AccountTelephone";
    private static final String AccountMobile = "AccountMobile";
    private static final String AccountFax = "AccountFax";
    private static final String AccountPreference = "AccountPreference";
    private static final String AccountTip = "AccountTip";
    private static final String AccountVisible = "AccountVisible";
    private static final String AccountImage = "AccountImage";

    private static final String CreateTableAccounts = "CREATE TABLE " + Accounts + " ("
            + AccountID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + AccountGroupID + " INTEGER NOT NULL, "
            + AccountCode + " INTEGER NOT NULL UNIQUE, "
            + AccountName + " TEXT NOT NULL, "
            + AccountTelephone + " TEXT, "
            + AccountMobile + " TEXT, "
            + AccountFax + " TEXT, "
            + AccountPreference + " BLOB, "
            + AccountTip + " TEXT, "
            + AccountVisible + " BLOB, "
            + AccountImage + " Text);";

    /*************************************************Table Product Groups*************************/

    private static final String ProductGroups = "ProductGroups";
    private static final String ProductGroupID = "ProductGroupID";
    private static final String ProductGroupName = "ProductGroupName";

    private static final String CreateTableProductGroups = "CREATE TABLE " + ProductGroups + " ("
            + ProductGroupID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductGroupName + " TEXT NOT NULL UNIQUE);";

    /*************************************************Table Factors********************************/

    private static final String Factors = "Factors";
    private static final String FactorID = "FactorID";
    private static final String FactorAccountsID = "FactorAccountsID";
    private static final String FactorNumber = "FactorNumber";
    private static final String FactorIsPrinted = "FactorIsPrinted";
    private static final String FactorTip = "FactorTip";

    private static final String CreateTableFactors = "CREATE TABLE " + Factors + " ("
            + FactorID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "FOREIGN KEY (" + FactorAccountsID + ") REFERENCES " + Accounts + " (" + AccountID + ") "
            + FactorNumber + " INTEGER NOT NULL, "
            + FactorIsPrinted + " BLOB, "
            + FactorTip + " TEXT);";

    /************************************************Table Products********************************/

    private static final String Products = "Products";
    private static final String ProductID = "ProductID";
    private static final String ProductCode = "ProductCode";
    private static final String ProductGroupsID = "ProductGroupsId";
    private static final String ProductName = "ProductName";
    private static final String ProductMaker = "ProductMaker";
    private static final String ProductIsVisible = "ProductIsVisible";
    private static final String ProductTip = "ProductTip";
    private static final String ProductDN1 = "ProductDN1";      //dn == defulte name
    private static final String ProductDN2 = "ProductDN2";
    private static final String ProductDN3 = "ProductDN3";
    private static final String ProductDN4 = "ProductDN4";
    private static final String ProductDN5 = "ProductDN5";
    private static final String ProductDN6 = "ProductDN6";
    private static final String ProductAN1 = "ProductAN1";      //an == avalable name
    private static final String ProductAN2 = "ProductAN2";
    private static final String ProductAN3 = "ProductAN3";
    private static final String ProductAN4 = "ProductAN4";
    private static final String ProductAN5 = "ProductAN5";
    private static final String ProductAN6 = "ProductAN6";

    private static final String CreateTableProducts = "CREATE TABLE " + Products + " ("
            + ProductID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ProductCode + " INTEGER NOT NULL UNIQUE, "
            + ProductGroupsID + " INTEGER, "
            + ProductName + " TEXT NOT NULL, "
            + ProductIsVisible + " BLOB, "
            + ProductTip + " TEXT, "
            + ProductDN1 + " INTEGER, "
            + ProductDN2 + " INTEGER, "
            + ProductDN3 + " INTEGER, "
            + ProductDN4 + " INTEGER, "
            + ProductDN5 + " INTEGER, "
            + ProductDN6 + " INTEGER, "
            + ProductAN1 + " INTEGER, "
            + ProductAN2 + " INTEGER, "
            + ProductAN3 + " INTEGER, "
            + ProductAN4 + " INTEGER, "
            + ProductAN5 + " INTEGER, "
            + ProductAN6 + " INTEGER, "
            + ProductMaker + " INTEGER, "
            + "UNIQUE (" + ProductGroupsID + ", " + ProductName + ") ON CONFLICT REPLACE, "
            + "FOREIGN KEY(" + ProductGroupsID + ") REFERENCES " + ProductGroups + "(" + ProductGroupID + "), "
            + "FOREIGN KEY (" + ProductMaker + ") REFERENCES " + Accounts + " (" + AccountID + "));";

    /************************************************Table Orders**********************************/

    private static final String Orders = "Orders";
    private static final String OrderID = "OrderID";
    private static final String OrderFactorsID = "OrderFactorsID";
    private static final String OrderProductsID = "OrderProductsID";
    private static final String OrderShamsi = "OrderShamsi";
    private static final String OrderCommitShamsi = "OrderCommitShamsi";
//    private static final String OrderMiladi = "OrderMiladi";
//    private static final String OrderCommitMiladi = "OrderCommitMiladi";
    private static final String OrderTip = "OrderTip";
    private static final String OrderN1 = "OrderN1";
    private static final String OrderN2 = "OrderN2";
    private static final String OrderN3 = "OrderN3";
    private static final String OrderN4 = "OrderN4";
    private static final String OrderN5 = "OrderN5";
    private static final String OrderN6 = "OrderN6";
    private static final String OrderCN1 = "OrderCN1";
    private static final String OrderCN2 = "OrderCN2";
    private static final String OrderCN3 = "OrderCN3";
    private static final String OrderCN4 = "OrderCN4";
    private static final String OrderCN5 = "OrderCN5";
    private static final String OrderCN6 = "OrderCN6";
    private static final String OrderIsCommited = "OrderIsCommited";

    private static final String CreateTableOrders = "CREATE TABLE " + Orders + " ("
            + OrderID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "FOREIGN KEY (" + OrderFactorsID + ") REFERENCES " + Factors + " (" + FactorID + ") "
            + "FOREIGN KEY (" + OrderProductsID + ") REFERENCES " + Products + " (" + ProductID + ") "
            + OrderShamsi + " NUMERIC, "
            + OrderCommitShamsi + " NUMERIC, "
            + OrderTip + "Text"
            + OrderN1 + " INTEGER, "
            + OrderN2 + " INTEGER, "
            + OrderN3 + " INTEGER, "
            + OrderN4 + " INTEGER, "
            + OrderN5 + " INTEGER, "
            + OrderN6 + " INTEGER, "
            + OrderCN1 + " INTEGER, "
            + OrderCN2 + " INTEGER, "
            + OrderCN3 + " INTEGER, "
            + OrderCN4 + " INTEGER, "
            + OrderCN5 + " INTEGER, "
            + OrderCN6 + " INTEGER, "
            + OrderIsCommited + "BLOB);";

    /**********************************************************************************************/

    public DBAdapter(Context ctx){
        this.context=ctx;
        DBHelper=new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            try {
                db.execSQL(CreateTableAccounts);
                db.execSQL(CreateTableProductGroups);
                db.execSQL(CreateTableProducts);
                //db.execSQL(CreateTableFactors);
                //db.execSQL(CreateTableOrders);
            }catch ( SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version" + oldVersion + "to" + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+ Accounts);
        //    db.execSQL("DROP TABLE IF EXISTS "+ Products);
        //    db.execSQL("DROP TABLE IF EXISTS "+ ProductGroups);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public int MaxCod() {
        int Max=0;
        Cursor m;
        m = db.rawQuery("SELECT MAX(" + AccountCode + ") FROM " + Accounts , null);

        if(m.moveToFirst())
            do{
                Max = m.getInt((0));
            }while(m.moveToNext());

        return Max;
    }

    public long insertAc(int grupId, int cod, String accNA, String tel, String mob, String fax, boolean ispre, String tip,boolean isvisi ){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AccountGroupID, grupId);
        initialValues.put(AccountCode, cod);
        initialValues.put(AccountName, accNA);
        initialValues.put(AccountTelephone, tel);
        initialValues.put(AccountMobile, mob);
        initialValues.put(AccountFax, fax);
        initialValues.put(AccountPreference, ispre);
        initialValues.put(AccountTip, tip);
        initialValues.put(AccountVisible, isvisi);

        try {
            return db.insertOrThrow(Accounts ,null,initialValues);
        } catch (Exception e) {
            if (e.getMessage().equals("column" + AccountCode + "  is not unique (code 19)"))
                return -2;
            else
                return -3;
        }
    }

    /**************************************************Insert Product******************************/

    public long insertProduct(int code, int groupsID, String name, int maker, boolean visible, String tip){
        ContentValues initialValues = new ContentValues();
        initialValues.put(ProductCode, code);
        initialValues.put(ProductGroupsID, groupsID);
        initialValues.put(ProductName, name);
        initialValues.put(ProductMaker, maker);
        initialValues.put(ProductIsVisible, visible);
        initialValues.put(ProductTip, tip);

        try {
            return db.insertOrThrow(Products, null, initialValues);
        } catch (Exception e) {
            return -2;
        }
    }

    /****************************************************Product Max Cod***************************/

    public int ProductMaxCod() {
        int Max=0;
        Cursor m;
        m = db.rawQuery("SELECT MAX(" + ProductCode + ") FROM " + Products , null);

        if(m.moveToFirst())
            do{
                Max = m.getInt((0));
            }while(m.moveToNext());

        return Max;
    }

    /**************************************************Filter Products Group***********************/

    public Cursor filterProductsGroup(String filter){

        cursor = db.query(
                true, ProductGroups,
                new String[] {ProductGroupID, ProductGroupName},
                ProductGroupName + " LIKE ?",
                new String[] {"%" + filter + "%"},
                null, null, ProductGroupName, null );
        return cursor;
    }

    /**************************************************Filter Accounts*****************************/

    public Cursor filterAccounts(String filter){

        cursor = db.rawQuery("SELECT " + AccountID + " , " + AccountName + " FROM "
                + Accounts + " WHERE " + AccountGroupID + " = 1 AND " + AccountName + " LIKE "
                + "'%" + filter + "%';", null);


      /*  cursor = db.query(
                true, Accounts,
                new String[] {AccountID, AccountName},
                AccountName + " LIKE ?",
                new String[] {"%" + filter + "%"},
                null, null, AccountName, null);*/
        return cursor;
    }

    /********************************************************Insert Product Group*******************/

    public long insertProductsGroup(String name){
        ContentValues initialValues = new ContentValues();
        initialValues.put(ProductGroupName, name);

        try {
            return db.insert(ProductGroups, null, initialValues);
        } catch (Exception e) {
            return -1;
        }
    }

    /**********************************************************************************************/


    public boolean deleteAc(long rowId){
        return db.delete(Accounts, AccountID + "=" + rowId, null)> 0;
    }

   // public Cursor getALLac(){
   //     return db.query(Accounts ,new String[]{AccountID,AccountName,AccountGroupID,AccountCode ,AccountTelephone, AccountMobile, AccountFax, AccountTip, AccountVisible, AccountPreference},null ,null,null,null,AccountName);
   // }

    public Cursor getALLacname(){
        cursor = db.query(Accounts,new String[]{AccountID,AccountName},null ,null,null,null,AccountName);
        return cursor;
    }

    /************************************************Get All Products Name*************************/

    public Cursor getAllProductName(){
        cursor = db.query(Products, new String[]{ProductID, ProductName}, null, null, null, null , ProductName);
        return cursor;
    }

    /**********************************************************************************************/

    public int lastUser(){
        int Max = 0;
        int r=0;
        Cursor m;
        m = db.rawQuery("SELECT MAX("+ AccountID+ ") FROM "+Accounts, null);
        if(m.moveToFirst())
            do{
                Max = m.getInt((0));
            }while(m.moveToNext());

        if(Max>0){
            Cursor mCursor = db.query(true,Accounts, new String[] {AccountGroupID}, AccountID + "=" + Max, null, null, null, null, null);

            if (mCursor != null){
                mCursor.moveToFirst();
                r = Integer.parseInt(mCursor.getString(0));
            }
        }
        return r;
    }

    public Cursor getAc(long rowId) throws SQLException{
        Cursor mCursor = db.query(true,Accounts, new String[] {AccountID,AccountName,AccountGroupID,AccountCode ,AccountTelephone, AccountMobile, AccountFax, AccountTip, AccountPreference, AccountVisible}, AccountID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /***********************************************Get Products Group*****************************/

    public int getProductsGroupID(String name) throws SQLException {
        cursor = db.rawQuery("SELECT " + ProductGroupID + "  FROM " + ProductGroups + " WHERE " + ProductGroupName + " = '" + name + "'", null);

        if(cursor.moveToFirst())
                return cursor.getInt(0);

        return -1;
    }

    /**************************************************Get Accounts Name***************************/

    public int getAccountsNameID(String name) throws SQLException {
        cursor = db.rawQuery("SELECT " + AccountID + "  FROM " + Accounts + " WHERE " + AccountName + " = '" + name + "'", null);

        if(cursor.moveToFirst())
            return cursor.getInt(0);

        return -1;
    }

    /***********************************************Get Product************************************/

    public Cursor getProduct(long ID) throws SQLException{

        cursor = db.rawQuery("SELECT "
                + ProductName + ", "
                + ProductCode + ", "
                + ProductTip + ", "
                + ProductIsVisible + ", "
                + AccountName + ", "
                + ProductGroupName
                + " FROM " + Products
                + " LEFT OUTER JOIN " + Accounts
                + " ON " + Products + "." + ProductMaker + " = " + Accounts + "." + AccountID
                + " LEFT OUTER JOIN " + ProductGroups
                + " ON " + Products + "." + ProductGroupsID + " = " + ProductGroups + "." + ProductGroupID
                + " WHERE " + ProductID + " = " + ID, null);

        return cursor;
    }


    /************************************************Get Products Group Name***********************/

    public String getProductsGroupName(long rowId) throws SQLException{

        cursor = db.rawQuery("SELECT " + ProductGroupName + "  FROM " + ProductGroups + " WHERE " + ProductGroupID + " = " + rowId, null);

        if(cursor.moveToFirst())
            return cursor.getString((0));

        return "null";
    }

    /************************************************Get Maker*************************************/

    public String getMaker(int rowId) throws SQLException{

        cursor = db.rawQuery("SELECT " + AccountName + "  FROM " + Accounts + " WHERE " + AccountID + " = " + rowId + " ;", null);

        if(cursor.moveToFirst())
            return cursor.getString((0));

        return "null";
    }

    /**********************************************************************************************/

    public long updateContact(int rowId, int grupId, int cod, String accNA, String tel, String mob, String fax, boolean ispre, String tip,boolean isvisi ){
        ContentValues initialValues = new ContentValues();
        initialValues.put(AccountGroupID, grupId);
        initialValues.put(AccountName, accNA);
        initialValues.put(AccountTelephone, tel);
        initialValues.put(AccountMobile, mob);
        initialValues.put(AccountFax, fax);
        initialValues.put(AccountPreference, ispre);
        initialValues.put(AccountTip, tip);
        initialValues.put(AccountCode, cod);
        initialValues.put(AccountVisible, isvisi);
        //  return db.update(DATABASE_TABLE,initialValues,KEY_ID + "=" + rowId, null) > 0;
        // return ContentUris.withAppendedId( Uri.EMPTY,id);
        return (long) db.updateWithOnConflict(Accounts,initialValues,AccountID + "=" + rowId,null, SQLiteDatabase.CONFLICT_IGNORE);
        // }

        //  try {
        //   boolean n;

        //     db.update(DATABASE_TABLE,initialValues,KEY_ID + "=" + rowId, null) > 0;
        //  if (n=true)
        //      return 1;
        //    return db.insertOrThrow(DATABASE_TABLE,null,initialValues);
        //   return db.updateWithOnConflict(DATABASE_TABLE, initialValues, KEY_ID + "=" + rowId, null, SQLiteDatabase.CONFLICT_IGNORE);
        // db.upd

        // } catch (Exception e) {
        //    if ( e.getMessage().toString().equals("column Code is not unique (code 19)"))
        //        return -2;

        //  else
        //      return -3;
        //  }
        //   return 2;
    }
}