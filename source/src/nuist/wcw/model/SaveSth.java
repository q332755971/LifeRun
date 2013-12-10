package nuist.wcw.model;

import android.app.Application;
import android.os.Handler;

/**
 * Application的实例化对象，用于存储全局变量Handler
 * 
 * @author 晨崴
 * 
 */
public class SaveSth extends Application {
	private Handler handler;

	/**
	 * 获取handler
	 * 
	 * @return handler
	 */
	public Handler getHandler() {
		return handler;
	}

	/**
	 * 设置handler
	 * 
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
