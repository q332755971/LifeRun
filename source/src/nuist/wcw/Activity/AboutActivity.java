package nuist.wcw.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 关于界面
 * 
 * @author 晨崴
 * 
 */
public class AboutActivity extends SherlockActivity {
	private TextView thanks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.thanks = (TextView) super.findViewById(R.id.thanks);
		this.thanks.setPaintFlags(thanks.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		this.thanks.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this,
						ThanksActivity.class);
				AboutActivity.this.startActivity(intent);
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
