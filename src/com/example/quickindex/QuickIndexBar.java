package com.example.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {

	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Paint paint;
	private int mWidth;
	private float mHeight;

	public QuickIndexBar(Context context) {
		super(context);
		initView();
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
		paint.setColor(Color.WHITE);
		paint.setTextSize(22);
		paint.setTextAlign(Align.CENTER);// 设置文本起点是文字底边框的中心
	}

	// 修改文字位置
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = getMeasuredWidth();// 格子宽度
		mHeight = getMeasuredHeight() * 1f / indexArr.length;// 格子高度
	}

	/**
	 * 绘制x轴坐标:mWidth/2 绘制y轴坐标:格子高度的一半 + 文本高度的一半 + position * 格子高度
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// String text = "黑";
		for (int i = 0; i < indexArr.length; i++) {
			float width = mWidth / 2;
			float height = mHeight / 2 + getTextHeight(indexArr[i]) / 2 + i
					* mHeight;
			paint.setColor(lastIndex == i ? Color.GRAY : Color.WHITE);
			canvas.drawText(indexArr[i], width, height, paint);
		}
	}

	private int getTextHeight(String text) {
		// 获取文本高度
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return bounds.height();
	}

	private int lastIndex = -1;// 记录上次触摸事件的索引
	private OnTouchLetterListener listener;

	/**
	 * 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			float downY = event.getY();
			int index = (int) (downY / mHeight);// 得到字母对应的索引
			if (lastIndex != index) {
				// 说明当前触摸的和上一次触摸的不是同一个字母
				// Log.e("tag", "index :"+indexArr[index]);
				// 对index安全性进行检查
				if (index >= 0 && index < indexArr.length) {
					if (listener != null) {
						listener.onTouchLetter(indexArr[index]);
					}
				}
			}
			lastIndex = index;
			break;
		case MotionEvent.ACTION_UP:
			// 重置lastIndex
			lastIndex = -1;
			break;

		default:
			break;
		}
		// 引起重绘
		invalidate();
		return true;
	}

	public void setOnTouchLetterListener(OnTouchLetterListener listener) {
		this.listener = listener;
	}

	/**
	 * 触摸字母的监听器
	 * 
	 * @author Administrator
	 * 
	 */
	// 接口回调
	public interface OnTouchLetterListener {
		public void onTouchLetter(String letter);
	}
}
