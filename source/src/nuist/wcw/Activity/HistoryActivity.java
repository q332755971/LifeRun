package nuist.wcw.Activity;

import nuist.wcw.controller.HistoryController;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 历史记录界面
 * 
 * @author 晨崴
 * 
 */
public class HistoryActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		HistoryController controller = new HistoryController(this); // 初始化控制模块
		controller.start(); // 控制模块启动
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.back, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// 因为本人习惯左手操作手机，同时顾及到大部分人习惯右手操作手机，所以两边的角上都加上返回按钮
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
