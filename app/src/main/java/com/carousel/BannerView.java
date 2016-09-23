/*
 * @Title:  SlideShowView.java
 * @Copyright:  Xitek Co., Ltd. Copyright 2015-2015,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  JiangHanQiao
 * @data:  2015年4月8日 下午1:59:58
 * @version:  V1.0
 */
package com.carousel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carousel.bean.MainPhotoBean;
import com.carousel.bean.MatchListBean;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager实现的轮播图广告自定义视图
 *
 */

public class BannerView extends FrameLayout {

	private AutoPlayTask autoPlayTask;
	private Context mContext;

	// 自定义轮播图的资源ID
	private List<MainPhotoBean.MatchListBean> elements;
	private LinearLayout dotLayout;
	private ViewPager viewPager;
	private Handler handler = new Handler();
	private TextView title;

	public BannerView(Context context) {
		this(context, null);
	}

	public BannerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.layout_slideshowview, this, true);
		elements = new ArrayList<>();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		title = (TextView) findViewById(R.id.title);
		autoPlayTask = new AutoPlayTask();
	}

	public void set(List<MainPhotoBean.MatchListBean> data) {
		elements.clear();
		elements.addAll(data);
		if (data.size() > 0) {
			initUI(data);
			if (data.size() > 1) {
				autoPlayTask.start();
			}
		} else {
			setVisibility(View.GONE);
		}
	}


	/**
	 * 初始化Views等UI
	 */
	private void initUI(List<MainPhotoBean.MatchListBean> data) {
		dotLayout.removeAllViews();
		for (int i = 0; i < data.size(); i++) {
				if(data.size()>1) {
					dotLayout.addView(addDotView(mContext, i));
				}
		}
		viewPager.setAdapter(new MyPagerAdapter());
		if(elements.size()>1) {
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
		}
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					autoPlayTask.stop();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					autoPlayTask.start();
				}

				return false;
			}
		});
	}

	private class AutoPlayTask implements Runnable {

		private int AUTO_PLAY_TIME = 4000;

		private boolean has_auto_play = false;

		@Override
		public void run() {
			if(has_auto_play){
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
				handler.postDelayed(this, AUTO_PLAY_TIME);
			}

		}

		public void stop() {
			has_auto_play = false;
			handler.removeCallbacks(this);
		}

		public void start() {
			if(!has_auto_play){
				has_auto_play = true;
				handler.removeCallbacks(this);
				handler.postDelayed(this, AUTO_PLAY_TIME);
			}
		}

	}

	public void stopPlay() {
		autoPlayTask.stop();
	}

	private ImageView addDotView(Context context, int i) {
		int width = Dp2Px(context, 7);
		int padding = Dp2Px(context, 7);
		ImageView dotVoew = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
		lp.setMargins(padding, padding, padding, padding);
		dotVoew.setLayoutParams(lp);
		if (i == 0) {
			dotVoew.setImageResource(R.drawable.dot_blue);
		} else {
			dotVoew.setImageResource(R.drawable.dot_white);
		}
		return dotVoew;
	}

	/**
	 * 填充ViewPager的页面适配器
	 *
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			 ((ViewPager)container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			int i = position % elements.size();
			SimpleDraweeView photoView = new SimpleDraweeView(getContext());
			DraweeController controller = ConfigConstants.getDraweeController(
					ConfigConstants.getImageRequest(photoView, DosnapApp.apiHost + elements.get(i).getImgurl()), photoView);
			photoView.setController(controller);
			elements.get(i >= elements.size() ? i % elements.size() : i);//防止错乱
			((ViewPager) container).addView(photoView);
			String mTitle = elements.get(i).getTitle();
			title.setText(mTitle);
			return photoView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
	}

	private OnClickListener mListener = new OnClickListener() {
		public void onClick(View v) {
//			Intent intent = new Intent(v.getContext(), ActivityActivity.class);
//			Bundle b = new Bundle();
//			b.putString("mid",v.getTag().toString());
//			b.putInt("eventtype", Integer.parseInt(v.getTag(R.id.tag_id).toString()));
//			intent.putExtras(b);
//			((Activity) v.getContext()).startActivityForResult(intent, DosnapApp.ACTIVITY_ACTIVITY);
		}
	};

	private OnClickListener aListener = new OnClickListener() {
		public void onClick(View v) {
//			Intent intent = new Intent(v.getContext(), ArticleActivity.class);
//			Bundle b = new Bundle();
//			b.putString("article",v.getTag().toString());
//			intent.putExtras(b);
//			((Activity) v.getContext()).startActivityForResult(intent, DosnapApp.ACTIVITY_ACTIVITY);
		}
	};

	private OnClickListener topicListener = new OnClickListener() {
		public void onClick(View v) {
//			Intent intent = new Intent(v.getContext(), TopicActivity.class);
//			Bundle b = new Bundle();
//			b.putString("article",v.getTag().toString());
//			intent.putExtras(b);
//			((Activity) v.getContext()).startActivityForResult(intent, DosnapApp.ACTIVITY_ACTIVITY);
		}
	};

	private OnClickListener h5Listener = new OnClickListener() {
		public void onClick(View v) {
//			Intent intent = new Intent(v.getContext(), H5Activity.class);
//			Bundle b = new Bundle();
//			b.putString("article",v.getTag().toString());
//			intent.putExtras(b);
//			((Activity) v.getContext()).startActivityForResult(intent, DosnapApp.ACTIVITY_ACTIVITY);
		}
	};
	private OnClickListener otherListener = new OnClickListener() {
		public void onClick(View v) {
		}
	};

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 *
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
				case 1:// 手势滑动，空闲中
					isAutoPlay = false;
					autoPlayTask.stop();
					break;
				case 2:// 界面切换中
					isAutoPlay = true;
					break;
				case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
					if (viewPager.getCurrentItem() == viewPager.getAdapter()
							.getCount() - 1 && !isAutoPlay) {
						viewPager.setCurrentItem(0);
					}
					// 当前为第一张，此时从左向右滑，则切换到最后一张
					else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
						viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
					}
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub
			for (int i = 0; i < dotLayout.getChildCount(); i++) {
				if (i == pos % elements.size()) {
					((ImageView) dotLayout.getChildAt(i))
							.setImageResource(R.drawable.dot_blue);
				} else {
					((ImageView) dotLayout.getChildAt(i))
							.setImageResource(R.drawable.dot_white);
				}
			}
		}
	}

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
}