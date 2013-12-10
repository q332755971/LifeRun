package nuist.wcw.controller;

import java.util.Date;

import nuist.wcw.Activity.R;
import nuist.wcw.model.FileUtils;
import nuist.wcw.model.MySQLiteHelper;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设置界面的控制模块
 * 
 * @author 晨崴
 * 
 */
public class SettingsController {
	private Activity activity; // activity的引用
	private TextView allMile, allHeat, allTime; // 总里程，总消耗热量，总消耗时间的显示组件
	private RelativeLayout setHeavy, clearHistory, setHeavySeekBarLayout; // 设置体重，清空历史记录，以及体重设置条
	private SeekBar setHeavySeekBar; // 体重设置条
	private TextView heavyTextView; // 显示体重的标签
	private MySQLiteHelper sqlHelper; // 数据库操作类

	public SettingsController(Activity activity) {
		this.activity = activity;
	}

	public void start() {
		this.findViews(); // 获取组件id
		this.initParams(); // 初始化变量
		this.initStatics(); // 设置统计数据
		this.setListeners(); // 设置监听

	}

	/**
	 * 获取各控件id
	 */
	private void findViews() {
		this.allMile = (TextView) this.activity.findViewById(R.id.allMile);
		this.allHeat = (TextView) this.activity.findViewById(R.id.allHeat);
		this.allTime = (TextView) this.activity.findViewById(R.id.allTime);

		this.setHeavy = (RelativeLayout) this.activity
				.findViewById(R.id.setheavy);
		this.clearHistory = (RelativeLayout) this.activity
				.findViewById(R.id.clearHistory);
		this.setHeavySeekBar = (SeekBar) this.activity
				.findViewById(R.id.setHeavySeekBar);
		this.heavyTextView = (TextView) this.activity.findViewById(R.id.heavy);
		this.setHeavySeekBarLayout = (RelativeLayout) this.activity
				.findViewById(R.id.setHeavySeekBarLayout);
	}

	/**
	 * 初始化组件
	 */
	private void initParams() {
		this.sqlHelper = new MySQLiteHelper(this.activity, "history.db", null,
				1);
	}

	/**
	 * 设置统计数据
	 */
	private void initStatics() {
		String allMile = String.valueOf(FileUtils.getAllMiles(activity)) + "米";
		this.allMile.setText(allMile);
		String allHeat = String.valueOf(FileUtils.getAllHeat(activity)) + "千卡";
		this.allHeat.setText(allHeat);
		String allTime = String.valueOf(FileUtils.getAllTime(activity)) + "秒";
		this.allTime.setText(allTime);
		int heavy = FileUtils.getHeavy(activity);
		this.heavyTextView.setText(heavy + "kg");
		this.setHeavySeekBar.setProgress(heavy);
	}

	/**
	 * 设置监听
	 */
	private void setListeners() {
		OnClickListener onClickListener = new OnClickListenerImpl();
		OnTouchListener onTouchListener = new OnTouchListenerImpl();
		this.setHeavy.setOnClickListener(onClickListener);
		this.setHeavy.setOnTouchListener(onTouchListener);
		this.clearHistory.setOnClickListener(onClickListener);
		this.clearHistory.setOnTouchListener(onTouchListener);
		this.setHeavySeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl());
		this.setHeavySeekBar.setOnTouchListener(onTouchListener);
	}

	// 仅仅用来将组件显示出来，真正写入文件的是在Listener中
	private void setHeavy() {
		this.setHeavySeekBarLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 清空历史记录
	 */
	private void clearHistory() {
		this.sqlHelper.clear();
		FileUtils.setAllMiles(activity, 0);
		FileUtils.setAllHeat(activity, 0);
		FileUtils.setAllTime(activity, 0);
		Toast.makeText(activity, "数据清除成功！", Toast.LENGTH_SHORT).show();
		this.initStatics();
	}

	/**
	 * 将heavy设置到面板上
	 * 
	 * @param heavy
	 */
	private void setHeavyTextView(int heavy) {
		this.heavyTextView.setText(heavy + "kg");
	}

	/**
	 * 单击监听类 当按下设置体重和清空历史记录，分别执行2项任务
	 * 
	 * @author 晨崴
	 * 
	 */
	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.setheavy) {
				setHeavy();
			} else if (v.getId() == R.id.clearHistory) {
				clearHistory();
			}
		}
	}

	/**
	 * 触摸监听类，并设置防误触 当手指悬停到响应2个设置选项时，改变其背景颜色
	 * 
	 * @author 晨崴
	 * 
	 */
	private class OnTouchListenerImpl implements OnTouchListener {
		private long startTime;
		private long endTime;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.setheavy || v.getId() == R.id.clearHistory) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundColor(0xffaecde6);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundColor(0xFFFFFFFF);
			} else if (v.getId() == R.id.setHeavySeekBar) {
				/**
				 * 防误触机制 如果2次触碰小于0.5秒,触发防误触机制，不执行任何操作
				 */
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startTime = new Date().getTime();
					System.out.println(startTime);
					break;
				case MotionEvent.ACTION_UP:
					endTime = new Date().getTime();
					System.out.println(endTime);
					return chargeTime();
				}

			}
			return false;
		}

		/**
		 * 核实时间差 若时间差小于300ms，则判断为误触 否则，不是误触
		 * 
		 * @return ----true 误触 ----false 不是误触
		 * */
		private boolean chargeTime() {
			if (endTime - startTime <= 300)
				return true;
			return false;

		}
	}

	/**
	 * 体重设置的进度条监控，当手指判定不是误触并离开屏幕时，将体重数据写入配置文件
	 * 
	 * @author 晨崴
	 * 
	 */
	private class OnSeekBarChangeListenerImpl implements
			OnSeekBarChangeListener {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			setHeavyTextView(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			setHeavySeekBarLayout.setVisibility(View.INVISIBLE);
			FileUtils.setHeavy(activity, seekBar.getProgress());
			Toast.makeText(activity, "体重设置成功:" + seekBar.getProgress() + "kg",
					Toast.LENGTH_SHORT).show();
		}
	}
}
