package nuist.wcw.Activity;

import nuist.wcw.controller.SettingsController;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 设置界面
 * 
 * @author 晨崴
 * 
 */
public class SettingsActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		SettingsController controller = new SettingsController(this); // 初始化控制模块
		controller.start(); // 控制模块启动
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.back, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.back:
			super.onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
