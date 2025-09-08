package com.example.pizzamaniaapplication.database;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PizzaManiaDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PizzaManiaDB";
    private static final int DB_VERSION = 1;

    public PizzaManiaDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Users Table
        db.execSQL("CREATE TABLE Users (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "phone TEXT," +
                "address TEXT)");

        // Staff Table
        db.execSQL("CREATE TABLE Staff (" +
                "staffId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT," +
                "phone TEXT," +
                "address TEXT," +
                "position TEXT," +
                "gender TEXT," +
                "dob TEXT)");

        // Branches Table
        db.execSQL("CREATE TABLE Branches (" +
                "branchId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "location TEXT," +
                "contact TEXT)");

        // Menu Table
        db.execSQL("CREATE TABLE Menu (" +
                "pizzaId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "price REAL," +
                "imageUrl TEXT," +       // store URI/path of image
                "available INTEGER," +   // 1 = available, 0 = not
                "category TEXT)");

        // Orders Table
        db.execSQL("CREATE TABLE Orders (" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "branchId INTEGER," +
                "items TEXT," +                  // could store JSON string
                "totalPrice REAL," +
                "currentStatus TEXT," +
                "estimatedDelivery TEXT," +
                "FOREIGN KEY(userId) REFERENCES Users(userId)," +
                "FOREIGN KEY(branchId) REFERENCES Branches(branchId))");

        db.execSQL("CREATE TABLE Cart (" +
                "cartId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +              // Who owns the cart
                "pizzaId INTEGER," +             // Which pizza
                "quantity INTEGER," +            // How many of this item
                "customizations TEXT," +         // (Optional) toppings, crust type, etc. as JSON/text
                "FOREIGN KEY(userId) REFERENCES Users(userId)," +
                "FOREIGN KEY(pizzaId) REFERENCES Menu(pizzaId))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now: drop & recreate
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Menu");
        db.execSQL("DROP TABLE IF EXISTS Branches");
        db.execSQL("DROP TABLE IF EXISTS Staff");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }
}