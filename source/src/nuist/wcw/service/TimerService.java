package nuist.wcw.service;

import java.util.Timer;

import nuist.wcw.model.SaveSth;
import nuist.wcw.model.TimerTaskExd;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * TimerService 计时服务类
 * 
 * @author 晨崴
 * 
 */
public class TimerService extends Service {
	private TimerTaskExd task;
	private Timer timer = new Timer();
	private Handler handler;
	private WakeLock mWakeLock;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();
		this.handler = ((SaveSth) getApplication()).getHandler();
		task = new TimerTaskExd(handler);
		timer.schedule(task, 0, 1000);
		this.acquireWakeLock();
	}

	// 申请并锁定电源锁
	private void acquireWakeLock() {
		if (null == mWakeLock) {
			PowerManager pm = (PowerManager) super
					.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
					| PowerManager.ON_AFTER_RELEASE, "mWakeLock");
			if (null != mWakeLock) {
				mWakeLock.acquire(); // 绑定电源锁
			}
		}
	}

	// 释放电源锁
	private void releaseWakeLock() {
		if (null != mWakeLock) {
			mWakeLock.release();
			mWakeLock = null;
		}
	}

	@Override
	public void onDestroy() {
		task.cancel(); // 任务撤销
		System.out.println("service已销毁");
		this.releaseWakeLock(); // 取消电源锁
		super.onDestroy();
	}

}
