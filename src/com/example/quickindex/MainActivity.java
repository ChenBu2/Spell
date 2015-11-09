package com.example.quickindex;

import java.util.ArrayList;
import java.util.Collections;

import com.example.quickindex.QuickIndexBar.OnTouchLetterListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private QuickIndexBar qib_quick_index;
	private ListView lv_list_item;
	private TextView tv_currentWord;

	private ArrayList<Friend> friends = new ArrayList<Friend>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initUI();

		initData();
	}

	/**
	 * 获取数据
	 */
	private void initData() {
		// 准备数据
		fillList();
		// 2.对数据进行排序
		Collections.sort(friends);
		// 设置adapter
		lv_list_item.setAdapter(new MyAdapter(friends));
	}

	class MyAdapter extends BaseAdapter {

		private ArrayList<Friend> friends;

		public MyAdapter(ArrayList<Friend> friends) {
			this.friends = friends;
		}

		@Override
		public int getCount() {
			return friends.size();
		}

		@Override
		public Object getItem(int position) {
			return friends.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.adapter_friend, null);
			}
			ViewHolder holder = ViewHolder.getHolder(convertView);
			// 刷新数据
			Friend friend = friends.get(position);
			holder.tv_name.setText(friend.getName());
			String currentWord = friend.getSpell().charAt(0) + "";
			if (position > 0) {
				// 获取上一个item的首字母
				String lastWord = friends.get(position - 1).getSpell()
						.charAt(0)
						+ "";
				// 拿当前的首字母比较
				if (currentWord.equals(lastWord)) {
					// 说明字母相同,隐藏当前的tv_first_word
					holder.tv_first_word.setVisibility(View.GONE);
				} else {
					// 不一样，需要显示当前的首字母
					// 由于布局是复用的，所以在需要显示的时候，再次将first_word设置为可见
					holder.tv_first_word.setVisibility(View.VISIBLE);
					holder.tv_first_word.setText(currentWord);
				}
			} else {
				holder.tv_first_word.setVisibility(View.VISIBLE);
				holder.tv_first_word.setText(currentWord);
			}
			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tv_first_word;
		public TextView tv_name;

		public ViewHolder(View convertView) {
			tv_first_word = (TextView) convertView
					.findViewById(R.id.tv_first_word);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		}

		public static ViewHolder getHolder(View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}

	/**
	 * 更新UI
	 */
	private void initUI() {
		qib_quick_index = (QuickIndexBar) findViewById(R.id.qib_quick_index);
		lv_list_item = (ListView) findViewById(R.id.lv_list_item);
		tv_currentWord = (TextView) findViewById(R.id.tv_currentWord);
		qib_quick_index.setOnTouchLetterListener(new OnTouchLetterListener() {
			// 根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
			@Override
			public void onTouchLetter(String letter) {
				for (int i = 0; i < friends.size(); i++) {
					String firstWord = friends.get(i).getSpell().charAt(0) + "";
					if (letter.equals(firstWord)) {
						// 说明找到了,将其放置顶端
						lv_list_item.setSelection(i);
						break;// 只需要找到第一个就行
					}
				}
				// 显示当前触摸的字母
				showCurrentWord(letter);
			}
		});
		// 通过缩小currentWord来隐藏
		ViewHelper.setScaleX(tv_currentWord, 0);
		ViewHelper.setScaleY(tv_currentWord, 0);
	}

	private boolean isScale = false;// 默认不开启动画
	private Handler mHandler = new Handler();

	protected void showCurrentWord(String letter) {
		tv_currentWord.setText(letter);
		// 加入动画功能
		if (!isScale) {
			isScale = true;
			ViewPropertyAnimator.animate(tv_currentWord).scaleX(1f)
					.setInterpolator(new OvershootInterpolator())
					.setDuration(450).start();
			ViewPropertyAnimator.animate(tv_currentWord).scaleY(1f)
					.setInterpolator(new OvershootInterpolator())
					.setDuration(450).start();
		}

		// 先移除之前的任务
		mHandler.removeCallbacksAndMessages(null);
		// 延时隐藏tv_currentWord(运行在主线程)
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// tv_currentWord.setVisibility(View.INVISIBLE);
				ViewPropertyAnimator.animate(tv_currentWord).scaleX(0f)
						.setDuration(450).start();
				ViewPropertyAnimator.animate(tv_currentWord).scaleY(0f)
						.setDuration(450).start();
				isScale = false;
			}
		}, 1000);
	}

	private void fillList() {
		// 虚拟数据
		friends.add(new Friend("李伟"));
		friends.add(new Friend("张三"));
		friends.add(new Friend("阿三"));
		friends.add(new Friend("阿四"));
		friends.add(new Friend("段誉"));
		friends.add(new Friend("段正淳"));
		friends.add(new Friend("张三丰"));
		friends.add(new Friend("陈坤"));
		friends.add(new Friend("林俊杰1"));
		friends.add(new Friend("陈坤2"));
		friends.add(new Friend("王二a"));
		friends.add(new Friend("林俊杰a"));
		friends.add(new Friend("张四"));
		friends.add(new Friend("林俊杰"));
		friends.add(new Friend("王二"));
		friends.add(new Friend("王二b"));
		friends.add(new Friend("赵四"));
		friends.add(new Friend("杨坤"));
		friends.add(new Friend("赵子龙"));
		friends.add(new Friend("杨坤1"));
		friends.add(new Friend("李伟1"));
		friends.add(new Friend("宋江"));
		friends.add(new Friend("宋江1"));
		friends.add(new Friend("李伟3"));
	}
}
