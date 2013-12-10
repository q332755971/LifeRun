package nuist.wcw.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 配置文件操作类 本类全部采用静态static方法，便于调用
 * 
 * @author 晨崴
 * 
 */
public class FileUtils {

	/**
	 * 在preference文件中存入体重
	 * 
	 * @param activity
	 *            activity的引用
	 * @param heavy
	 *            体重值
	 */
	public static void setHeavy(Activity activity, int heavy) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("heavy", heavy);
		editor.commit();
	}

	/**
	 * 设置第一次执行标记
	 * 
	 * @param activity
	 *            activity的引用
	 * @param isFirstRun
	 *            第一次执行标记
	 */
	public static void setIsFirstRun(Activity activity, boolean isFirstRun) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstRun", isFirstRun);
		editor.commit();
	}

	/**
	 * 设置总里程
	 * 
	 * @param activity
	 *            activity的引用
	 * @param miles
	 *            总里程
	 */
	public static void setAllMiles(Activity activity, int miles) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("allMiles", miles);
		editor.commit();
	}

	/**
	 * 设置总消耗热量
	 * 
	 * @param activity
	 *            activity的引用
	 * @param heat
	 *            总消耗热量
	 */
	public static void setAllHeat(Activity activity, int heat) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("allHeat", heat);
		editor.commit();
	}

	/**
	 * 设置消耗总时间
	 * 
	 * @param activity
	 *            activity的引用
	 * @param time
	 *            消耗总时间
	 */
	public static void setAllTime(Activity activity, int time) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt("allTime", time);
		editor.commit();
	}

	/**
	 * 设置彩蛋标记
	 * 
	 * @param activity
	 *            activity的引用
	 * @param caidan
	 *            彩蛋标记
	 */
	public static void setCaidan(Activity activity, boolean caidan) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("Caidan", caidan);
		editor.commit();
	}

	/**
	 * 获取体重
	 * 
	 * @param activity
	 *            Activity的引用
	 * @return 体重
	 */
	public static int getHeavy(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getInt("heavy", 0);
	}

	/**
	 * 获取第一次执行标记
	 * 
	 * @param activity
	 *            activity的引用
	 * @return 第一次执行标记
	 */
	public static boolean getIsFirstRun(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getBoolean("isFirstRun", true);
	}

	/**
	 * 获取总里程
	 * 
	 * @param activity
	 *            activity的引用
	 * @return总里程
	 */
	public static int getAllMiles(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getInt("allMiles", 0);
	}

	public static int getAllHeat(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getInt("allHeat", 0);
	}

	/**
	 * 获取总消耗时间
	 * 
	 * @param activity
	 *            activity的引用
	 * @return 总消耗时间
	 */
	public static int getAllTime(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getInt("allTime", 0);
	}

	/**
	 * 获取彩蛋标记
	 * 
	 * @param activity
	 *            activity的引用
	 * @return彩蛋标记
	 */
	public static boolean getCaidan(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences("preference",
				Context.MODE_PRIVATE);
		return sp.getBoolean("Caidan", false);
	}
}
