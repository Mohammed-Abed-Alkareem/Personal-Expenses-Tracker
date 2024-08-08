package com.example.personalexpensestracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private Context context;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String TABLE_CREATE_TYPE = "CREATE TABLE expenses_type (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "type TEXT UNIQUE)";
            sqLiteDatabase.execSQL(TABLE_CREATE_TYPE);

            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Food')");
            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Transport')");
            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Entertainment')");
            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Electricity')");
            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Water')");
            sqLiteDatabase.execSQL("INSERT INTO expenses_type (type) VALUES ('Rent')");

            String TABLE_CREATE_EXPENSES = "CREATE TABLE EXPENSES (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "AMOUNT REAL, " +
                    "NOTES TEXT, " +
                    "DATE TEXT, " +
                    "TIME TEXT, " +
                    "TYPE INTEGER, " +
                    "FOREIGN KEY (TYPE) REFERENCES expenses_type(id))";
            sqLiteDatabase.execSQL(TABLE_CREATE_EXPENSES);

        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage());
            Toast.makeText(context, "Error creating table: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS EXPENSES");
            db.execSQL("DROP TABLE IF EXISTS expenses_type");
            onCreate(db);
        }
    }

    public void addExpense(@NonNull Expenses expense) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("AMOUNT", expense.getAmount());
            values.put("NOTES", expense.getNotes());
            values.put("DATE", expense.getDate().format(DATE_FORMATTER));
            values.put("TIME", expense.getTime().format(TIME_FORMATTER));

            // Get the ID of the expense type
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM expenses_type WHERE type = ?", new String[]{expense.getType()});
            if (cursor != null && cursor.moveToFirst()) {
                int typeId = cursor.getInt(getColumnIndex(cursor, "id"));
                values.put("TYPE", typeId);
            }

            sqLiteDatabase.insertOrThrow("EXPENSES", null, values);

            System.out.println("type: " + expense.getType());
            System.out.println("amount: " + expense.getAmount());
            System.out.println("notes: " + expense.getNotes());
            System.out.println("date: " + expense.getDate().format(DATE_FORMATTER));
            System.out.println("time: " + expense.getTime().format(TIME_FORMATTER));

        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
            Toast.makeText(context, "Error inserting data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
            Toast.makeText(context, "Error inserting data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
    }

    public List<Expenses> getAllExpenses() {
        List<Expenses> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("EXPENSES", null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(getColumnIndex(cursor, "ID"));
                    double amount = cursor.getDouble(getColumnIndex(cursor, "AMOUNT"));
                    String notes = cursor.getString(getColumnIndex(cursor, "NOTES"));
                    LocalDate date = LocalDate.parse(cursor.getString(getColumnIndex(cursor, "DATE")), DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(cursor.getString(getColumnIndex(cursor, "TIME")), TIME_FORMATTER);

                    // Get the expense type
                    int typeId = cursor.getInt(getColumnIndex(cursor, "TYPE"));
                    Cursor typeCursor = db.rawQuery("SELECT type FROM expenses_type WHERE id = ?", new String[]{String.valueOf(typeId)});
                    String type = "";
                    if (typeCursor != null && typeCursor.moveToFirst()) {
                        type = typeCursor.getString(getColumnIndex(typeCursor, "type"));
                    }

                    Expenses expense = new Expenses(id, type, amount, notes);
                    expense.setDate(date);
                    expense.setTime(time);
                    expenseList.add(expense);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving data: " + e.getMessage());
            Toast.makeText(context, "Error retrieving data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return expenseList;
    }

    public Expenses getExpenseById(int expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Expenses expense = null;

        // Query to select the expense with the given ID
        String query = "SELECT * FROM expenses WHERE id = ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(expenseId)});

            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = getColumnIndex(cursor, "ID");
                int typeColumnIndex = getColumnIndex(cursor, "TYPE");
                int amountColumnIndex = getColumnIndex(cursor, "AMOUNT");
                int notesColumnIndex = getColumnIndex(cursor, "NOTES");
                int dateColumnIndex = getColumnIndex(cursor, "DATE");
                int timeColumnIndex = getColumnIndex(cursor, "TIME");

                    int id = cursor.getInt(idColumnIndex);
                    int typeId = cursor.getInt(typeColumnIndex);
                    double amount = cursor.getDouble(amountColumnIndex);
                    String notes = cursor.getString(notesColumnIndex);
                    LocalDate date = LocalDate.parse(cursor.getString(dateColumnIndex), DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(cursor.getString(timeColumnIndex), TIME_FORMATTER);

                    // Get the expense type from the expense_type table
                    String type = "";
                    Cursor typeCursor = db.rawQuery("SELECT type FROM expenses_type WHERE id = ?", new String[]{String.valueOf(typeId)});
                    if (typeCursor != null && typeCursor.moveToFirst()) {
                        type = typeCursor.getString(getColumnIndex(typeCursor, "type"));
                    }

                    // Create the Expenses object
                    expense = new Expenses(id, type, amount, notes);
                    expense.setDate(date);
                    expense.setTime(time);

            } else {
                Log.e(TAG, "Cursor is null or empty");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving expense by ID: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return expense;
    }

    private int getColumnIndex(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) {
            Log.e(TAG, "Column index not found for column: " + columnName);
        }
        return index;
    }

    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("EXPENSES", "ID" + " = ?", new String[]{String.valueOf(expenseId)});
        } catch (Exception e) {
            Log.e(TAG, "Error deleting expense: " + e.getMessage());
            Toast.makeText(context, "Error deleting expense: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public String[] get_types() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT type FROM expenses_type", null);
        String[] types = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            types[i] = cursor.getString(0);
            i++;
        }
        cursor.close();
        db.close();
        return types;
    }
}
