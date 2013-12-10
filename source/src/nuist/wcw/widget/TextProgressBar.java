package nuist.wcw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 自定义ProgressBar，用于在传统progressBar的基础上显示文字进度的文字信息
 * @author 晨崴
 *
 */
public class TextProgressBar extends ProgressBar {
	String text;
	Paint mPaint;

	/**
	 * 构造函数
	 * @param context 传入Context属性
	 */
	public TextProgressBar(Context context) {
		super(context);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		// TODO Auto-generated method stub
		setText(progress);
		super.setProgress(progress);
	}
	
	//重写onDraw方法，实现文字的显示
	@SuppressLint("DrawAllocation")
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// this.setText();
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x, y, this.mPaint);
	}

	// 初始化，画笔
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.BLACK);
		this.mPaint.setTextSize(40);
	}


	// 设置文字内容
	private void setText(int progress) {
		int sumProgress = super.getMax();
		while(progress >= sumProgress*0.8){
			sumProgress = 2 * sumProgress;
			super.setMax(sumProgress);
			Toast.makeText(getContext(), "加油，你行的！！！", Toast.LENGTH_SHORT).show();
		}
		this.text = "" + progress + "/" + sumProgress + "米";
	}
}