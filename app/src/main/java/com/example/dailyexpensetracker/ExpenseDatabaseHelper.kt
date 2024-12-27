package com.example.dailyexpensetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ExpenseDatabaseHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DailyExpense_DB"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "expenses"
        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DATE = "date"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(database: SQLiteDatabase) {
        val createTable = """ 
            CREATE  TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_AMOUNT REAL NOT NULL,
            $COLUMN_CATEGORY TEXT NOT NULL,
            $COLUMN_DATE TEXT NOT NULL,
            $COLUMN_DESCRIPTION TEXT
            )
            """
        database.execSQL(createTable)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    fun addExpense (expenseDataModel: ExpenseDataModel): Long {
        val sqliteDb = writableDatabase
        val contentValue = ContentValues().apply {
            put(COLUMN_AMOUNT,expenseDataModel.amount)
            put(COLUMN_CATEGORY,expenseDataModel.category)
            put(COLUMN_DATE,expenseDataModel.date)
            put(COLUMN_DESCRIPTION,expenseDataModel.description)
        }
        return sqliteDb.insert(TABLE_NAME, null,contentValue)
    }

    fun getAllExpense():List<ExpenseDataModel>{
        val sqlite = readableDatabase
        val curser = sqlite.query(TABLE_NAME,null,null,null,null,null,"$COLUMN_DATE DESC")
        val expense = mutableListOf<ExpenseDataModel>()
        while (curser.moveToNext()){
            expense.add(ExpenseDataModel(
                id = curser.getInt(curser.getColumnIndexOrThrow(COLUMN_ID)),
                amount = curser.getDouble(curser.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                category = curser.getString(curser.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                date = curser.getString(curser.getColumnIndexOrThrow(COLUMN_DATE)),
                description = curser.getString(curser.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            ))
        }
        curser.close()
        return expense
    }

    fun updateExpense(expenseDataModel: ExpenseDataModel):Int{
        val sqLiteDatabase = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT,expenseDataModel.amount)
            put(COLUMN_CATEGORY,expenseDataModel.category)
            put(COLUMN_DATE,expenseDataModel.date)
            put(COLUMN_DESCRIPTION,expenseDataModel.description)
        }
        return sqLiteDatabase.update(TABLE_NAME,values,"id = ?", arrayOf(expenseDataModel.id.toString()))
    }

    fun deleteExpense(expenseID:Int): Int{
        val sqLiteDatabase = this.writableDatabase
        return sqLiteDatabase.delete(TABLE_NAME,"id = ?", arrayOf(expenseID.toString()))
    }


    fun getExpenseById(expenseId: Int): ExpenseDataModel? {
        val db = this.readableDatabase
        val cursor = db.query(
            "expenses",
            arrayOf("id", "amount", "category", "date", "description"),
            "id = ?",
            arrayOf(expenseId.toString()),
            null,
            null,
            null
        )

        return if (cursor != null && cursor.moveToFirst()) {
            val expense = ExpenseDataModel(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount")),
                category = cursor.getString(cursor.getColumnIndexOrThrow("category")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date")),
                description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            )
            cursor.close()
            expense
        } else {
            cursor?.close()
            null
        }
    }


}