package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class HPFindLogin extends Activity{

	LinearLayout li;
	
	Button btn1,btn2,btn3;
	EditText et1,et2,et3,et4;
	ViewFlipper filp;
	
	int activityflag;
	Toast mtoast;
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	String foundid, foundpw;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpfindidpw);
		
		btn1 = (Button)findViewById(R.id.btnfindnext);
		btn2 = (Button)findViewById(R.id.btnfindid);
		btn3 = (Button)findViewById(R.id.btnfindpw);
		et1 = (EditText)findViewById(R.id.etfindidnic);
		et2 = (EditText)findViewById(R.id.etfindidphone);
		et3 = (EditText)findViewById(R.id.etfindpwid);
		et4 = (EditText)findViewById(R.id.etfindpwphone);
		filp = (ViewFlipper)findViewById(R.id.viewFlipper1);
		li = (LinearLayout) findViewById(R.id.lifindidpw);
		
		hpdb = new HPDatabase(HPFindLogin.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.findid_blue);
				btn1.setBackgroundResource(R.drawable.btnmisspw_blue);
				btn2.setBackgroundResource(R.drawable.btnfindid_blue);
				btn3.setBackgroundResource(R.drawable.btnfindpw_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.findid_pupple);
				btn1.setBackgroundResource(R.drawable.btnmisspw_pupple);
				btn2.setBackgroundResource(R.drawable.btnfindid_pupple);
				btn3.setBackgroundResource(R.drawable.btnfindpw_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.findid_green);
				btn1.setBackgroundResource(R.drawable.btnmisspw_green);
				btn2.setBackgroundResource(R.drawable.btnfindid_green);
				btn3.setBackgroundResource(R.drawable.btnfindpw_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.findid_red);
				btn1.setBackgroundResource(R.drawable.btnmisspw_red);
				btn2.setBackgroundResource(R.drawable.btnfindid_red);
				btn3.setBackgroundResource(R.drawable.btnfindpw_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.findid_gold);
				btn1.setBackgroundResource(R.drawable.btnmisspw_gold);
				btn2.setBackgroundResource(R.drawable.btnfindid_gold);
				btn3.setBackgroundResource(R.drawable.btnfindpw_gold);
				break;
			}
		}
		
		
		et1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(8) });
		et3.setFilters(new InputFilter[] { HPNewMember.IDFilter,
				new InputFilter.LengthFilter(6) });
		//btn1.setText("비밀번호가 기억이 안나세요?");
		
		activityflag = 0;
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(activityflag)
				{
				case 0:
					activityflag = 1;
					filp.showNext();
					//btn1.setText("아이디가 기억이 안나세요?");
					
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							li.setBackgroundResource(R.drawable.findpw_blue);
							btn1.setBackgroundResource(R.drawable.btnmissid_blue);
							break;
						case 1:
							li.setBackgroundResource(R.drawable.findpw_pupple);
							btn1.setBackgroundResource(R.drawable.btnmissid_pupple);
							break;
						case 2:
							li.setBackgroundResource(R.drawable.findpw_green);
							btn1.setBackgroundResource(R.drawable.btnmissid_green);
							break;
						case 3:
							li.setBackgroundResource(R.drawable.findpw_red);
							btn1.setBackgroundResource(R.drawable.btnmissid_red);
							break;
						case 4:
							li.setBackgroundResource(R.drawable.findpw_gold);
							btn1.setBackgroundResource(R.drawable.btnmissid_gold);
							break;
						}
					}
					break;
				case 1:
					activityflag = 0;
					filp.showPrevious();
					//btn1.setText("비밀번호가 기억이 안나세요?");
					
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							li.setBackgroundResource(R.drawable.findid_blue);
							btn1.setBackgroundResource(R.drawable.btnmisspw_blue);
							break;
						case 1:
							li.setBackgroundResource(R.drawable.findid_pupple);
							btn1.setBackgroundResource(R.drawable.btnmisspw_pupple);
							break;
						case 2:
							li.setBackgroundResource(R.drawable.findid_green);
							btn1.setBackgroundResource(R.drawable.btnmisspw_green);
							break;
						case 3:
							li.setBackgroundResource(R.drawable.findid_red);
							btn1.setBackgroundResource(R.drawable.btnmisspw_red);
							break;
						case 4:
							li.setBackgroundResource(R.drawable.findid_gold);
							btn1.setBackgroundResource(R.drawable.btnmisspw_gold);
							break;
						}
					}
					break;
				}
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!et1.getText().toString().isEmpty() && !et2.getText().toString().isEmpty())
				{
					handler.sendEmptyMessage(0);
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

								HPObject hp = new HPObject(null, null, et1.getText().toString(), null,
										null, null, et2.getText().toString(),
										null, null, null,null,"findid");

								Object obc = hp;

								out.writeObject(obc);

								if (MainActivity.connectthread.channel
										.isConnected()) {
									MainActivity.connectthread.channel
											.write(ByteBuffer.wrap(baos
													.toByteArray()));
								} else {
									handler2.sendEmptyMessage(0);
									handler.sendEmptyMessageDelayed(1, 1000);
								}

								ByteBuffer data = ByteBuffer.allocate(1024);
								MainActivity.connectthread.selector.select();
								MainActivity.connectthread.channel.read(data);

								bais = new ByteArrayInputStream(data.array());

								ois = new ObjectInputStream(bais);

								HPObject inhp = (HPObject) ois.readObject();
								foundid = inhp.message;
								Log.e("id", foundid);
								if(inhp.message.equals("falsefindid"))
								{
									handler2.sendEmptyMessage(1);
								}
								else
								{
									handler2.sendEmptyMessage(2);
								}
								Hpthread.interrupt();
								handler.sendEmptyMessageDelayed(1, 1000);
							} catch (Exception e) {
								// TODO 자동 생성된 catch 블록
								if (MainActivity.connectthread.channel
										.isConnected()) {
									Hpthread.interrupt();
									MainActivity.connectthread.interrupt();
									MainActivity.connectthread = new HPConnectThread(
											MainActivity.context);
									MainActivity.connectthread.start();
									handler.sendEmptyMessage(2);
								}
							}

						}
					});
					Hpthread.start();
				}
				else
				{
					mtoast = Toast.makeText(HPFindLogin.this, "전부 입력해주세요!", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
			}
		});
		
		btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!et3.getText().toString().isEmpty() && !et4.getText().toString().isEmpty())
				{
					handler.sendEmptyMessage(3);
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

								HPObject hp = new HPObject(et3.getText().toString(), null, null, null,
										null, null, et4.getText().toString(),
										null,null, null, null, "findpw");

								Object obc = hp;

								out.writeObject(obc);

								if (MainActivity.connectthread.channel
										.isConnected()) {
									MainActivity.connectthread.channel
											.write(ByteBuffer.wrap(baos
													.toByteArray()));
								} else {
									handler2.sendEmptyMessage(0);
									handler.sendEmptyMessageDelayed(4, 1000);
								}

								ByteBuffer data = ByteBuffer.allocate(1024);
								MainActivity.connectthread.selector.select();
								MainActivity.connectthread.channel.read(data);

								bais = new ByteArrayInputStream(data.array());

								ois = new ObjectInputStream(bais);

								HPObject inhp = (HPObject) ois.readObject();
								foundpw = inhp.message;
								if(inhp.message.equals("falsefindpw"))
								{
									handler2.sendEmptyMessage(1);
								}
								else
								{
									handler2.sendEmptyMessage(3);
								}
								Hpthread.interrupt();
								handler.sendEmptyMessageDelayed(4, 1000);
							} catch (Exception e) {
								// TODO 자동 생성된 catch 블록
								if (MainActivity.connectthread.channel
										.isConnected()) {
									Hpthread.interrupt();
									MainActivity.connectthread.interrupt();
									MainActivity.connectthread = new HPConnectThread(
											MainActivity.context);
									MainActivity.connectthread.start();
									handler.sendEmptyMessage(5);
								}
							}

						}
					});
					Hpthread.start();
				}
				else
				{
					mtoast = Toast.makeText(HPFindLogin.this, "전부 입력해주세요!", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
			}
		});
	}
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				btn2.setEnabled(false);
				break;
			case 1:
				btn2.setEnabled(true);
				break;
			case 2:
				btn2.performClick();
				break;
			case 3:
				btn3.setEnabled(false);
				break;
			case 4:
				btn3.setEnabled(true);
				break;
			case 5:
				btn3.performClick();
				break;
			}
		}
	};
	
	Handler handler2 = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				mtoast = Toast.makeText(HPFindLogin.this, "네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPFindLogin.this, "일치하는 정보가 없습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 2:
				mtoast = Toast.makeText(HPFindLogin.this, "찾고자 하는 아이디는 \n" + foundid, 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(HPFindLogin.this, "찾고자 하는 암호는 \n" + foundpw, 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			}
		}
	};
}
