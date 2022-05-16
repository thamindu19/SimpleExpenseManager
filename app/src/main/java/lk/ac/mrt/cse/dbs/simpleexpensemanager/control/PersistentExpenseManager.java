package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
        private Context context;

        public PersistentExpenseManager(Context context) {
            this.context = context;
            setup();
        }

        @Override
        public void setup() {
            SQLiteDatabase db = context.openOrCreateDatabase("190316H", context.MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS account (" +
                    "account_number VARCHAR PRIMARY KEY," +
                    "bank VARCHAR," +
                    "holder VARCHAR," +
                    "balance REAL" +
                    " );");

            db.execSQL("CREATE TABLE IF NOT EXISTS transactionRecord (" +
                    "transaction_number INTEGER PRIMARY KEY," +
                    "account_number VARCHAR," +
                    "date DATE," +
                    "type INT," +
                    "amount REAL," +
                    "FOREIGN KEY (account_number) REFERENCES account(account_number)" +
                    ");");

            TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(db);
            setTransactionsDAO(persistentTransactionDAO);

            AccountDAO persistentAccountDAO = new PersistentAccountDAO(db);
            setAccountsDAO(persistentAccountDAO);

            Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
            Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
            getAccountsDAO().addAccount(dummyAcct1);
            getAccountsDAO().addAccount(dummyAcct2);

        }
    }
