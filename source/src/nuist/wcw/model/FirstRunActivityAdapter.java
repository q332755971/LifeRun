package nuist.wcw.model;

import nuist.wcw.Activity.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 继承自FragmentPagerAdapter，用于将CONTENT中的内容转成ViewPager的内容被用于显示
 * 
 * @author 晨崴
 * 
 */
public class FirstRunActivityAdapter extends FragmentPagerAdapter {
	protected static final int[] CONTENT = { R.drawable.first_1,
			R.drawable.first_2, R.drawable.first_3 };

	private int mCount = CONTENT.length;

	public FirstRunActivityAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * 获取Item
	 */
	@Override
	public Fragment getItem(int position) {
		boolean flag = false;
		if (position == (mCount - 1)) {
			flag = true;
		}
		return FirstRunFragment.newInstance(CONTENT[position % CONTENT.length],
				flag);
	}

	/**
	 * 获取Count
	 */
	@Override
	public int getCount() {
		return mCount;
	}

	/**
	 * 设置数量
	 * 
	 * @param count
	 *            数量
	 */
	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

}
