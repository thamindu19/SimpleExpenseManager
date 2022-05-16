package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class PersistentTransactionDAO implements TransactionDAO {
    private SQLiteDatabase db;

    public PersistentTransactionDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String prepStmt = "INSERT INTO transactionRecord (account_number, date, type, amount) VALUES (?, ?, ?, ?)";
        SQLiteStatement sqlStmt = db.compileStatement(prepStmt);

        sqlStmt.bindString(1, accountNo);
        sqlStmt.bindLong(2, date.getTime());
        sqlStmt.bindLong(3, (expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        sqlStmt.bindDouble(4, amount);

        sqlStmt.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor curTrs = db.rawQuery("SELECT * FROM transactionRecord", null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if (curTrs.moveToFirst()) {
            do {
                Transaction trs = new Transaction(new Date(curTrs.getLong(curTrs.getColumnIndex("date"))),
                        curTrs.getString(curTrs.getColumnIndex("account_number")),
                        (curTrs.getInt(curTrs.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        curTrs.getDouble(curTrs.getColumnIndex("amount")));
                transactions.add(trs);
            } while (curTrs.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor curTrs = db.rawQuery("SELECT * FROM transactionRecord LIMIT " + limit, null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if (curTrs.moveToFirst()) {
            do {
                Transaction trs = new Transaction(new Date(curTrs.getLong(curTrs.getColumnIndex("date"))),
                        curTrs.getString(curTrs.getColumnIndex("account_number")),
                        (curTrs.getInt(curTrs.getColumnIndex("type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        curTrs.getDouble(curTrs.getColumnIndex("amount")));
                transactions.add(trs);
            } while (curTrs.moveToNext());
        }
        return transactions;
    }

}
