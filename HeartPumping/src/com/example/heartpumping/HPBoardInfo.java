package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HPBoardInfo extends Activity {

	EditText et1, et2, et3, et4;
	Button btn1, btn2;
	Intent intent;
	String written;
	String writtennic;
	String subject;
	String content;
	String meetzone;
	String peoplecount;
	String boardid;

	private Intent result;
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;
	HPObject inhpmy;

	String sex;
	String univ_1;
	String phone;
	String heartpoint;
	int flag;
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �ڵ� ������ �޼ҵ� ����
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpboardinfo);
		li = (LinearLayout)findViewById(R.id.lihpboardinfo);
		btn1 = (Button) findViewById(R.id.boardinfobtn1);
		btn2 = (Button) findViewById(R.id.boardinfobtn2);
		
		hpdb = new HPDatabase(HPBoardInfo.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.boardinfo_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.boardinfo_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.boardinfo_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.boardinfo_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.boardinfo_gold);
				break;
			}
		}
		
		intent = getIntent();
		et1 = (EditText) findViewById(R.id.boardinfoet1);
		et2 = (EditText) findViewById(R.id.boardinfoet2);
		et3 = (EditText) findViewById(R.id.boardinfoet3);
		et4 = (EditText) findViewById(R.id.boardinfoet4);
		

		written = intent.getStringExtra("written");
		writtennic = intent.getStringExtra("writtennic");
		subject = intent.getStringExtra("subject");
		content = intent.getStringExtra("content");
		meetzone = intent.getStringExtra("meetzone");
		peoplecount = intent.getStringExtra("peoplecount");
		boardid = intent.getStringExtra("boardid");
		flag = intent.getIntExtra("flag", 2);
		SetBoardInfo();
		switch(flag)
		{
		case 0:
			//btn1.setText("�Խ��� ���� ����");
			//btn2.setText("����ϱ�");
			if(c.moveToFirst())
			{
				switch(c.getInt(6))
				{
				case 0:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_blue);
					btn2.setBackgroundResource(R.drawable.btncancel_blue);
					break;
				case 1:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_pupple);
					btn2.setBackgroundResource(R.drawable.btncancel_pupple);
					break;
				case 2:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_green);
					btn2.setBackgroundResource(R.drawable.btncancel_green);
					break;
				case 3:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_red);
					btn2.setBackgroundResource(R.drawable.btncancel_red);
					break;
				case 4:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_gold);
					btn2.setBackgroundResource(R.drawable.btncancel_gold);
					break;
				}
			}
			
			
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout abc = (LinearLayout) vi.inflate(
							R.layout.hpfindfriendlist, null);
					abc.setOrientation(LinearLayout.VERTICAL);

					TextView nic = (TextView) abc.findViewById(R.id.textView1);
					TextView sex = (TextView) abc.findViewById(R.id.textView2);
					TextView univ_1 = (TextView) abc.findViewById(R.id.textView3);
					TextView phone = (TextView) abc.findViewById(R.id.textView4);
					TextView point = (TextView) abc.findViewById(R.id.textView5);

					nic.setTextSize(25);
					sex.setTextSize(25);
					univ_1.setTextSize(25);
					phone.setTextSize(25);
					point.setTextSize(25);
					String sexstr = "";

					if (inhpmy.Sex.equals("0")) {
						sexstr = "����";
					} else {
						sexstr = "����";
					}

					nic.setText("�г��� : " + inhpmy.message);
					sex.setText("���� : " + sexstr);
					univ_1.setText("���б� : " + inhpmy.Univ_1 + "���б�");
					phone.setText("����ó : " + inhpmy.Phone);
					point.setText("�αٵα� : " + inhpmy.HeartPoint + " ��");	
					
					AlertDialog dia1 = new AlertDialog.Builder(
							HPBoardInfo.this).create();
					dia1.setTitle("�Խ��� ����");
					dia1.setView(abc);
					dia1.setCancelable(true);
					dia1.setCanceledOnTouchOutside(true);
					dia1.show();
				}
			});
			
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					AlertDialog.Builder dia1 = new AlertDialog.Builder(
							HPBoardInfo.this);
					dia1.setTitle("�˸�");
					dia1.setMessage("���������� ����Ͻðڽ��ϱ�?");
					dia1.setCancelable(true);
					dia1.setPositiveButton("��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �ڵ� ������ �޼ҵ� ����
							if (!MainActivity.connectthread.channel
									.isConnected()) {
								if (!MainActivity.connectthread.isAlive()) {
									MainActivity.connectthread = new HPConnectThread(
											MainActivity.context);
									MainActivity.connectthread.start();
								} else {
									if (MainActivity.connectthread
											.isInterrupted()) {
										MainActivity.connectthread = new HPConnectThread(
												MainActivity.context);
										MainActivity.connectthread.start();
									} else {
										MainActivity.connectthread
												.interrupt();
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
									// TODO �ڵ� ������ �޼ҵ� ����
									try {

										baos = new ByteArrayOutputStream();
										out = new ObjectOutputStream(baos);

										HPObject hp = new HPObject(
												MainActivity.Loginid,
												boardid, "cancel", null,
												null, null, null, null,
												null, null, null,
												"oknoreqboard");

										Object obc = hp;

										out.writeObject(obc);

										if (MainActivity.connectthread.channel
												.isConnected()) {
											MainActivity.connectthread.channel.write(ByteBuffer
													.wrap(baos
															.toByteArray()));
										} else {
											handler2.sendEmptyMessage(0);
										}

										ByteBuffer data = ByteBuffer
												.allocate(1024);
										MainActivity.connectthread.selector
												.select();
										MainActivity.connectthread.channel
												.read(data);

										bais = new ByteArrayInputStream(
												data.array());

										ois = new ObjectInputStream(bais);

										inhp = (HPObject) ois.readObject();

										if (inhp.message
												.equals("falsereqboard")) {
											 handler.sendEmptyMessage(0);
										} else {
											handler2.sendEmptyMessage(1);
										}
									} catch (Exception e) {

									}
								}
							});

							Hpthread.start();
						}
						
					});
					dia1.setNegativeButton("�ƴϿ�", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �ڵ� ������ �޼ҵ� ����
							dialog.dismiss();
						}
					});
					
					dia1.show();
				}
			});
			break;
		case 1:
			//btn1.setText("�����ϱ�");
			//btn2.setText("�����ϱ�");
			
			if(c.moveToFirst())
			{
				switch(c.getInt(6))
				{
				case 0:
					btn1.setBackgroundResource(R.drawable.btndelete_blue);
					btn2.setBackgroundResource(R.drawable.btnmodify_blue);
					break;
				case 1:
					btn1.setBackgroundResource(R.drawable.btndelete_pupple);
					btn2.setBackgroundResource(R.drawable.btnmodify_pupple);
					break;
				case 2:
					btn1.setBackgroundResource(R.drawable.btndelete_green);
					btn2.setBackgroundResource(R.drawable.btnmodify_green);
					break;
				case 3:
					btn1.setBackgroundResource(R.drawable.btndelete_red);
					btn2.setBackgroundResource(R.drawable.btnmodify_red);
					break;
				case 4:
					btn1.setBackgroundResource(R.drawable.btndelete_gold);
					btn2.setBackgroundResource(R.drawable.btnmodify_gold);
					break;
				}
			}
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					AlertDialog.Builder dia1 = new AlertDialog.Builder(
							HPBoardInfo.this);
					dia1.setTitle("�˸�");
					dia1.setMessage("�Խù��� �����Ͻðڽ��ϱ�?");
					dia1.setCancelable(true);
					dia1.setPositiveButton("��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �ڵ� ������ �޼ҵ� ����
							if (!MainActivity.connectthread.channel
									.isConnected()) {
								if (!MainActivity.connectthread.isAlive()) {
									MainActivity.connectthread = new HPConnectThread(
											MainActivity.context);
									MainActivity.connectthread.start();
								} else {
									if (MainActivity.connectthread
											.isInterrupted()) {
										MainActivity.connectthread = new HPConnectThread(
												MainActivity.context);
										MainActivity.connectthread.start();
									} else {
										MainActivity.connectthread
												.interrupt();
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
									// TODO �ڵ� ������ �޼ҵ� ����
									try {

										baos = new ByteArrayOutputStream();
										out = new ObjectOutputStream(baos);

										HPObject hp = new HPObject(
												MainActivity.Loginid,
												boardid, "delete", null,
												null, null, null, null,
												null, null, null,
												"oknoreqboard");

										Object obc = hp;

										out.writeObject(obc);

										if (MainActivity.connectthread.channel
												.isConnected()) {
											MainActivity.connectthread.channel.write(ByteBuffer
													.wrap(baos
															.toByteArray()));
										} else {
											handler2.sendEmptyMessage(0);
										}

										ByteBuffer data = ByteBuffer
												.allocate(1024);
										MainActivity.connectthread.selector
												.select();
										MainActivity.connectthread.channel
												.read(data);

										bais = new ByteArrayInputStream(
												data.array());

										ois = new ObjectInputStream(bais);

										inhp = (HPObject) ois.readObject();

										if (inhp.message
												.equals("falsereqboard")) {
											 handler.sendEmptyMessage(0);
										} else {
											handler2.sendEmptyMessage(1);
										}
									} catch (Exception e) {

									}
								}
							});

							Hpthread.start();
						}
						
					});
					dia1.setNegativeButton("�ƴϿ�", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �ڵ� ������ �޼ҵ� ����
							dialog.dismiss();
						}
					});
					
					dia1.show();
				}
			});
			
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					Intent intent = new Intent(HPBoardInfo.this, HPHomeMakeBoard.class);
					intent.putExtra("subject", subject);
					intent.putExtra("content", content);
					intent.putExtra("meetzone", meetzone);
					intent.putExtra("flag", 1);
					startActivityForResult(intent, 0);
				}
			});
			break;
		case 2:
			if(c.moveToFirst())
			{
				switch(c.getInt(6))
				{
				case 0:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_blue);
					btn2.setBackgroundResource(R.drawable.btnagree_blue);
					break;
				case 1:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_pupple);
					btn2.setBackgroundResource(R.drawable.btnagree_pupple);
					break;
				case 2:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_green);
					btn2.setBackgroundResource(R.drawable.btnagree_green);
					break;
				case 3:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_red);
					btn2.setBackgroundResource(R.drawable.btnagree_red);
					break;
				case 4:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_gold);
					btn2.setBackgroundResource(R.drawable.btnagree_gold);
					break;
				}
			}
			
			btn1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout abc = (LinearLayout) vi.inflate(
							R.layout.hpfindfriendlist, null);
					abc.setOrientation(LinearLayout.VERTICAL);

					TextView nic = (TextView) abc.findViewById(R.id.textView1);
					TextView sex = (TextView) abc.findViewById(R.id.textView2);
					TextView univ_1 = (TextView) abc.findViewById(R.id.textView3);
					TextView phone = (TextView) abc.findViewById(R.id.textView4);
					TextView point = (TextView) abc.findViewById(R.id.textView5);

					nic.setTextSize(25);
					sex.setTextSize(25);
					univ_1.setTextSize(25);
					phone.setTextSize(25);
					point.setTextSize(25);
					String sexstr = "";

					if (inhpmy.Sex.equals("0")) {
						sexstr = "����";
					} else {
						sexstr = "����";
					}

					nic.setText("�г��� : " + inhpmy.message);
					sex.setText("���� : " + sexstr);
					univ_1.setText("���б� : " + inhpmy.Univ_1 + "���б�");
					phone.setText("����ó : " + inhpmy.Phone);
					point.setText("�αٵα� : " + inhpmy.HeartPoint + " ��");	
					
					AlertDialog dia1 = new AlertDialog.Builder(
							HPBoardInfo.this).create();
					dia1.setTitle("�Խ��� ����");
					dia1.setView(abc);
					dia1.setCancelable(true);
					dia1.setCanceledOnTouchOutside(true);
					dia1.show();
				}
			});

			btn2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					if (!MainActivity.connectthread.channel
							.isConnected()) {
						if (!MainActivity.connectthread.isAlive()) {
							MainActivity.connectthread = new HPConnectThread(
									MainActivity.context);
							MainActivity.connectthread.start();
						} else {
							if (MainActivity.connectthread
									.isInterrupted()) {
								MainActivity.connectthread = new HPConnectThread(
										MainActivity.context);
								MainActivity.connectthread.start();
							} else {
								MainActivity.connectthread
										.interrupt();
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
							// TODO �ڵ� ������ �޼ҵ� ����
							try {

								baos = new ByteArrayOutputStream();
								out = new ObjectOutputStream(baos);

								HPObject hp = new HPObject(
										MainActivity.Loginid,
										boardid, "ok", null,
										null, null, null, null,
										null, null, null,
										"oknoreqboard");

								Object obc = hp;

								out.writeObject(obc);

								if (MainActivity.connectthread.channel
										.isConnected()) {
									MainActivity.connectthread.channel.write(ByteBuffer
											.wrap(baos
													.toByteArray()));
								} else {
									handler2.sendEmptyMessage(0);
								}

								ByteBuffer data = ByteBuffer
										.allocate(1024);
								MainActivity.connectthread.selector
										.select();
								MainActivity.connectthread.channel
										.read(data);

								bais = new ByteArrayInputStream(
										data.array());

								ois = new ObjectInputStream(bais);

								inhp = (HPObject) ois.readObject();

								if (inhp.message
										.equals("falsereqboard")) {
									 handler.sendEmptyMessage(0);
								} else {
									handler2.sendEmptyMessage(1);
								}
							} catch (Exception e) {

							}
						}
					});

					Hpthread.start();
				}
			});
			break;
		case 3:
			//btn1.setText("�Խ��� ���� ����");
			//btn2.setText("��û�ϱ�");
			if(c.moveToFirst())
			{
				switch(c.getInt(6))
				{
				case 0:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_blue);
					btn2.setBackgroundResource(R.drawable.btnask_blue);
					break;
				case 1:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_pupple);
					btn2.setBackgroundResource(R.drawable.btnask_pupple);
					break;
				case 2:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_green);
					btn2.setBackgroundResource(R.drawable.btnask_green);
					break;
				case 3:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_red);
					btn2.setBackgroundResource(R.drawable.btnask_red);
					break;
				case 4:
					btn1.setBackgroundResource(R.drawable.btnwriteinfo_gold);
					btn2.setBackgroundResource(R.drawable.btnask_gold);
					break;
				}
			}
			
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout abc = (LinearLayout) vi.inflate(
							R.layout.hpfindfriendlist, null);
					abc.setOrientation(LinearLayout.VERTICAL);

					TextView nic = (TextView) abc.findViewById(R.id.textView1);
					TextView sex = (TextView) abc.findViewById(R.id.textView2);
					TextView univ_1 = (TextView) abc.findViewById(R.id.textView3);
					TextView phone = (TextView) abc.findViewById(R.id.textView4);
					TextView point = (TextView) abc.findViewById(R.id.textView5);

					nic.setTextSize(25);
					sex.setTextSize(25);
					univ_1.setTextSize(25);
					phone.setTextSize(25);
					point.setTextSize(25);
					String sexstr = "";

					if (inhpmy.Sex.equals("0")) {
						sexstr = "����";
					} else {
						sexstr = "����";
					}

					nic.setText("�г��� : " + inhpmy.message);
					sex.setText("���� : " + sexstr);
					univ_1.setText("���б� : " + inhpmy.Univ_1 + "���б�");
					phone.setText("����ó : " + inhpmy.Phone);
					point.setText("�αٵα� : " + inhpmy.HeartPoint + " ��");	
					
					AlertDialog dia1 = new AlertDialog.Builder(
							HPBoardInfo.this).create();
					dia1.setTitle("�Խ��� ����");
					dia1.setView(abc);
					dia1.setCancelable(true);
					dia1.setCanceledOnTouchOutside(true);
					dia1.show();
				}
			});
			
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO �ڵ� ������ �޼ҵ� ����
					if(peoplecount.equals("1"))
					{
						//�ٷ� ��û
						if (!MainActivity.connectthread.channel
								.isConnected()) {
							if (!MainActivity.connectthread.isAlive()) {
								MainActivity.connectthread = new HPConnectThread(
										MainActivity.context);
								MainActivity.connectthread.start();
							} else {
								if (MainActivity.connectthread
										.isInterrupted()) {
									MainActivity.connectthread = new HPConnectThread(
											MainActivity.context);
									MainActivity.connectthread.start();
								} else {
									MainActivity.connectthread
											.interrupt();
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
								// TODO �ڵ� ������ �޼ҵ� ����
								try {

									baos = new ByteArrayOutputStream();
									out = new ObjectOutputStream(baos);

									HPObject hp = new HPObject(
											MainActivity.Loginid,
											boardid, "request", "1",
											null, null, null, null,
											null, null, null,
											"reqmeetboard");

									Object obc = hp;

									out.writeObject(obc);

									if (MainActivity.connectthread.channel
											.isConnected()) {
										MainActivity.connectthread.channel.write(ByteBuffer
												.wrap(baos
														.toByteArray()));
									} else {
										handler2.sendEmptyMessage(0);
									}

									ByteBuffer data = ByteBuffer
											.allocate(1024);
									MainActivity.connectthread.selector
											.select();
									MainActivity.connectthread.channel
											.read(data);

									bais = new ByteArrayInputStream(
											data.array());

									ois = new ObjectInputStream(bais);

									inhp = (HPObject) ois.readObject();

									if (inhp.message
											.equals("falsereqboard")) {
										// handler.sendEmptyMessage(0);
									} else {
										//handler2.sendEmptyMessage(1);
									}
								} catch (Exception e) {

								}
							}
						});

						Hpthread.start();
					}
					else
					{
						//ģ���ʴ��ϰ��û
						Intent friend = new Intent(HPBoardInfo.this, HPBoardFriend.class);
						startActivity(friend);
					}
				}
			});
			break;
		}
		

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPBoardInfo.this,
						"�̹� ���ÿ� �������̰ų� ������ �� ���� �����Դϴ�.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			}
		}
	};

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPBoardInfo.this,
						"��Ʈ��ũ ���°� ��Ȱ���� �ʽ��ϴ�.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPBoardInfo.this,
						"��û�� �Ϸ�Ǿ����ϴ�.", 0);
				if(btn2.getText().toString().equals("����ϱ�"))
				{
					mtoast = Toast.makeText(HPBoardInfo.this,
							"��û�� ��ҵǾ����ϴ�.", 0);
				}
				else if(btn1.getText().toString().equals("�����ϱ�"))
				{
					mtoast = Toast.makeText(HPBoardInfo.this,
							"�Խñ��� �����Ǿ����ϴ�.", 0);
				}
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				result = new Intent();
				result.putExtra("listposition", intent.getIntExtra("listposition", 0));
				setResult(1,result);
				HPBoardInfo.this.finish();
				break;
			case 2:
				break;
			case 3:
				break;
			}
		}
	};

	public void SetBoardInfo() {
		et1.setText(subject);
		et2.setText(content);
		et3.setText(meetzone);
		et4.setText(peoplecount + " & " + peoplecount);

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
				// TODO �ڵ� ������ �޼ҵ� ����
				try {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject hp = new HPObject(written, null, null, null, null,
							null, null, null, null, null, null, "findhomemy");

					Object obc = hp;

					out.writeObject(obc);

					if (MainActivity.connectthread.channel.isConnected()) {
						MainActivity.connectthread.channel.write(ByteBuffer
								.wrap(baos.toByteArray()));
					} else {
						handler2.sendEmptyMessage(0);
					}

					ByteBuffer data = ByteBuffer.allocate(1024);
					MainActivity.connectthread.selector.select();
					MainActivity.connectthread.channel.read(data);

					bais = new ByteArrayInputStream(data.array());

					ois = new ObjectInputStream(bais);

					inhpmy = (HPObject) ois.readObject();

					if (inhpmy.message.equals("falseresultmy")) {
						// handler2.sendEmptyMessage(1);
					} else {
						// handler2.sendEmptyMessage(1);
					}
					Hpthread.interrupt();
				} catch (Exception e) {

				}
			}
		});
		Hpthread.start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �ڵ� ������ �޼ҵ� ����
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0)
		{
			result = new Intent();
			setResult(1,result);
			finish();
		}
	}

}
