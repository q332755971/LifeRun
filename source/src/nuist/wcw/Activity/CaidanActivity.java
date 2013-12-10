package nuist.wcw.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 彩蛋界面 所有显示文字都写在Java代码中，拒绝在xml文件被反编译
 * 
 * @author 晨崴
 * 
 */
public class CaidanActivity extends SherlockActivity {
	private TextView caidanTitle, caidanIntroduce, caidanContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caidan);
		super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.caidanTitle = (TextView) super.findViewById(R.id.caidanTitle);
		this.caidanIntroduce = (TextView) super
				.findViewById(R.id.caidanIntroduce);
		this.caidanContent = (TextView) super.findViewById(R.id.caidanContent);

		this.caidanTitle.setText("彩蛋");
		this.caidanIntroduce.setText("首先，祝贺你坚持下来了10千米的奔跑！");
		this.caidanContent.setText("  你被骗了~没有彩蛋\n  不过你跑完全程的毅力值得钦佩！~");
		this.caidanIntroduce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				caidanContent.setVisibility(View.VISIBLE);
			}
		});
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
