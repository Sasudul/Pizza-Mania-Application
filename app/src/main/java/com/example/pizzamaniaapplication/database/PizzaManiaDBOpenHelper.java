package com.example.pizzamaniaapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pizzamaniaapplication.R;
import com.example.pizzamaniaapplication.models.CartItem;
import com.example.pizzamaniaapplication.models.Order;
import com.example.pizzamaniaapplication.models.Pizza;
import com.example.pizzamaniaapplication.models.Staff;

import java.util.ArrayList;
import java.util.List;

public class PizzaManiaDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PizzaManiaDB";
    private static final int DB_VERSION = 5;
    // Table Names
    // Table Names
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_STAFF = "Staff";
    public static final String TABLE_MENU = "Menu";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_ORDERS = "Orders";

    // User Table Columns
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_PHOTO = "profilePhoto";

    // Staff Table Columns
    public static final String COLUMN_STAFF_ID = "staffId";
    public static final String COLUMN_STAFF_NAME = "name";
    public static final String COLUMN_STAFF_EMAIL = "email";
    public static final String COLUMN_STAFF_PHONE = "phone";
    public static final String COLUMN_STAFF_ADDRESS = "address";
    public static final String COLUMN_STAFF_PASSWORD = "password";
    public static final String COLUMN_STAFF_POSITION = "position";
    public static final String COLUMN_STAFF_GENDER = "gender";
    public static final String COLUMN_STAFF_DOB = "dob";
    public static final String COLUMN_STAFF_PHOTO = "profilePhoto";

    // Menu Table Columns
    public static final String COLUMN_MENU_PIZZA_ID = "pizzaId";
    public static final String COLUMN_MENU_NAME = "name";
    public static final String COLUMN_MENU_DESCRIPTION = "description";
    public static final String COLUMN_MENU_PRICE = "price";
    public static final String COLUMN_MENU_IMAGE_URL = "imageUrl";
    public static final String COLUMN_MENU_AVAILABLE = "available";
    public static final String COLUMN_MENU_CATEGORY = "category";
    public static final String COLUMN_MENU_BRANCH_ID = "branchId";

    // Cart Table Columns
    public static final String COLUMN_CART_ID = "cartId";
    public static final String COLUMN_CART_USER_ID = "userId";
    public static final String COLUMN_CART_PIZZA_ID = "pizzaId";
    public static final String COLUMN_CART_QUANTITY = "quantity";
    public static final String COLUMN_CART_CUSTOMIZATIONS = "customizations";

    // Orders Table Columns
    public static final String COLUMN_ORDER_ID = "orderId";
    public static final String COLUMN_ORDER_USER_ID = "userId";
    public static final String COLUMN_ORDER_BRANCH_ID = "branchId";
    public static final String COLUMN_ORDER_ITEMS = "items";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "totalPrice";
    public static final String COLUMN_ORDER_STATUS = "currentStatus";
    public static final String COLUMN_ORDER_ESTIMATED_DELIVERY = "estimatedDelivery";

    public PizzaManiaDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users Table
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_USERNAME + " TEXT UNIQUE," +
                COLUMN_USER_PASSWORD + " TEXT," +
                COLUMN_USER_NAME + " TEXT," +
                COLUMN_USER_EMAIL + " TEXT UNIQUE," +
                COLUMN_USER_PHONE + " TEXT," +
                COLUMN_USER_ADDRESS + " TEXT," +
                COLUMN_USER_PHOTO + " BLOB)"); // ✅ added BLOB

        // Staff Table
        db.execSQL("CREATE TABLE " + TABLE_STAFF + " (" +
                COLUMN_STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_STAFF_NAME + " TEXT," +
                COLUMN_STAFF_EMAIL + " TEXT," +
                COLUMN_STAFF_PHONE + " TEXT," +
                COLUMN_STAFF_ADDRESS + " TEXT," +
                COLUMN_STAFF_PASSWORD + " TEXT," +
                COLUMN_STAFF_POSITION + " TEXT," +
                COLUMN_STAFF_GENDER + " TEXT," +
                COLUMN_STAFF_DOB + " TEXT," +
                COLUMN_STAFF_PHOTO + " BLOB)"); // ✅ added BLOB

        // Branches Table
        db.execSQL("CREATE TABLE Branches (" +
                "branchId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "location TEXT," +
                "contact TEXT," +
                "latitude REAL," +
                "longitude REAL)");

        // Menu Table
        db.execSQL("CREATE TABLE Menu (" +
                "pizzaId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "description TEXT," +
                "price REAL," +
                "imageUrl TEXT," +
                "available INTEGER," +
                "category TEXT," +
                "branchId INTEGER," +
                "FOREIGN KEY(branchId) REFERENCES Branches(branchId))");

        // Orders Table
        db.execSQL("CREATE TABLE Orders (" +
                "orderId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "branchId INTEGER," +
                "items TEXT," +
                "totalPrice REAL," +
                "currentStatus TEXT," +
                "estimatedDelivery TEXT," +
                "FOREIGN KEY(userId) REFERENCES Users(userId)," +
                "FOREIGN KEY(branchId) REFERENCES Branches(branchId))");

        // Cart Table
        db.execSQL("CREATE TABLE Cart (" +
                "cartId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "pizzaId INTEGER," +
                "quantity INTEGER," +
                "customizations TEXT," +
                "FOREIGN KEY(userId) REFERENCES Users(userId)," +
                "FOREIGN KEY(pizzaId) REFERENCES Menu(pizzaId))");

        // Insert sample data
        insertUser(db, "Sasa", "1234", "Sasudul Nirodhana", "sasuduln@gmail.com", "0740629020", "4M52+C75 Black Forest Estate, Pussellawa", null);
        insertStaff(db, "Admin", "admin@email.com", "0712345678", "Colombo", "admin123", "Manager", "Male", "2006-09-05", null);

        insertBranch(db, "Colombo Branch", "Colombo 07", "0112456789", 6.9271, 79.8612);
        insertBranch(db, "Galle Branch", "Galle", "0112157851", 6.0535, 80.2210);
        insertBranch(db, "Matara Branch", "Matara", "0112456789", 5.9549, 80.5549);
        insertBranch(db, "Kandy Branch", "Kandy", "0112876508", 7.284440, 80.637466);


// Inside onCreate() or when seeding DB
        insertMenu(db, "Four Cheese Pizza", "Delicious blend of mozzarella, cheddar, parmesan, and gorgonzola",
                1800.00, String.valueOf(R.drawable.italian_4_cheese_pizza), 1, "Veg", 1);

        insertMenu(db, "Anchovies Pizza", "Cheesy pizza topped with anchovies and herbs",
                1600.00, String.valueOf(R.drawable.italian_anchovies_pizza), 1, "Non-Veg", 2);

        insertMenu(db, "Chicken Pizza", "Juicy chicken chunks with mozzarella cheese",
                1700.00, String.valueOf(R.drawable.italian_chicken_pizza), 1, "Non-Veg", 1);

        insertMenu(db, "Margherita Pizza", "Classic pizza with mozzarella, tomato, and basil",
                1400.00, String.valueOf(R.drawable.italian_margerita_pizza), 1, "Veg", 1);

        insertMenu(db, "Pepperoni Pizza", "Cheesy pizza with spicy pepperoni",
                1500.00, String.valueOf(R.drawable.italian_pepperoni_pizza), 1, "Non-Veg", 1);

        insertMenu(db, "Prawn Pizza", "Fresh prawns with creamy cheese topping",
                1900.00, String.valueOf(R.drawable.italian_prawn_pizza), 1, "Seafood", 1);

        insertMenu(db, "Seafood Pizza", "Combination of prawns, calamari, and fish chunks",
                2000.00, String.valueOf(R.drawable.italian_seafood_pizza), 1, "Seafood", 1);

        insertMenu(db, "Spicy Chicken Pizza", "Hot and spicy chicken with chili flakes",
                1750.00, String.valueOf(R.drawable.italian_spicy_chicken_pizza), 1, "Non-Veg", 1);

        insertMenu(db, "Garlic Pizza", "Cheesy garlic-flavored pizza",
                1300.00, String.valueOf(R.drawable.italian_garlic_pizza), 1, "Veg", 1);

        insertMenu(db, "Devilled Chicken Pizza", "Sri Lankan style devilled chicken with onions and capsicum",
                1850.00, String.valueOf(R.drawable.italian_devilled_chicken_pizza), 1, "Non-Veg", 1);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Menu");
        db.execSQL("DROP TABLE IF EXISTS Branches");
        db.execSQL("DROP TABLE IF EXISTS Staff");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    // Insert User with photo
    public void insertUser(SQLiteDatabase db, String username, String password,
                           String name, String email, String phone, String address, byte[] profilePhoto) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_ADDRESS, address);
        if (profilePhoto != null) {
            values.put(COLUMN_USER_PHOTO, profilePhoto); // ✅ photo
        }
        db.insert(TABLE_USERS, null, values);
    }


    // Insert Staff with photo
    public void insertStaff(SQLiteDatabase db, String name, String email, String phone,
                            String address, String password, String position,
                            String gender, String dob, byte[] profilePhoto) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAFF_NAME, name);
        values.put(COLUMN_STAFF_EMAIL, email);
        values.put(COLUMN_STAFF_PHONE, phone);
        values.put(COLUMN_STAFF_ADDRESS, address);
        values.put(COLUMN_STAFF_PASSWORD, password);
        values.put(COLUMN_STAFF_POSITION, position);
        values.put(COLUMN_STAFF_GENDER, gender);
        values.put(COLUMN_STAFF_DOB, dob);
        if (profilePhoto != null) {
            values.put(COLUMN_STAFF_PHOTO, profilePhoto); // ✅ photo
        }
        db.insert(TABLE_STAFF, null, values);
    }
    // Inside PizzaManiaDBOpenHelper.java

    // Method to get all staff members
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_STAFF_ID + ", " + COLUMN_STAFF_NAME + ", " + COLUMN_STAFF_EMAIL + ", " + COLUMN_STAFF_PHONE + " FROM " + TABLE_STAFF, null);

        if (cursor.moveToFirst()) {
            do {
                int staffId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STAFF_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAFF_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAFF_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STAFF_PHONE));

                Staff staff = new Staff(staffId, name, email, phone);
                staffList.add(staff);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return staffList;
    }

    // Method to delete a staff member
    public boolean deleteStaff(int staffId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STAFF, COLUMN_STAFF_ID + " = ?", new String[]{String.valueOf(staffId)});
        db.close();
        return result > 0;
    }

    // Insert Branch
    public void insertBranch(SQLiteDatabase db, String name, String location, String contact, double lat, double lon) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("location", location);
        values.put("contact", contact);
        values.put("latitude", lat);
        values.put("longitude", lon);
        db.insert("Branches", null, values);
    }

    public int getNearestBranchId(double userLat, double userLon) {
        int branchId = 1; // default Colombo
        double minDistance = Double.MAX_VALUE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT branchId, latitude, longitude FROM Branches", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                double lat = cursor.getDouble(1);
                double lon = cursor.getDouble(2);

                double distance = Math.sqrt(Math.pow(userLat - lat, 2) + Math.pow(userLon - lon, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    branchId = id;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return branchId;
    }


    // Insert Menu Item
    public void insertMenu(SQLiteDatabase db, String name, String description,
                           double price, String imageUrl, int available, String category, int branchId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("imageUrl", imageUrl);
        values.put("available", available);
        values.put("category", category);
        values.put("branchId", branchId);
        db.insert("Menu", null, values);

    }

    public String getUserAddress(SQLiteDatabase db, int userId) {
        String address = null;
        Cursor cursor = db.rawQuery("SELECT address FROM Users WHERE userId=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            address = cursor.getString(0);
        }
        cursor.close();
        return address;
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.cartId, m.name, m.price, c.quantity, m.imageUrl " +
                "FROM Cart c INNER JOIN Menu m ON c.pizzaId = m.pizzaId " +
                "WHERE c.userId=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(0);
                String pizzaName = cursor.getString(1);
                double price = cursor.getDouble(2);
                int quantity = cursor.getInt(3);
                String imageUrl = cursor.getString(4);

                cartItems.add(new CartItem(cartId, pizzaName, price, quantity, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }
    // Inside PizzaManiaDBOpenHelper.java
    public void placeOrder(int userId, double totalPrice, int branchId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Get cart items to store in the order
        String cartItemsQuery = "SELECT m.name, c.quantity FROM Cart c JOIN Menu m ON c.pizzaId = m.pizzaId WHERE c.userId = ?";
        Cursor cursor = db.rawQuery(cartItemsQuery, new String[]{String.valueOf(userId)});

        StringBuilder itemsString = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(0);
                int quantity = cursor.getInt(1);
                itemsString.append(pizzaName).append(" (x").append(quantity).append("), ");
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Remove the trailing comma and space
        String items = itemsString.length() > 0 ? itemsString.substring(0, itemsString.length() - 2) : "";

        // 2. Insert into Orders table
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("branchId", branchId);
        values.put("items", items);
        values.put("totalPrice", totalPrice);
        values.put("currentStatus", "Pending");
        values.put("estimatedDelivery", "30-45 minutes");
        db.insert("Orders", null, values);

        db.delete("Cart", "userId = ?", new String[]{String.valueOf(userId)});

        db.close();
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT orderId, userId, branchId, items, totalPrice, currentStatus, estimatedDelivery FROM Orders", null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int branchId = cursor.getInt(2);
                String items = cursor.getString(3);
                double totalPrice = cursor.getDouble(4);
                String status = cursor.getString(5);
                String eta = cursor.getString(6);

                Order order = new Order(orderId, userId, branchId, items, totalPrice, status,  eta);
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    public void insertMenuItem(String name, String description, double price, String imagePath, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("imageUrl", imagePath);
        values.put("category", category);
        values.put("available", 1);
        values.put("branchId", 1);
        db.insert("Menu", null, values);
        db.close();
    }

    public Pizza getPizzaById(int pizzaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Menu WHERE pizzaId = ?", new String[]{String.valueOf(pizzaId)});
        Pizza pizza = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            pizza = new Pizza(pizzaId, name, description, price, imageUrl);
        }
        cursor.close();
        db.close();
        return pizza;
    }


    public void addToCart(int userId, int pizzaId, int quantity, String customizations) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("pizzaId", pizzaId);
        values.put("quantity", quantity);
        values.put("customizations", customizations);
        db.insert("Cart", null, values);
        db.close();
    }

    public List<Order> getOrdersForUser(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT orderId, userId, branchId, items, totalPrice, currentStatus, estimatedDelivery FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                int orderUserId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_ID));
                int branchId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_BRANCH_ID));
                String items = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS));
                double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_PRICE));
                String currentStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));
                String estimatedDelivery = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ESTIMATED_DELIVERY));

                Order order = new Order(orderId, orderUserId, branchId, items, totalPrice, currentStatus, estimatedDelivery);
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

}
