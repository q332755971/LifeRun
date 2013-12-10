package nuist.wcw.Activity;

import nuist.wcw.model.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * 欢迎界面，程序每次开启(除第一次)都会经过这个界面
 * 
 * @author 晨崴
 * 
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐去状态栏部分(电池等图标和一切修饰部分)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.charge(); // 判定是否是第一次运行
		setContentView(R.layout.activity_welcome);

	}

	/**
	 * 判断是否是第一次运行 若是，跳转到新手指导界面 否则，延迟3秒跳转到主界面
	 */
	private void charge() {
		if (FileUtils.getIsFirstRun(this)) { // 如果是第一次运行
			FileUtils.setIsFirstRun(this, false);
			Intent intent = new Intent(this, FirstRunActivity.class);
			this.startActivity(intent);
			this.finish();
		} else {
			final Intent intent = new Intent(this, RunActivity.class);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(intent);
					WelcomeActivity.this.finish();
				}
			}, 3000);
		}
	}
}
