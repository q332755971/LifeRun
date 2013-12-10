package nuist.wcw.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * 数据库操作类，用于本程序一切数据库操作。
 * 
 * @author 晨崴
 * 
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            context的引用
	 * @param name
	 *            数据库文件名
	 * @param factory
	 * @param version
	 *            数据库版本
	 */
	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/**
	 * 当第一次执行时执行
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists history(runDate long,duration Integer,distance Integer,heat Integer)");
	}

	/**
	 * 当数据库版本发生变化时执行
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * 将历史记录写入数据库
	 * 
	 * @param history
	 *            历史记录
	 */
	public void insertData(History history) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("runDate", history.getDate());
		values.put("duration", history.getDuration());
		values.put("distance", history.getDistance());
		values.put("heat", history.getHeat());
		db.insert("history", null, values);
		db.close();
	}

	/**
	 * 返回所有历史记录，暂时不支持查找
	 * 
	 * @return 历史记录的List集合
	 */
	public List<History> queryAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query("history", null, null, null, null, null, null);
		List<History> result = new ArrayList<History>();
		if (cursor.getCount() != 0) {
			int runDateIndex = cursor.getColumnIndex("runDate");
			int durationIndex = cursor.getColumnIndex("duration");
			int distanceIndex = cursor.getColumnIndex("distance");
			int heatIndex = cursor.getColumnIndex("heat");
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				History history = new History(cursor.getLong(runDateIndex),
						cursor.getInt(durationIndex),
						cursor.getInt(distanceIndex), cursor.getInt(heatIndex));
				result.add(history);
			}
		}
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * 清空数据库
	 * 
	 * @return
	 */
	public boolean clear() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("history", null, null);
		db.close();
		return true;
	}
}
