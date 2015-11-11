package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btn1, btn2, btn3;
	EditText et1, et2;
	private Intent intent;

	public static SocketChannel channel;
	public static String Loginid;
	public static String Loginpw;
	Selector selector;
	ByteArrayOutputStream baos;
	ByteArrayInputStream bais;
	ObjectOutputStream out;
	ObjectInputStream ois;
	public static HPConnectThread connectthread;

	Cursor c;
	HPDatabase hpdb;
	SQLiteDatabase sql;

	Thread Hpthread;
	String logincheck;
	Toast mtoast;
	public static Context context;
	
	LinearLayout li;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hplogin);
		li = (LinearLayout)findViewById(R.id.lihplogin);
		btn1 = (Button) findViewById(R.id.btnlogin);
		btn2 = (Button) findViewById(R.id.btnmakemember);
		btn3 = (Button) findViewById(R.id.btnfindidpw);
		hpdb = new HPDatabase(MainActivity.this);
		sql = hpdb.getReadableDatabase();
		context = getApplicationContext();
		connectthread = new HPConnectThread(MainActivity.this);
		
		
		startActivityForResult(new Intent(MainActivity.this,
				HPLoadingSplash.class), 3);
		
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.login_blue);
				btn1.setBackgroundResource(R.drawable.btnlogin_blue);
				btn2.setBackgroundResource(R.drawable.btnaddmember_blue);
				btn3.setBackgroundResource(R.drawable.btnfindidpw_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.login_pupple);
				btn1.setBackgroundResource(R.drawable.btnlogin_pupple);
				btn2.setBackgroundResource(R.drawable.btnaddmember_pupple);
				btn3.setBackgroundResource(R.drawable.btnfindidpw_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.login_green);
				btn1.setBackgroundResource(R.drawable.btnlogin_green);
				btn2.setBackgroundResource(R.drawable.btnaddmember_green);
				btn3.setBackgroundResource(R.drawable.btnfindidpw_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.login_red);
				btn1.setBackgroundResource(R.drawable.btnlogin_red);
				btn2.setBackgroundResource(R.drawable.btnaddmember_red);
				btn3.setBackgroundResource(R.drawable.btnfindidpw_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.login_gold);
				btn1.setBackgroundResource(R.drawable.btnlogin_gold);
				btn2.setBackgroundResource(R.drawable.btnaddmember_gold);
				btn3.setBackgroundResource(R.drawable.btnfindidpw_gold);
				break;
			}
		}
		
		et1 = (EditText) findViewById(R.id.etloginid);
		et2 = (EditText) findViewById(R.id.etloginpw);

		et1.setFilters(new InputFilter[] { HPNewMember.IDFilter,
				new InputFilter.LengthFilter(8) });
		et2.setFilters(new InputFilter[] { HPNewMember.pwFilter,
				new InputFilter.LengthFilter(8) });
		
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et1.getText().toString().isEmpty()
						|| et2.getText().toString().isEmpty()) {
					mtoast = Toast.makeText(MainActivity.this, "전부 입력해주세요", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
					return;
				}
				
				handler.sendEmptyMessage(0);

				if (!connectthread.channel.isConnected()) {
					if (!connectthread.isAlive()) {
						connectthread = new HPConnectThread(MainActivity.this);
						connectthread.start();
					} else {
						if (connectthread.isInterrupted()) {
							connectthread = new HPConnectThread(
									MainActivity.this);
							connectthread.start();
						} else {
							connectthread.interrupt();
							connectthread = new HPConnectThread(
									MainActivity.this);
							connectthread.start();
						}
					}
				}

				if (Hpthread != null && Hpthread.isAlive()) {
					Hpthread.interrupt();
				}

				Hpthread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO 자동 생성된 메소드 스텁
						try {
							baos = new ByteArrayOutputStream();
							out = new ObjectOutputStream(baos);

							HPObject hp = new HPObject(
									et1.getText().toString(), et2.getText()
											.toString(), null, null, null,
									null, null, null, null, null, null, "login");

							Object obc = hp;

							out.writeObject(obc);
							if (connectthread.channel.isConnected()) {
								connectthread.channel.write(ByteBuffer
										.wrap(baos.toByteArray()));
							} else {
								handler.sendEmptyMessage(2);
								handler2.sendEmptyMessageDelayed(0, 1000);
							}

							ByteBuffer data = ByteBuffer.allocate(1024);
							connectthread.selector.select();
							connectthread.channel.read(data);
							bais = new ByteArrayInputStream(data.array());

							ois = new ObjectInputStream(bais);

							HPObject inhp = (HPObject) ois.readObject();

							logincheck = inhp.message;
							handler2.sendEmptyMessageDelayed(0, 1000);
							if (logincheck.equals("truelogin")) {
								Loginid = et1.getText().toString();
								Loginpw = et2.getText().toString();
								intent = new Intent(MainActivity.this,
										HPHome.class);
								startActivityForResult(intent, 0);
								sql.execSQL("update personal set loginid = '"
										+ Loginid + "', loginpw ='" + Loginpw
										+ "' where id='set'");
								Hpthread.interrupt();
							} else {
								Loginid = "";
								Loginpw = "";
								Hpthread.interrupt();
								handler.sendEmptyMessage(3);
							}

						} catch (Exception e) {
							// TODO 자동 생성된 catch 블록
							if (connectthread.channel.isConnected()) {
								Hpthread.interrupt();
								connectthread.interrupt();
								connectthread = new HPConnectThread(
										MainActivity.this);
								connectthread.start();
								handler.sendEmptyMessage(1);
							}
						}
					}
				});
				Hpthread.start();

			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				intent = new Intent(MainActivity.this, HPNewMember.class);
				startActivity(intent);
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(MainActivity.this, HPFindLogin.class);
				startActivity(intent);
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn1.setEnabled(false);
				break;
			case 1:
				btn1.performClick();
				break;
			case 2:
				mtoast = Toast.makeText(MainActivity.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(MainActivity.this,
						"로그인 정보가 일치하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			}
		}
	};

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn1.setEnabled(true);
				break;
			case 1:
				btn1.performClick();
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		// TODO 자동 생성된 메소드 스텁
		if (connectthread.channel.isConnected()) {
			if (Hpthread != null && Hpthread.isAlive()) {
				Hpthread.interrupt();
			}
			Hpthread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO 자동 생성된 메소드 스텁
					try {

						baos = new ByteArrayOutputStream();
						out = new ObjectOutputStream(baos);
						HPObject hp = new HPObject(null, null, null, null,
								null, null, null, null, null, null, null,
								"loginbye");

						Object obc = hp;

						out.writeObject(obc);
						connectthread.channel.write(ByteBuffer.wrap(baos
								.toByteArray()));
						connectthread.channel.close();
						connectthread.selector.close();
						connectthread.interrupt();
						Hpthread.interrupt();
					} catch (IOException e) {
						// TODO 자동 생성된 catch 블록
						e.printStackTrace();
					}
				}
			});
			Hpthread.start();
		}
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 자동 생성된 메소드 스텁
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			if (requestCode == 0) {

				MainActivity.this.onBackPressed();
			}
			else if(requestCode == 3)
			{
				if (c.moveToFirst()) {
					if (c.getInt(1) == 1) {
						et1.setText(c.getString(4));
						et2.requestFocus();
					}

					if (c.getInt(2) == 1 && c.getInt(1) == 1) {
						et1.setText(c.getString(4));
						et2.setText(c.getString(5));
						handler2.sendEmptyMessage(1);
					}
				}
				
			}
		}
	}

}
