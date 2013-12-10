package nuist.wcw.model;

import nuist.wcw.Activity.R;
import nuist.wcw.Activity.RunActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 第一次运行ViewPagerIndicator的独立界面
 * 
 * @author 晨崴
 * 
 */
public class FirstRunFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";

	/**
	 * 静态构造
	 * 
	 * @param content
	 *            位置
	 * @param isLast
	 *            是否是最后一个
	 * @return FirstRunFragment类对象实例
	 */
	public static FirstRunFragment newInstance(int content, boolean isLast) {
		FirstRunFragment fragment = new FirstRunFragment();
		fragment.mContent = content;
		fragment.isLast = isLast;
		return fragment;
	}

	private int mContent;
	private boolean isLast = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 创建一个界面，如果是最后一个界面，多加一个用于设置体重的Edit和一个TextView
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView img = new ImageView(getActivity());
		img.setBackgroundResource(mContent);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		img.setLayoutParams(params);

		RelativeLayout layout = new RelativeLayout(getActivity());
		layout.setLayoutParams(params);
		layout.setGravity(Gravity.BOTTOM);
		layout.addView(img);

		if (isLast) {
			// 如果是最后一个界面，创建一个TextView和一个EditText
			TextView text = new TextView(getActivity());
			final EditText setHeavy = new EditText(getActivity());

			text.setText(R.string.firstText);
			text.setTextSize(40);
			text.setId(111);
			LayoutParams textParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			textParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			text.setLayoutParams(textParams);
			text.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String heavyString = setHeavy.getText().toString();
					try {
						int heavy = Integer.parseInt(heavyString);
						FileUtils.setHeavy(getActivity(), heavy);
						Toast.makeText(getActivity(),
								"体重已成功设置为" + heavy + "kg", Toast.LENGTH_LONG)
								.show();
					} catch (Exception e) {
						Toast.makeText(getActivity(), R.string.setHeavyError,
								Toast.LENGTH_LONG).show();
					} finally {
						Intent intent = new Intent(getActivity(),
								RunActivity.class);
						getActivity().startActivity(intent);
						getActivity().finish();
					}
				}
			});

			setHeavy.setHint(R.string.firstEditHint);
			setHeavy.setInputType(InputType.TYPE_CLASS_NUMBER);
			setHeavy.setEms(10);
			LayoutParams heavyParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			heavyParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);
			heavyParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
			setHeavy.setLayoutParams(heavyParams);

			layout.addView(text);
			layout.addView(setHeavy);

		}
		return layout;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(KEY_CONTENT, mContent);
	}
}
