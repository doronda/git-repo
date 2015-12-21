package doronda.app.generator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by doronda on 15.12.2015.
 */
public class DBAdapter {

    private static final String DB_NAME = "generatorDb";
    private static final int DB_VERSION = 1;
    private static final String HISTORY_TABLE = "history";
    private static final String CACHE_TABLE = "cache";
    private static final String RANGE_TABLE = "range";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_RANGE = "range";
    public static final String COLUMN_THREAD_COUNT = "threads";
    public static final String COLUMN_CACHE = "cache";
    public static final String COLUMN_DURATION = "duration";

    private static DBAdapter dbAdapterInstance;

    private static final String DB_CREATE =
            "create table " + HISTORY_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TIME + " text, " +
                    COLUMN_THREAD_COUNT + " integer, " +
                    COLUMN_DURATION + " real, " +
                    COLUMN_RANGE + " text" +
                    ");";

    private static final String CACHE_CREATE =
            "create table " + CACHE_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_CACHE + " integer" +
                    ");";

    private static final String RANGE_CREATE =
            "create table " + RANGE_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_RANGE + " integer" +
                    ");";

    private static DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DBAdapter(Context ctx) {
        mDBHelper = DatabaseHelper.getInstance(ctx.getApplicationContext());
    }
    public static DBAdapter getInstance(Context ctx) {
        if (dbAdapterInstance == null) {
            dbAdapterInstance = new DBAdapter(ctx.getApplicationContext());
        }
        return dbAdapterInstance;
    }
    // DB open
    public DBAdapter open() throws SQLException {
        try {
            mDB = mDBHelper.getWritableDatabase();
        } catch (SQLException e) {
            throw e;
        }
        return this;
    }
    // DB close
    public void close() {
        mDBHelper.close();
    }
    // get range value
    public int getRange() {
        Cursor cursor = mDB.query(RANGE_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        int range = cursor.getInt(1);
        cursor.close();
        return range;
    }
    // get generation history
    public Cursor getHistory() {
        return mDB.query(HISTORY_TABLE, null, null, null, null, null, null);
    }
    // get value from cache
    public ArrayList<Integer> getCache(int value) {
        ArrayList<Integer> arr = new ArrayList<>();
        Log.d("1", "1");
        Cursor c = mDB.rawQuery("SELECT cache FROM cache WHERE cache <= ?", new String[]{"" + String.valueOf(value)});
        Log.d("1", "2");
        if (c.moveToFirst()) {
            do {
                arr.add(c.getInt(0));
            } while (c.moveToNext());
        }
        Log.d("1", "3");
        return arr;
    }
    public Cursor getCacheCur(int value) {
        ArrayList<Integer> arr = new ArrayList<>();
        Log.d("1", "1");
        Cursor c = mDB.rawQuery("SELECT _id, cache FROM cache WHERE cache <= ?", new String[]{"" + String.valueOf(value)});
        Log.d("1", "2");

        return c;
    }
    // get value from cache
    public int getMaxCacheId() {
        Cursor c = mDB.rawQuery("SELECT _id FROM cache WHERE MAX(cache)", new String[]{""});
        c.moveToFirst();
        int res = c.getInt(0);
        c.close();
        return res;
    }
    public ArrayList<Integer> getCache(int start, int end, int value) {
        ArrayList<Integer> arr = new ArrayList<>();
        Cursor c = mDB.rawQuery("SELECT cache FROM cache WHERE (_id between ? and ?) AND cache <= ?", new String[]{"" + String.valueOf(start), String.valueOf(end), String.valueOf(value)});

        if (c.moveToFirst()) {
            do {
                arr.add(c.getInt(0));
            } while (c.moveToNext());
        }

        return arr;
    }
    // get threads value for range from history
    public HashSet<Integer> getThreads(int range) {
        HashSet<Integer> arr = new HashSet<>();
        Cursor c = mDB.rawQuery("SELECT threads FROM history WHERE range = ?", new String[]{"" + String.valueOf(range)});

        if (c.moveToFirst()) {
            do {
                arr.add(c.getInt(0));
            } while (c.moveToNext());
        }

        return arr;
    }
    // get threads value for range from history
    public  List<PointValue> getDataForChart(int range) {
        HashSet<Integer> arr = new HashSet<>();
        TreeMap<Integer, Float> three = new TreeMap<>();
        List<PointValue> values = new ArrayList<PointValue>();
        Cursor c = mDB.rawQuery("SELECT threads, duration FROM history WHERE range = ?", new String[]{"" + String.valueOf(range)});

        if (c.moveToFirst()) {
            do {
                if(!arr.contains(c.getInt(0))){
                    arr.add(c.getInt(0));
                    if(c.getFloat(1)!=0)
                       three.put(c.getInt(0), c.getFloat(1));
                       values.add(new PointValue(c.getInt(0), c.getFloat(1)));
                }

            } while (c.moveToNext());
        }

        return values;
    }
    // get Max duration
    public  float getMaxDuration(int range) {
        Cursor c = mDB.rawQuery("SELECT MAX(duration) FROM history WHERE range = ?", new String[]{"" + String.valueOf(range)});
        c.moveToFirst();
        float res = c.getFloat(0);
        c.close();
        return res;
    }
    // get Max number of threads
    public  int getMaxNumberOfThreads(int range) {
        Cursor c = mDB.rawQuery("SELECT MAX(threads) FROM history WHERE range = ?", new String[]{"" + String.valueOf(range)});
        c.moveToFirst();
        int res = c.getInt(0);
        c.close();
        return res;
    }
    // update range value
    public void updateRange(int range) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RANGE, range);
        mDB.update(RANGE_TABLE, cv, "_id = ?", new String[]{"" + "1"});
    }
    // add history item
    public void insertHistoryItem(String time, String range, int threads, float duration) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_RANGE, range);
        cv.put(COLUMN_THREAD_COUNT, threads);
        cv.put(COLUMN_DURATION, duration);
        mDB.insert(HISTORY_TABLE, null, cv);
    }
    // save  primes to cache
    public ArrayList<Long> insertCache(ArrayList<Integer> arr)
    {
        deleteCache();
        ArrayList<Long> ids = new ArrayList();
        String sql = "INSERT INTO "+CACHE_TABLE+"("+"cache"+") values(?)";
        mDB.beginTransaction();
        try {
            SQLiteStatement stmt = mDB.compileStatement(sql);
            for(Integer  i: arr) {
                stmt.bindLong(1, i);
                ids.add(stmt.executeInsert());
            }
            stmt.clearBindings();
            mDB.setTransactionSuccessful();
        } finally {
            mDB.endTransaction();
        }
        return ids;
    }
    // delete cache value
    public void deleteCache() {
        mDB.execSQL("delete from " + CACHE_TABLE);
    }
    // create and manage DB
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static DatabaseHelper dbHelperInstance;

        public static synchronized  DatabaseHelper getInstance(Context context) {
            if (dbHelperInstance == null) {
                dbHelperInstance = new DatabaseHelper(context);
            }
            return dbHelperInstance;
        }
        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            db.execSQL(RANGE_CREATE);
            db.execSQL(CACHE_CREATE);
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_RANGE, "2");
            db.insert(RANGE_TABLE, null, cv);
            cv = new ContentValues();
            cv.put(COLUMN_CACHE, "2");
            db.insert(CACHE_TABLE, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}