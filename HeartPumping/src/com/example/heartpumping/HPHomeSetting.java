package com.example.heartpumping;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HPHomeSetting extends Activity {

	private Intent result;
	private BackPressCloseHandler back;
	Button btn1, btn2, btn3;

	LinearLayout li;

	HPDatabase hpdb;
	Cursor c;
	SQLiteDatabase sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpsetting);
		li = (LinearLayout) findViewById(R.id.lihpsetting);
		btn1 = (Button) findViewById(R.id.btnpersonal);
		btn2 = (Button) findViewById(R.id.btntema);
		btn3 = (Button) findViewById(R.id.btnhelp);
		
		
		hpdb = new HPDatabase(HPHomeSetting.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);

		if (c.moveToFirst()) {
			switch (c.getInt(6)) {
			case 0:
				li.setBackgroundResource(R.drawable.setting_blue);
				btn1.setBackgroundResource(R.drawable.btnmyinfo_blue);
				btn2.setBackgroundResource(R.drawable.btntema_blue);
				btn3.setBackgroundResource(R.drawable.btnhelp_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.setting_pupple);
				btn1.setBackgroundResource(R.drawable.btnmyinfo_pupple);
				btn2.setBackgroundResource(R.drawable.btntema_pupple);
				btn3.setBackgroundResource(R.drawable.btnhelp_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.setting_green);
				btn1.setBackgroundResource(R.drawable.btnmyinfo_green);
				btn2.setBackgroundResource(R.drawable.btntema_green);
				btn3.setBackgroundResource(R.drawable.btnhelp_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.setting_red);
				btn1.setBackgroundResource(R.drawable.btnmyinfo_red);
				btn2.setBackgroundResource(R.drawable.btntema_red);
				btn3.setBackgroundResource(R.drawable.btnhelp_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.setting_gold);
				btn1.setBackgroundResource(R.drawable.btnmyinfo_gold);
				btn2.setBackgroundResource(R.drawable.btntema_gold);
				btn3.setBackgroundResource(R.drawable.btnhelp_gold);
				break;
			}
		}
		

		back = new BackPressCloseHandler(this);

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				Intent intent = new Intent(HPHomeSetting.this,
						HPSetPersonal.class);
				startActivityForResult(intent, 0);
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPHomeSetting.this, HPSetTema.class);
				startActivityForResult(intent, 1);
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPHomeSetting.this, HPSetHelp.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO 자동 생성된 메소드 스텁
		if (back.onBackPressed()) {
			result = new Intent();
			result.putExtra("finish", "finish");
			setResult(0, result);
			super.onBackPressed();
		}
	}

	class BackPressCloseHandler {
		private long backKeyPressedTime = 0;
		private Toast toast;

		private Activity activity;

		public BackPressCloseHandler(Activity context) {
			this.activity = context;
		}

		public boolean onBackPressed() {
			if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
				backKeyPressedTime = System.currentTimeMillis();
				showGuide();
				return false;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
				toast.cancel();
				return true;
			}
			return false;
		}

		public void showGuide() {
			toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 자동 생성된 메소드 스텁
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 0) {
				if (data != null) {
					result = new Intent();
					setResult(1, result);
					finish();
				}
			}
		} else if (requestCode == 1) {
			if (data != null) {
				int flag = data.getIntExtra("flag", 0);

				switch (flag) {
				case 0:
					li.setBackgroundResource(R.drawable.setting_blue);
					btn1.setBackgroundResource(R.drawable.btnmyinfo_blue);
					btn2.setBackgroundResource(R.drawable.btntema_blue);
					btn3.setBackgroundResource(R.drawable.btnhelp_blue);
					break;
				case 1:
					li.setBackgroundResource(R.drawable.setting_pupple);
					btn1.setBackgroundResource(R.drawable.btnmyinfo_pupple);
					btn2.setBackgroundResource(R.drawable.btntema_pupple);
					btn3.setBackgroundResource(R.drawable.btnhelp_pupple);
					
					break;
				case 2:
					break;
				case 3:
					break;
				}
				HPHome.tabhost1.setCurrentTab(2);
				HPHome.tabhost1.setCurrentTab(3);
			}
		}
	}
}
