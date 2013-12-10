package nuist.wcw.model;

import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

/**
 * 时间计时类。
 * @author 晨崴
 *
 */
public class TimerTaskExd extends TimerTask {
	private int hour;
	private int minute;
	private int second = -1;
	private Handler handler;

	/**
	 * 构造函数，传入Handler的引用
	 * @param handler
	 */
	public TimerTaskExd(Handler handler) {
		this.handler = handler;
	}
	
	/**
	 * 开始运行，每隔0.1秒更新一次数据并传到前台显示
	 */
	@Override
	public void run() {
		String str = "";
		second++;
		if (second >= 60) {
			minute++;
			second -= 60;
		}
		if (minute >= 60) {
			hour++;
			minute -= 60;
		}
		if (hour < 10)
			str = str + "0" + hour;
		else
			str = str + hour;
		str = str + ":";
		// 输出分钟，要是小于10，前面加0
		if (minute < 10)
			str = str + "0" + minute;
		else
			str = str + minute;
		str = str + ":";
		// 输出秒钟，要是小于10，前面加0
		if (second < 10)
			str = str + "0" + second;
		else
			str = str + second;
		Message msg = new Message();
		msg.what = 1;
		msg.obj = str;
		handler.sendMessage(msg);
	}
}
