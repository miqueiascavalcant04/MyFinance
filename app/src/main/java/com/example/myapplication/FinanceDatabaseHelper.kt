package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FinanceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "finance.db"
        const val DATABASE_VERSION = 1

        const val EXPENSES_TABLE_NAME = "expenses"
        const val INCOMES_TABLE_NAME = "incomes"
        const val COLUMN_ID = "id"
        const val COLUMN_USER_ID = "user_id"  // Nova coluna para o ID do usuário
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_AMOUNT = "amount"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createExpensesTable = """
            CREATE TABLE $EXPENSES_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES users(id)
            );
        """
        val createIncomesTable = """
            CREATE TABLE $INCOMES_TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_ID INTEGER NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_AMOUNT REAL NOT NULL,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES users(id)
            );
        """
        db?.execSQL(createExpensesTable)
        db?.execSQL(createIncomesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropExpensesTable = "DROP TABLE IF EXISTS $EXPENSES_TABLE_NAME"
        val dropIncomesTable = "DROP TABLE IF EXISTS $INCOMES_TABLE_NAME"
        db?.execSQL(dropExpensesTable)
        db?.execSQL(dropIncomesTable)
        onCreate(db)
    }

    // Inserir despesa relacionada ao usuário
    fun insertExpense(userId: Int, category: String, amount: Float) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)  // Associa a despesa ao usuário
            put(COLUMN_CATEGORY, category)
            put(COLUMN_AMOUNT, amount)
        }
        db.insert(EXPENSES_TABLE_NAME, null, values)
        db.close()
    }

    // Inserir receita relacionada ao usuário
    fun insertIncome(userId: Int, category: String, amount: Float) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)  // Associa a receita ao usuário
            put(COLUMN_CATEGORY, category)
            put(COLUMN_AMOUNT, amount)
        }
        db.insert(INCOMES_TABLE_NAME, null, values)
        db.close()
    }

    // Recuperar as despesas totais de um usuário específico
    fun getTotalExpenses(userId: Int): Float {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_AMOUNT) FROM $EXPENSES_TABLE_NAME WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        cursor.moveToFirst()
        val total = cursor.getFloat(0)
        cursor.close()
        db.close()
        return total
    }

    // Recuperar as receitas totais de um usuário específico
    fun getTotalIncomes(userId: Int): Float {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_AMOUNT) FROM $INCOMES_TABLE_NAME WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        cursor.moveToFirst()
        val total = cursor.getFloat(0)
        cursor.close()
        db.close()
        return total
    }
}
