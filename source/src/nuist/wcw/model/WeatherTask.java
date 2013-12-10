package nuist.wcw.model;

import java.net.URLEncoder;

import nuist.wcw.Activity.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * 获得天气数据，并设置到ActionBar的导航栏上 继承自AsyncTask，是异步处理类
 * 
 * @author 晨崴
 * 
 */
public class WeatherTask extends AsyncTask<String, Integer, String[]> {
	private SherlockActivity activity; // activity的引用
	private static final String getIpURL = "http://iframe.ip138.com/ic.asp"; // 根据ip获取城市信息的URL
	private static final String getWeatherURL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx/getWeatherbyCityName?theCityName="; // 获取气象信息的WebService的URL
	private String city; // 城市
	private String temperature; // 温度
	private String weather; // 天气情况
	private ActionBar actionBar;

	/**
	 * 将传入的引用赋值
	 * 
	 * @param activity
	 *            SherlockActivity的引用
	 */
	public WeatherTask(SherlockActivity activity) {
		this.activity = activity;
		this.actionBar = this.activity.getSupportActionBar();
	}

	/**
	 * 在开始联网之前检查网络是否可用。 若可用，则继续联网 若不可用，则把ActionBar的title设置成程序名后返回。
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		System.out.println(this.testConnection());
		if (this.testConnection()) {// 检测是否能联网
			this.actionBar.setTitle(R.string.connecting);
		} else {
			this.cancel(true);
		}
	}

	/**
	 * 问题最大的一个类 这个类中adapter实例化时经常在2.3.X的系统上崩溃 因此，在实例化时添加try--catch语句
	 * 功能是将联网获取到的天气设置到SherlockActivity的title上
	 */
	@SuppressWarnings("unused")
	@Override
	protected void onPostExecute(String[] result) {
		super.onPostExecute(result);
		ArrayAdapter<String> adapter = null;
		try {
			adapter = new ArrayAdapter<String>(this.activity,
					android.R.layout.simple_spinner_dropdown_item, result);
		} catch (Exception e) {
			this.actionBar.setTitle(R.string.app_name);
			return;
		}
		if (adapter == null) {
			this.actionBar.setTitle(R.string.app_name);
			return;
		}
		this.actionBar.setTitle(null);
		this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		this.actionBar.setListNavigationCallbacks(adapter, null);
	}

	/**
	 * 异步处理类中最重要的一个类，完成后台的联网，获取城市和天气
	 */
	@Override
	protected String[] doInBackground(String... params) {
		try {
			this.setCity();
			this.setWeatherAndTemperature();
		} catch (Exception e) {
			this.cancel(true);
		}
		if (this.city != null && this.temperature != null
				&& this.weather != null) {
			System.out.println(city + temperature + weather);
			return new String[] { this.weather, this.temperature, this.city };
		} else {
			return null;
		}
	}

	/**
	 * 测试是否能联网
	 * 
	 * @return ---true 能 ---false 不能
	 */
	private boolean testConnection() {
		ConnectivityManager cm = (ConnectivityManager) this.activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取城市并设置城市，当联网异常时城市设置为null
	 * 
	 * @throws Exception
	 */
	private void setCity() throws Exception {
		HttpGet request = new HttpGet(getIpURL);
		HttpClient client = new DefaultHttpClient();

		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils
					.toString(response.getEntity(), "GB2312");
			city = XMLAnalyzing.getCity(result);
		} else {
			city = null;
		}
	}

	/**
	 * 联网获取天气和温度，并设置到属性中
	 * 
	 * @throws Exception
	 */
	private void setWeatherAndTemperature() throws Exception {
		if (city == null) {
			this.onCancelled();
			Toast.makeText(activity, "联网获取天气失败", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpGet request = new HttpGet(getWeatherURL
				+ URLEncoder.encode(city, "UTF-8"));
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			this.temperature = XMLAnalyzing.getTemperature(result);
			this.weather = XMLAnalyzing.getWeather(result);
		}
	}
}
