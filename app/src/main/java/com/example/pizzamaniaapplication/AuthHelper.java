package com.example.pizzamaniaapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.pizzamaniaapplication.database.PizzaManiaDBOpenHelper;
import com.example.pizzamaniaapplication.models.User;

import java.io.ByteArrayOutputStream;

public class AuthHelper {
    private PizzaManiaDBOpenHelper dbHelper;
    private Context context;

    public AuthHelper(Context context) {
        this.context = context;
        dbHelper = new PizzaManiaDBOpenHelper(context);
    }

    // Convert Bitmap → byte[]
    private byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Register user
    public boolean registerUser(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Bitmap defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile);
        byte[] defaultPhoto = bitmapToBytes(defaultBitmap);

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", password);
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("address", user.getAddress());
        values.put("profilePhoto", defaultPhoto);

        long result = db.insert("Users", null, values);
        db.close();
        return result != -1;
    }

    // Login check
    public User loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Users",
                null,
                "email=? AND password=?",
                new String[]{email, password},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("username")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address"))
            );

            SessionManager sessionManager = new SessionManager(context);
            sessionManager.saveLogin(user.getUserId(), user.getUsername());

            cursor.close();
            db.close();
            return user;
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }
}
