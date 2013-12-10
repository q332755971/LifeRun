package nuist.wcw.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nuist.wcw.Activity.R;
import nuist.wcw.model.History;
import nuist.wcw.model.MySQLiteHelper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 历史记录控制类
 * @author 晨崴
 *
 */
public class HistoryController {
	private Activity activity; // activity的引用
	private ListView listView; // 视图
	private MySQLiteHelper sqlHelper;
	private SimpleAdapter adapter;

	/**
	 * 构造函数，引用Activity
	 * @param activity
	 */
	public HistoryController(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 开始方法，HistoryActivity的执行
	 */
	public void start() {
		this.findViews();	//通过id找到组件
		this.initParams();	//初始化一些控件参数
		this.setAdapters();	//设置适配器
	}

	/**
	 * 获取组件
	 * listView
	 */
	private void findViews() {
		this.listView = (ListView) this.activity.findViewById(R.id.history);
	}

	/**
	 * 初始化组件参数
	 * sqlHelper，adapter
	 */
	private void initParams() {
		this.sqlHelper = new MySQLiteHelper(activity, "history.db", null, 1);
		this.initAdapter();	//由于adapter初始化过于复杂，单独写一个方法
	}

	/**
	 * 初始化adapter组件
	 */
	@SuppressLint("SimpleDateFormat")
	private void initAdapter() {
		List<History> histories = this.sqlHelper.queryAll();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Iterator<History> iter = histories.iterator();
		while (iter.hasNext()) {
			History history = iter.next();
			Map<String, String> map = new HashMap<String, String>();
			String date = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss")
					.format(new Date(history.getDate()));// 使用SimpleDateFormat格式化日期
			map.put("date", date);
			map.put("duration", "跑步耗时:" + history.getDuration() + "秒");
			map.put("distance", "跑步距离:" + history.getDistance() + "米");
			map.put("heat", "消耗热量:" + history.getHeat() + "千卡");
			data.add(map);
		}
		this.adapter = new SimpleAdapter(activity, data,
				R.layout.adapter_simple_history, new String[] { "date",
						"duration", "distance", "heat" }, new int[] {
						R.id.date, R.id.duration, R.id.distance, R.id.heat });
	}

	/**
	 * 设置适配器
	 * listView
	 */
	private void setAdapters() {
		this.listView.setAdapter(adapter);
	}

}
