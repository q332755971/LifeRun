package nuist.wcw.Activity;

import nuist.wcw.controller.RunController;
import nuist.wcw.model.FileUtils;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 主界面
 * 
 * @author 晨崴
 * 
 */
public class RunActivity extends SherlockActivity {
	private RunController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_run);
		super.getSupportActionBar().setHomeButtonEnabled(true);
		super.getSupportActionBar().setLogo(R.drawable.act_logo);

		this.controller = new RunController(this);// 初始化控制模块
		this.controller.start();// 控制模块启动
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.controller.resume();
	}

	@Override
	protected void onDestroy() {
		this.controller.destroy();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (this.controller.getIsRunning()) {
			new AlertDialog.Builder(this).setTitle("返回键")
					.setMessage(R.string.backpressed)
					.setPositiveButton("确定", null).show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this,
					"恭喜你发现了本程序的彩蛋！！\n那我就偷偷告诉你\n要是跑到10km，会有惊喜哦 ^_^",
					Toast.LENGTH_LONG).show();
			FileUtils.setCaidan(this, true);// 彩蛋被发现
			break;
		case R.id.history:
			intent = new Intent(this, HistoryActivity.class);
			this.startActivity(intent);
			break;
		case R.id.settings:
			intent = new Intent(this, SettingsActivity.class);
			this.startActivity(intent);
			break;
		case R.id.about:
			intent = new Intent(this, AboutActivity.class);
			this.startActivity(intent);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.run, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
