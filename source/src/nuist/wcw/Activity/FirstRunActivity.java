package nuist.wcw.Activity;

import nuist.wcw.model.FirstRunActivityAdapter;

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

/**
 * 第一次启动界面
 * 
 * @author 晨崴
 * 
 */
public class FirstRunActivity extends FragmentActivity {

	FirstRunActivityAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐去标题栏（应用程序的名字）
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐去状态栏部分(电池等图标和一切修饰部分)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_first_run);

		mAdapter = new FirstRunActivityAdapter(getSupportFragmentManager());
		mPager = (ViewPager) super.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (LinePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

}
