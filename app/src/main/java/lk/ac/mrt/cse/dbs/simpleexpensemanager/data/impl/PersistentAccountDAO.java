package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    public PersistentAccountDAO(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {

        Cursor curAcc = db.rawQuery("SELECT account_number FROM account", null);

        List<String> accountNumbers = new ArrayList<String>();

        if (curAcc.moveToFirst()) {
            do {
                accountNumbers.add(curAcc.getString(curAcc.getColumnIndex("account_number")));
            } while (curAcc.moveToNext());
        }

        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor curAcc = db.rawQuery("SELECT * FROM account", null);

        List<Account> accounts = new ArrayList<Account>();

        if (curAcc.moveToFirst()) {
            do {
                Account acc = new Account(curAcc.getString(curAcc.getColumnIndex("account_number")),
                        curAcc.getString(curAcc.getColumnIndex("bank")),
                        curAcc.getString(curAcc.getColumnIndex("holder")),
                        curAcc.getDouble(curAcc.getColumnIndex("balance")));
                accounts.add(acc);
            } while (curAcc.moveToNext());
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor curAcc = db.rawQuery("SELECT * FROM account WHERE account_number = " + accountNo, null);
        Account acc = null;

        if (curAcc.moveToFirst()) {
            do {
                acc = new Account(curAcc.getString(curAcc.getColumnIndex("account_number")),
                        curAcc.getString(curAcc.getColumnIndex("bank")),
                        curAcc.getString(curAcc.getColumnIndex("holder")),
                        curAcc.getDouble(curAcc.getColumnIndex("balance")));
            } while (curAcc.moveToNext());
        }
    return acc;
    }

    @Override
    public void addAccount(Account account) {
        String prepStmt = "INSERT INTO account (account_number, bank, holder, balance) VALUES (?, ?, ?, ?)";
        SQLiteStatement sqlStmt = db.compileStatement(prepStmt);

        sqlStmt.bindString(1, account.getAccountNo());
        sqlStmt.bindString(2, account.getBankName());
        sqlStmt.bindString(3, account.getAccountHolderName());
        sqlStmt.bindDouble(4, account.getBalance());

        sqlStmt.executeInsert();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String prepStmt = "DELETE FROM account WHERE account_number = ?";
        SQLiteStatement sqlStmt = db.compileStatement(prepStmt);

        sqlStmt.bindString(1, accountNo);

        sqlStmt.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String prepStmt = "UPDATE account SET balance = balance + ?";
        SQLiteStatement sqlStmt = db.compileStatement(prepStmt);

        if (expenseType == ExpenseType.EXPENSE) {
            sqlStmt.bindDouble(1, -amount);
        } else {
            sqlStmt.bindDouble(1, amount);
        }

        sqlStmt.executeUpdateDelete();
    }

}
