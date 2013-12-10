package nuist.wcw.service;

import nuist.wcw.model.SaveSth;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

/**
 * GPSService 用于开启GPS服务
 * 
 * @author 晨崴
 * 
 */
public class GPSService extends Service {
	private LocationManager mLocationManager;
	private Handler handler;
	private LocationListener listener;
	private boolean getLocationFlag = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initParams();
		startService();
	}

	// 从Application中获取handler的引用
	private void initParams() {
		if (mLocationManager == null)
			this.mLocationManager = (LocationManager) super
					.getSystemService(Context.LOCATION_SERVICE);
		this.handler = ((SaveSth) getApplication()).getHandler();
	}

	// 开启GPS定位
	private void startService() {
		setGPSOptions();
	}

	// 设置GPS配置信息和监听
	private void setGPSOptions() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
		String provider = mLocationManager.getBestProvider(criteria, true); // 获取GPS信息

		// 设置位置改变监听
		if (listener == null){
			listener = new LocationListenerImpl();
		}
		mLocationManager.requestLocationUpdates(provider, 1000, 0.5f, listener);
	}
	
	//销毁GPS
	@Override
	public void onDestroy() {
		this.mLocationManager.removeUpdates(listener);
		super.onDestroy();
	}

	/**
	 * GPS状态监听类，用与GPS状态改变时将信息从Handler传出
	 * @author 晨崴
	 *
	 */
	private class LocationListenerImpl implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (getLocationFlag) {
				Message msg = new Message();
				msg.what = 2;
				msg.obj = location;
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				getLocationFlag = true;
				Toast.makeText(getApplicationContext(), "GPS正常",
						Toast.LENGTH_SHORT).show();
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				getLocationFlag = false;
				Toast.makeText(getApplicationContext(), "GPS在服务区外，不能提供定位",
						Toast.LENGTH_SHORT).show();
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				getLocationFlag = false;
				Toast.makeText(getApplicationContext(), "GPS关闭，不能提供定位",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS正常", Toast.LENGTH_SHORT)
					.show();

		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "GPS关闭，不能提供定位",
					Toast.LENGTH_SHORT).show();
			getLocationFlag = false;

		}

	}

}
