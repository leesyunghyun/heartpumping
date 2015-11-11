package com.example.heartpumping;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class HPHome extends TabActivity {

	public static TabHost tabhost1;
	private Intent intent;
	private PointF mLastPoint;
	private int prevtab = 0;
	private Intent result;
	Cursor c;
	HPDatabase hpdb;
	SQLiteDatabase sql;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hpdb = new HPDatabase(HPHome.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		tabhost1 = (TabHost) findViewById(android.R.id.tabhost);
		mLastPoint = new PointF();

		LayoutInflater vi1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout a11 = (LinearLayout) vi1.inflate(
				R.layout.hpiconbackground, null);
		
		
		LayoutInflater vi2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout a12 = (LinearLayout) vi2.inflate(
				R.layout.hpiconbackground, null);
		
		
		LayoutInflater vi3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout a13 = (LinearLayout) vi3.inflate(
				R.layout.hpiconbackground, null);
		
		
		LayoutInflater vi4 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout a14 = (LinearLayout) vi4.inflate(
				R.layout.hpiconbackground, null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				a11.setBackgroundResource(R.drawable.home_icon_select_blue);
				a12.setBackgroundResource(R.drawable.board_icon_blue);
				a13.setBackgroundResource(R.drawable.find_icon_blue);
				a14.setBackgroundResource(R.drawable.setting_icon_blue);
				break;
			case 1:
				a11.setBackgroundResource(R.drawable.home_icon_select_pupple);
				a12.setBackgroundResource(R.drawable.board_icon_pupple);
				a13.setBackgroundResource(R.drawable.find_icon_pupple);
				a14.setBackgroundResource(R.drawable.setting_icon_pupple);
				break;
			case 2:
				a11.setBackgroundResource(R.drawable.home_icon_select_green);
				a12.setBackgroundResource(R.drawable.board_icon_green);
				a13.setBackgroundResource(R.drawable.find_icon_green);
				a14.setBackgroundResource(R.drawable.setting_icon_green);
				break;
			case 3:
				a11.setBackgroundResource(R.drawable.home_icon_select_red);
				a12.setBackgroundResource(R.drawable.board_icon_red);
				a13.setBackgroundResource(R.drawable.find_icon_red);
				a14.setBackgroundResource(R.drawable.setting_icon_red);
				break;
			case 4:
				a11.setBackgroundResource(R.drawable.home_icon_select_gold);
				a12.setBackgroundResource(R.drawable.board_icon_gold);
				a13.setBackgroundResource(R.drawable.find_icon_gold);
				a14.setBackgroundResource(R.drawable.setting_icon_gold);
				break;
			}
		}
		
		intent = new Intent();
		intent.setClass(HPHome.this, HPHomeHome.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TabSpec spec1 = tabhost1.newTabSpec("spec1").setIndicator(a11);
		spec1.setContent(intent);
		tabhost1.addTab(spec1);

		intent = new Intent();
		intent.setClass(HPHome.this, HPHomeBoard.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TabSpec spec2 = tabhost1.newTabSpec("spec2").setIndicator(a12);
		spec2.setContent(intent);
		
		tabhost1.addTab(spec2);

		intent = new Intent();
		intent.setClass(HPHome.this, HPHomeFind.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TabSpec spec3 = tabhost1.newTabSpec("spec3").setIndicator(a13);
		
		spec3.setContent(intent);
		tabhost1.addTab(spec3);

		intent = new Intent();
		intent.setClass(HPHome.this, HPHomeSetting.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TabSpec spec4 = tabhost1.newTabSpec("spec4").setIndicator(a14);
		
		spec4.setContent(intent);
		tabhost1.addTab(spec4);

		tabhost1.setCurrentTab(0);
		tabhost1.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO 자동 생성된 메소드 스텁
				if (prevtab > tabhost1.getCurrentTab()) {
					tabhost1.getCurrentView().startAnimation(
							AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.right_in));
				} else if (prevtab < tabhost1.getCurrentTab()) {
					tabhost1.getCurrentView().startAnimation(
							AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.left_in));
				}
				c = sql.rawQuery("select * from personal", null);
				if(c.moveToFirst())
				{
					switch(c.getInt(6))
					{
					case 0:
						switch(tabhost1.getCurrentTab())
						{
						case 0:
							a11.setBackgroundResource(R.drawable.home_icon_select_blue);
							a12.setBackgroundResource(R.drawable.board_icon_blue);
							a13.setBackgroundResource(R.drawable.find_icon_blue);
							a14.setBackgroundResource(R.drawable.setting_icon_blue);
							break;
						case 1:
							a11.setBackgroundResource(R.drawable.home_icon_blue);
							a12.setBackgroundResource(R.drawable.board_icon_select_blue);
							a13.setBackgroundResource(R.drawable.find_icon_blue);
							a14.setBackgroundResource(R.drawable.setting_icon_blue);
							break;
						case 2:
							a11.setBackgroundResource(R.drawable.home_icon_blue);
							a12.setBackgroundResource(R.drawable.board_icon_blue);
							a13.setBackgroundResource(R.drawable.find_icon_select_blue);
							a14.setBackgroundResource(R.drawable.setting_icon_blue);
							break;
						case 3:
							a11.setBackgroundResource(R.drawable.home_icon_blue);
							a12.setBackgroundResource(R.drawable.board_icon_blue);
							a13.setBackgroundResource(R.drawable.find_icon_blue);
							a14.setBackgroundResource(R.drawable.setting_icon_select_blue);
							break;
						case 4:
							break;
						}
						break;
					case 1:
						switch(tabhost1.getCurrentTab())
						{
						case 0:
							a11.setBackgroundResource(R.drawable.home_icon_select_pupple);
							a12.setBackgroundResource(R.drawable.board_icon_pupple);
							a13.setBackgroundResource(R.drawable.find_icon_pupple);
							a14.setBackgroundResource(R.drawable.setting_icon_pupple);
							break;
						case 1:
							a11.setBackgroundResource(R.drawable.home_icon_pupple);
							a12.setBackgroundResource(R.drawable.board_icon_select_pupple);
							a13.setBackgroundResource(R.drawable.find_icon_pupple);
							a14.setBackgroundResource(R.drawable.setting_icon_pupple);
							break;
						case 2:
							a11.setBackgroundResource(R.drawable.home_icon_pupple);
							a12.setBackgroundResource(R.drawable.board_icon_pupple);
							a13.setBackgroundResource(R.drawable.find_icon_select_pupple);
							a14.setBackgroundResource(R.drawable.setting_icon_pupple);
							break;
						case 3:
							a11.setBackgroundResource(R.drawable.home_icon_pupple);
							a12.setBackgroundResource(R.drawable.board_icon_pupple);
							a13.setBackgroundResource(R.drawable.find_icon_pupple);
							a14.setBackgroundResource(R.drawable.setting_icon_select_pupple);
							break;
						}
						break;
					case 2:
						switch(tabhost1.getCurrentTab())
						{
						case 0:
							a11.setBackgroundResource(R.drawable.home_icon_select_green);
							a12.setBackgroundResource(R.drawable.board_icon_green);
							a13.setBackgroundResource(R.drawable.find_icon_green);
							a14.setBackgroundResource(R.drawable.setting_icon_green);
							break;
						case 1:
							a11.setBackgroundResource(R.drawable.home_icon_green);
							a12.setBackgroundResource(R.drawable.board_icon_select_green);
							a13.setBackgroundResource(R.drawable.find_icon_green);
							a14.setBackgroundResource(R.drawable.setting_icon_green);
							break;
						case 2:
							a11.setBackgroundResource(R.drawable.home_icon_green);
							a12.setBackgroundResource(R.drawable.board_icon_green);
							a13.setBackgroundResource(R.drawable.find_icon_select_green);
							a14.setBackgroundResource(R.drawable.setting_icon_green);
							break;
						case 3:
							a11.setBackgroundResource(R.drawable.home_icon_green);
							a12.setBackgroundResource(R.drawable.board_icon_green);
							a13.setBackgroundResource(R.drawable.find_icon_green);
							a14.setBackgroundResource(R.drawable.setting_icon_select_green);
							break;
						}
						break;
					case 3:
						switch(tabhost1.getCurrentTab())
						{
						case 0:
							a11.setBackgroundResource(R.drawable.home_icon_select_red);
							a12.setBackgroundResource(R.drawable.board_icon_red);
							a13.setBackgroundResource(R.drawable.find_icon_red);
							a14.setBackgroundResource(R.drawable.setting_icon_red);
							break;
						case 1:
							a11.setBackgroundResource(R.drawable.home_icon_red);
							a12.setBackgroundResource(R.drawable.board_icon_select_red);
							a13.setBackgroundResource(R.drawable.find_icon_red);
							a14.setBackgroundResource(R.drawable.setting_icon_red);
							break;
						case 2:
							a11.setBackgroundResource(R.drawable.home_icon_red);
							a12.setBackgroundResource(R.drawable.board_icon_red);
							a13.setBackgroundResource(R.drawable.find_icon_select_red);
							a14.setBackgroundResource(R.drawable.setting_icon_red);
							break;
						case 3:
							a11.setBackgroundResource(R.drawable.home_icon_red);
							a12.setBackgroundResource(R.drawable.board_icon_red);
							a13.setBackgroundResource(R.drawable.find_icon_red);
							a14.setBackgroundResource(R.drawable.setting_icon_select_red);
							break;
						}
						break;
					case 4:
						switch(tabhost1.getCurrentTab())
						{
						case 0:
							a11.setBackgroundResource(R.drawable.home_icon_select_gold);
							a12.setBackgroundResource(R.drawable.board_icon_gold);
							a13.setBackgroundResource(R.drawable.find_icon_gold);
							a14.setBackgroundResource(R.drawable.setting_icon_gold);
							break;
						case 1:
							a11.setBackgroundResource(R.drawable.home_icon_gold);
							a12.setBackgroundResource(R.drawable.board_icon_select_gold);
							a13.setBackgroundResource(R.drawable.find_icon_gold);
							a14.setBackgroundResource(R.drawable.setting_icon_gold);
							break;
						case 2:
							a11.setBackgroundResource(R.drawable.home_icon_gold);
							a12.setBackgroundResource(R.drawable.board_icon_gold);
							a13.setBackgroundResource(R.drawable.find_icon_select_gold);
							a14.setBackgroundResource(R.drawable.setting_icon_gold);
							break;
						case 3:
							a11.setBackgroundResource(R.drawable.home_icon_gold);
							a12.setBackgroundResource(R.drawable.board_icon_gold);
							a13.setBackgroundResource(R.drawable.find_icon_gold);
							a14.setBackgroundResource(R.drawable.setting_icon_select_gold);
							break;
						}
						
						break;
					}
				}		
				prevtab = tabhost1.getCurrentTab();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float distance = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastPoint.x = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			distance = mLastPoint.x - event.getX();
			break;
		}

		if (Math.abs(distance) < 100) {
			return false;
		}

		if (distance > 0) {
			int index = tabhost1.getCurrentTab() + 1;
			if (index == 4)
				index = 0;
			tabhost1.setCurrentTab(index);
			tabhost1.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.left_in));
		} else {
			int index = tabhost1.getCurrentTab() - 1;
			if (index == -1)
				index = 3;
			tabhost1.setCurrentTab(index);
			tabhost1.getCurrentView().startAnimation(
					AnimationUtils.loadAnimation(getApplicationContext(),
							R.anim.right_in));
		}
		return true;
	}
}
