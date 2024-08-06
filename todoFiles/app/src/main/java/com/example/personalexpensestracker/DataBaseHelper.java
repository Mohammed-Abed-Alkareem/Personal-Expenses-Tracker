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

    public static final String TABLE_EXPENSES = "EXPENSES";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_NOTES = "NOTES";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_TIME = "TIME";

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String TABLE_CREATE = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_NOTES + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_TIME + " TEXT)";
            sqLiteDatabase.execSQL(TABLE_CREATE);
        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage());
            Toast.makeText(context, "Error creating table: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
            onCreate(db);
        }
    }

    public void addExpense(@NonNull Expenses expense) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TYPE, expense.getType());
            values.put(COLUMN_AMOUNT, expense.getAmount());
            values.put(COLUMN_NOTES, expense.getNotes());
            values.put(COLUMN_DATE, expense.getDate().format(DATE_FORMATTER));
            values.put(COLUMN_TIME, expense.getTime().format(TIME_FORMATTER));
            sqLiteDatabase.insertOrThrow(TABLE_EXPENSES, null, values);

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
            cursor = db.query(TABLE_EXPENSES, null, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                    double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                    String notes = cursor.getString(cursor.getColumnIndex(COLUMN_NOTES));
                    LocalDate date = LocalDate.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)), DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)), TIME_FORMATTER);

                    Expenses expense = new Expenses(type, amount, notes);
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
}
