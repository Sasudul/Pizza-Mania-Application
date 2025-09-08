package com.example.pizzamaniaapplication.database;

import android.provider.BaseColumns;

public final class DBContract {

    private DBContract() {}

    // ================= Users =================
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COL_USER_ID = "userId";
        public static final String COL_USERNAME = "username";
        public static final String COL_NAME = "name";
        public static final String COL_EMAIL = "email";
        public static final String COL_PHONE = "phone";
        public static final String COL_ADDRESS = "address";
    }

    // ================= Staff =================
    public static class Staff implements BaseColumns {
        public static final String TABLE_NAME = "Staff";
        public static final String COL_STAFF_ID = "staffId";
        public static final String COL_NAME = "name";
        public static final String COL_EMAIL = "email";
        public static final String COL_PHONE = "phone";
        public static final String COL_POSITION = "position";
        public static final String COL_ADDRESS = "address";
        public static final String COL_GENDER = "gender";
        public static final String COL_DOB = "dob";
    }

    // ================= Branches =================
    public static class Branches implements BaseColumns {
        public static final String TABLE_NAME = "Branches";
        public static final String COL_BRANCH_ID = "branchId";
        public static final String COL_NAME = "name";
        public static final String COL_LOCATION = "location";
        public static final String COL_CONTACT = "contact";
    }

    // ================= Menu =================
    public static class Menu implements BaseColumns {
        public static final String TABLE_NAME = "Menu";
        public static final String COL_PIZZA_ID = "pizzaId";
        public static final String COL_NAME = "name";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_PRICE = "price";
        public static final String COL_IMAGE_URL = "imageUrl";
        public static final String COL_AVAILABLE = "available"; // 1 = available, 0 = not
        public static final String COL_CATEGORY = "category";
    }

    // ================= Orders =================
    public static class Orders implements BaseColumns {
        public static final String TABLE_NAME = "Orders";
        public static final String COL_ORDER_ID = "orderId";
        public static final String COL_USER_ID = "userId";
        public static final String COL_BRANCH_ID = "branchId";
        public static final String COL_TOTAL_PRICE = "totalPrice";
        public static final String COL_CURRENT_STATUS = "currentStatus";
        public static final String COL_ESTIMATED_DELIVERY = "estimatedDelivery";
    }

    // ================= OrderItems =================
    public static class OrderItems implements BaseColumns {
        public static final String TABLE_NAME = "OrderItems";
        public static final String COL_ORDER_ITEM_ID = "orderItemId";
        public static final String COL_ORDER_ID = "orderId";
        public static final String COL_PIZZA_ID = "pizzaId";
        public static final String COL_QUANTITY = "quantity";
        public static final String COL_PRICE = "price";
    }

    // ================= Cart =================
    public static class Cart implements BaseColumns {
        public static final String TABLE_NAME = "Cart";
        public static final String COL_CART_ID = "cartId";
        public static final String COL_USER_ID = "userId";
        public static final String COL_PIZZA_ID = "pizzaId";
        public static final String COL_QUANTITY = "quantity";
        public static final String COL_CUSTOMIZATIONS = "customizations";
    }
}
