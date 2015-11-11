package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
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

public class HPHomeProFModify extends Activity {

	EditText et;
	Button btn, btn2;
	Intent intent, result;
	
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;
    HPObject inhpmy;
    
    LinearLayout li;
    
    HPDatabase hpdb;
    SQLiteDatabase sql;
    Cursor c;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 자동 생성된 메소드 스텁
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpmodify);
		li = (LinearLayout)findViewById(R.id.lihpmodify);
		btn = (Button)findViewById(R.id.button1);
		btn2 = (Button)findViewById(R.id.button2);
		
		hpdb = new HPDatabase(HPHomeProFModify.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.changelan_blue);
				btn.setBackgroundResource(R.drawable.btnsure_blue);
				btn2.setBackgroundResource(R.drawable.btnerase_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.changelan_pupple);
				btn.setBackgroundResource(R.drawable.btnsure_pupple);
				btn2.setBackgroundResource(R.drawable.btnerase_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.changelan_green);
				btn.setBackgroundResource(R.drawable.btnsure_green);
				btn2.setBackgroundResource(R.drawable.btnerase_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.changelan_red);
				btn.setBackgroundResource(R.drawable.btnsure_red);
				btn2.setBackgroundResource(R.drawable.btnerase_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.changelan_gold);
				btn.setBackgroundResource(R.drawable.btnsure_gold);
				btn2.setBackgroundResource(R.drawable.btnerase_gold);
				break;
			}
		}
		
		intent = getIntent();
		et = (EditText)findViewById(R.id.editText1);
		
		et.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(50) });
		et.setText(intent.getStringExtra("message"));
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				et.setText("");
			}
		});
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				
				if (!MainActivity.connectthread.channel.isConnected()) {
					if (!MainActivity.connectthread.isAlive()) {
						MainActivity.connectthread = new HPConnectThread(
								MainActivity.context);
						MainActivity.connectthread.start();
					} else {
						if (MainActivity.connectthread.isInterrupted()) {
							MainActivity.connectthread = new HPConnectThread(
									MainActivity.context);
							MainActivity.connectthread.start();
						} else {
							MainActivity.connectthread.interrupt();
							MainActivity.connectthread = new HPConnectThread(
									MainActivity.context);
							MainActivity.connectthread.start();
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

							HPObject hp = new HPObject(MainActivity.Loginid, null, et.getText().toString(),
									null, null, null, null,
									null, null, null,null,
									"messagemodify");

							Object obc = hp;

							out.writeObject(obc);

							if (MainActivity.connectthread.channel
									.isConnected()) {
								MainActivity.connectthread.channel
										.write(ByteBuffer.wrap(baos
												.toByteArray()));
							} else {
								handler.sendEmptyMessage(2);
								handler2.sendEmptyMessageDelayed(4,
										1000);
								Hpthread.interrupt();
								return;
							}
							handler2.sendEmptyMessageDelayed(1, 0);
							
							result = new Intent();
							result.putExtra("message",et.getText().toString());
							setResult(1,result);
							finish();
							Hpthread.interrupt();
							HPHomeProFModify.this.onDestroy();
						} catch (IOException e) {
							// TODO 자동 생성된 catch 블록
							if (MainActivity.connectthread.channel
									.isConnected()) {
								Hpthread.interrupt();
								MainActivity.connectthread.interrupt();
								MainActivity.connectthread = new HPConnectThread(
										MainActivity.context);
								MainActivity.connectthread.start();
								handler2.sendEmptyMessage(3);
							}
						}
					}
				});
				Hpthread.start();
				
			}
		});
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;
			case 1:
				break;
			case 2:
				break;
			}
		}
	};

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPHomeProFModify.this, "네트워크 상태가 원활하지 않습니다.",
						0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				/*
				 * mtoast = Toast.makeText(HPHomeHome.this, "일치하는 정보가 없습니다.",
				 * 0); mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				 * mtoast.show(); arrayadapter = new MyAdapter(HPHomeHome.this,
				 * android.R.layout.simple_list_item_1, arraylist);
				 * listview.setAdapter(arrayadapter);
				 * arrayadapter.notifyDataSetChanged();
				 */
				break;
			case 2:
				break;
			}
		}
	};
}
