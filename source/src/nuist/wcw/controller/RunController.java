package nuist.wcw.controller;

import java.util.Date;

import com.actionbarsherlock.app.SherlockActivity;

import nuist.wcw.Activity.CaidanActivity;
import nuist.wcw.Activity.FirstRunActivity;
import nuist.wcw.Activity.R;
import nuist.wcw.model.FileUtils;
import nuist.wcw.model.History;
import nuist.wcw.model.MySQLiteHelper;
import nuist.wcw.model.SaveSth;
import nuist.wcw.model.WeatherTask;
import nuist.wcw.service.GPSService;
import nuist.wcw.service.TimerService;
import nuist.wcw.widget.TextProgressBar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * RunActivity的控制类
 * 
 * @author 晨崴
 * 
 */
public class RunController {
	private Activity activity; // 引用
	// 以下为显示组件
	private ImageView timer_progress; // 计时进度
	private ImageView timer_pointer; // 计时指针
	private TextView recordTime; // 显示时间
	private RelativeLayout startAndEndLayout;// 用于更换点击颜色
	private TextView startAndEnd; // 开始结束按钮
	private TextView speed; // 显示当前速度
	private TextView heat; // 显示当前消耗热量
	private TextProgressBar progressBar; // 跑步路程进度
	// 以下为动画组件
	private Animation mAnimation; // 动画控制组件
	// 以下为控制组件
	private MySQLiteHelper sqlHelper;
	private boolean isRunning = false; // 是否执行的控制标记
	private Handler handler; // 处理类
	private Location lastLocation; // 记录最后一次定位的位置
	private int singleDistance = 0; // 单次跑步距离
	private Intent service4Timer; // 计时服务
	private Intent service4GPS; // GPS定位服务
	private Long startDate; // 单次开始时间
	private int heavy; // 体重
	private boolean isCaidanOpen = false; // 彩蛋是否没打开过
	private float maxSpeed; // 最高速度

	// 构造函数，初始化Activity的引用
	public RunController(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 与onCreate()共同执行，判断是否首次执行 同时设置天气
	 */
	public void start() {
		if (FileUtils.getIsFirstRun(activity)) { // 如果是第一次运行
			FileUtils.setIsFirstRun(activity, false);
			Intent intent = new Intent(this.activity, FirstRunActivity.class);
			this.activity.startActivity(intent);
			this.activity.finish();
		} else {
			// 获取并显示天气
			WeatherTask task = new WeatherTask((SherlockActivity) activity);
			task.execute();
		}
	}

	/**
	 * 与onResume()同步，更新体重信息。判断GPS状态，若不正常则跳转到GPS设置页面
	 */
	public void resume() {
		if (this.isGPSOnline()) {
			this.goToRun();
			this.heavy = FileUtils.getHeavy(activity);
		} else {
			this.createAlertDialog();
		}
	}

	/**
	 * 取得程序的运行状态
	 * 
	 * @return ---true 正在运行 ----false 不在运行
	 */
	public boolean getIsRunning() {
		return this.isRunning;
	}

	/**
	 * 当GPS未开启时创建该对话框，并执行相应跳转
	 */
	private void createAlertDialog() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			Intent intent;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					intent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					activity.startActivity(intent);
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					activity.finish();
					break;
				}
			}
		};
		AlertDialog dialog = new AlertDialog.Builder(activity)
				.setTitle(R.string.GPSAlertTitle)
				.setMessage(R.string.GPSAlertMessage)
				.setPositiveButton(R.string.settings, listener)
				.setNegativeButton(R.string.exit, listener).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 当Activity销毁时调用
	 */
	public void destroy() {
		this.stopAnimation(); // 停止订花
		this.stopTiming(); // 停止计时
		this.stopGPS(); // 销毁GPS监控
	}

	/**
	 * 判断GPS是否开启
	 * 
	 * @return ----true 开启 ----false 未开启
	 */
	private boolean isGPSOnline() {
		LocationManager alm = (LocationManager) this.activity
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 正式运行
	 */
	private void goToRun() {
		this.findViews(); // 获取控件id
		this.initParams(); // 初始化控制组件
		this.setListeners(); // 设置监听
	}

	/**
	 * 获取控件id
	 */
	private void findViews() {
		this.timer_progress = (ImageView) this.activity
				.findViewById(R.id.timer_progress);
		this.timer_pointer = (ImageView) this.activity
				.findViewById(R.id.timer_pointer);
		this.recordTime = (TextView) this.activity
				.findViewById(R.id.recordTime);
		this.startAndEnd = (TextView) this.activity
				.findViewById(R.id.startAndEnd);
		this.startAndEndLayout = (RelativeLayout) this.activity
				.findViewById(R.id.father);
		this.speed = (TextView) this.activity.findViewById(R.id.speed);
		this.heat = (TextView) this.activity.findViewById(R.id.heat);
		this.progressBar = (TextProgressBar) this.activity
				.findViewById(R.id.progressBar);
	}

	/**
	 * 初始化各控制组件
	 */
	@SuppressLint("HandlerLeak")
	private void initParams() {
		this.sqlHelper = new MySQLiteHelper(this.activity, "history.db", null,
				1);
		this.mAnimation = AnimationUtils.loadAnimation(activity, R.anim.tip);
		this.mAnimation.setInterpolator(new LinearInterpolator()); // 设置选装方式为线性旋转
		this.startAndEndLayout.setBackgroundColor(0xFFD2D2D2); // 设置按钮背景色
		this.handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1: // 设置计时器时间
					recordTime.setText((String) msg.obj);
					break;
				case 2: // 设置GPS相关信息
					dealWithDistance((Location) msg.obj);
					break;
				}
			}
		};
		((SaveSth) this.activity.getApplication()).setHandler(handler); // 将handler的引用传到application以供service调用
		this.startGPS();
	}

	/**
	 * 设置监听
	 */
	private void setListeners() {
		this.startAndEndLayout.setOnClickListener(new OnClickListenerImpl());
		this.startAndEndLayout.setOnTouchListener(new OnTouchListenerImpl());
	}

	/**
	 * 设置并开始动画
	 */
	private void startAnimation() {
		this.timer_pointer.startAnimation(mAnimation);
		this.timer_progress.startAnimation(mAnimation);
	}

	/**
	 * 停止动画
	 */
	private void stopAnimation() {
		if (this.timer_pointer != null)
			this.timer_pointer.clearAnimation();
		if (this.timer_progress != null)
			this.timer_progress.clearAnimation();
	}

	/**
	 * 开始计时
	 */
	private void startTiming() {

		if (this.service4Timer == null) {
			this.service4Timer = new Intent(this.activity, TimerService.class);
		}
		this.activity.startService(service4Timer);
	}

	/**
	 * 停止计时
	 */
	private void stopTiming() {
		// 关闭后台计时服务
		if (service4Timer != null)
			this.activity.stopService(service4Timer);

	}

	/**
	 * 开启GPS服务
	 */
	private void startGPS() {
		if (this.service4GPS == null) {
			service4GPS = new Intent(this.activity, GPSService.class);
		}
		this.activity.startService(service4GPS);
	}

	/**
	 * 终止GPS服务
	 */
	private void stopGPS() {
		if (service4GPS != null)
			this.activity.stopService(service4GPS);
	}

	/**
	 * 处理GPS返回的location数据，包括为开启彩蛋的放作弊系统 如果已经发现彩蛋并完成了任务同时判定没有作弊的话，显示进入彩蛋的对话框
	 * 作弊判定：最大速度大于13.0m/s或者时间小于770秒都归为作弊
	 * 
	 * @param location
	 *            GPS返回的GPS数据
	 */
	private void dealWithDistance(Location location) {
		if (this.isRunning == false)
			return;
		if (this.lastLocation == null)
			this.lastLocation = location;
		float[] results = new float[1];
		Location.distanceBetween(lastLocation.getLatitude(),
				lastLocation.getLongitude(), location.getLatitude(),
				location.getLongitude(), results);
		this.singleDistance += results[0];
		progressBar.setProgress(this.singleDistance);
		this.setSpeed(location.getSpeed());
		this.setHeat(singleDistance, heavy);
		this.lastLocation = location;
		if (this.maxSpeed < location.getSpeed()) {
			this.maxSpeed = location.getSpeed();
		}
		// 防止作弊
		if (this.singleDistance >= 10000 && !this.isCaidanOpen
				&& FileUtils.getCaidan(activity)) {
			this.isCaidanOpen = true;
			if (maxSpeed <= 13.0f
					&& this.parseTime(this.recordTime.getText().toString()) >= 770) {
				// 设置彩蛋
				this.createCaidanAlertDialog();
			} else {
				Toast.makeText(activity, R.string.cheat, Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	/**
	 * 彩蛋开启dialog
	 */
	private void createCaidanAlertDialog() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			Intent intent;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					intent = new Intent(activity, CaidanActivity.class);
					activity.startActivity(intent);
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				}
			}
		};
		AlertDialog dialog = new AlertDialog.Builder(activity).setTitle("提示！！")
				.setMessage("恭喜你发现并完成了彩蛋任务，点击'确定'打开彩蛋，点击'取消'返回程序")
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", listener).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 设置速度
	 * 
	 * @param speed
	 *            速度
	 */
	private void setSpeed(float speed) {
		this.speed.setText(R.string.currentSpeed);
		this.speed.append("\n" + speed + "m/s");
	}

	/**
	 * 设置热量
	 * 
	 * @param distance
	 *            距离
	 * @param heavy
	 *            体重
	 */
	private void setHeat(int distance, float heavy) {
		this.heat.setText(R.string.costHeat);
		if (heavy < 5.0f) {
			this.heat.setText(R.string.heavyError);
			return;
		}
		this.heat.setText("\n" + (int) (heavy * distance * 1.036 / 1000)
				+ "千卡(kcal)");
	}

	/**
	 * 单次结束存入历史记录 包括存单次记录的数据库和存存累计记录的配置文件
	 */
	private void saveHistory() {
		saveHistoryInDB(); // 单次存储到数据库种
		saveHistoryInSP(); // 更新配置文件里的总记录
	}

	/**
	 * 将数据存入数据库
	 */
	private void saveHistoryInDB() {
		int duration = parseTime(this.recordTime.getText().toString());
		int distance = this.singleDistance;
		int heat = 0;
		if (heavy < 5.0f) {
			heat = 0;
		} else {
			heat = (int) (heavy * distance * 1.036 / 1000);
		}
		History history = new History(startDate, duration, distance, heat);
		this.sqlHelper.insertData(history);
	}

	/**
	 * 将数据存入配置文件
	 */
	private void saveHistoryInSP() {
		// 设置总里程
		int allMiles = FileUtils.getAllMiles(activity);
		allMiles += this.singleDistance;
		FileUtils.setAllMiles(activity, allMiles);
		// 设置总热量
		int allHeat = FileUtils.getAllHeat(activity);
		allHeat += (int) (heavy * singleDistance * 1.036 / 1000);
		FileUtils.setAllHeat(activity, allHeat);
		// 设置总时间
		int allTime = FileUtils.getAllTime(activity);
		allTime += parseTime(this.recordTime.getText().toString());
		FileUtils.setAllTime(activity, allTime);
	}

	/**
	 * 将“xx:xx:xx”形式的时间转换成以秒为单位的int形
	 * 
	 * @param dura
	 *            持续时间
	 * @return 以秒为单位的时间长度
	 */
	private int parseTime(String dura) {
		String[] temp = dura.split(":");
		int hour = Integer.parseInt(temp[0]);
		int min = Integer.parseInt(temp[1]);
		int sec = Integer.parseInt(temp[2]);
		return hour * 3600 + min * 60 + sec;
	}

	/**
	 * 单击事件监听类
	 * 
	 * @author 晨崴
	 * 
	 */
	private class OnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (isRunning == false) { // 点击开始时
				isRunning = !isRunning; // 标志位改变
				startAnimation(); // 开始动画
				startTiming(); // 开始计时
				startAndEnd.setText(R.string.stopCalTime); // 设置按钮为“停止计时”
				startDate = new Date().getTime(); // 记录开始时间
				progressBar.setProgress(0); // 进度条设置进度为0
				progressBar.setMax(100); // 进度条进度最大设置为100
				lastLocation = null; // 上一次位置记录置空
				singleDistance = 0; // 单次距离设置为0
				maxSpeed = 0; // 单次开始时，最大速度变为0
			} else { // 点击停止时
				isRunning = !isRunning; // 标志位改变
				stopAnimation(); // 停止动画
				stopTiming(); // 停止计时
				saveHistory(); // 统计并记入历史记录
				startAndEnd.setText(R.string.startCalTime); // 设置按钮为“开始计时”
			}
		}
	}

	/**
	 * 触摸事件监听类，用于改变触摸时空间的背景颜色
	 * 
	 * @author 晨崴
	 * 
	 */
	private class OnTouchListenerImpl implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.father) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					v.setBackgroundColor(0xffaecde6);
				else if (event.getAction() == MotionEvent.ACTION_UP)
					v.setBackgroundColor(0xFFD2D2D2);
			}
			return false;
		}
	}
}
