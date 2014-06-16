package net.sf.dvstar.kidsdialer.data;

import net.sf.dvstar.kidsdialer.utils.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDataProvider implements TableDefs {

	private Context mContext;
	private DBHelper dbHelper;
	private SQLiteDatabase databaseKD;

	public FavoriteDataProvider(Context context) {
		mContext = context;

		// создаем объект для создания и управления версиями БД
		dbHelper = new DBHelper(mContext);
		// подключаемся к БД
		databaseKD = dbHelper.getWritableDatabase();
	}

	public Cursor getFavoriteCursor() {
		// делаем запрос всех данных из таблицы mytable, получаем Cursor
		Cursor cursor = databaseKD.query(TABLE_FAVORITES, null, null, null, null, null,
				null);
		return cursor;
	}

	public void updatedFavorites(ContentValues cv) {
		String conactId = cv.getAsString(CONTACT_ID);
		long rowID = databaseKD.update(TABLE_FAVORITES, cv, CONTACT_ID+"=?", new String[]{conactId});
		Log.d("row updated, ID = " + rowID);
	}

	/**
	 * Add item to database
	 * @param cv item
	 */
	public void addFavorites(ContentValues cv) {
		// вставляем запись и получаем ее ID
		String conactId = cv.getAsString(CONTACT_ID);
		Cursor cursor = databaseKD.query(TABLE_FAVORITES, null, CONTACT_ID+"="+conactId, null, null, null,null);
		if(!cursor.moveToFirst()) {
			long rowID = databaseKD.insert(TABLE_FAVORITES, null, cv);
			Log.d("row inserted, ID = " + rowID);
		}
	}

	public void delAllFavorites() {
		Log.d("--- Clear mytable: ---");
		// удаляем все записи
		int clearCount = databaseKD.delete("favorites", null, null);
		Log.d("deleted rows count = " + clearCount);
	}

	public void delFavorites(String id) {
		Log.d("--- Delete mytable: ---");
		int clearCount = databaseKD.delete("favorites", "id=?", new String[] { id });
		Log.d("deleted rows count = " + clearCount);
	}

	class DBHelper extends SQLiteOpenHelper {

		private static final String DB_FAVORITES_TABLE = "favorites";

		public DBHelper(Context context) {
			// конструктор суперкласса
			super(context, "kdDB", null, 3);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v("--- onCreate database ---");
			// создаем таблицу с полями
			db.execSQL("create table "+DB_FAVORITES_TABLE+" ("
					+ "id integer primary key autoincrement," 
					+ CONTACT_ID+" text,"
					+ CONTACT_NAME+" text," 
					+ CONTACT_PHONES+" text," 
					+ CONTACT_LAST_PHONE+" text," 
					+ CONTACT_IMAGE+" text,"
					+ CONTACT_RING+" text," 
					+ CONTACT_EMAILS+" text" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+DB_FAVORITES_TABLE);
		    onCreate(db);
		}
	}

}
