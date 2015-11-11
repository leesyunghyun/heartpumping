package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HPFriendList extends Activity {

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	private String[] friendIn;
	private String[] friendInid;
	private String[] friendOut;
	private String[] friendOutid;
	HPObject inhp;
	HPObject inhp2;

	private Intent result;
	private Button[] btnv1, btnv2, btnv3;
	private ListView lv1, lv2;
	private ArrayAdapter<String> adapter1, adapter2;
	private ArrayList<String> arraylist1, arraylist2;
	private String[] strsexin;
	private String[] strsexout;
	private String[] strphonein;
	private String[] strphoneout;
	private String[] strheartpointin;
	private String[] strheartpointout;
	private String[] struniv_1in;
	private String[] struniv_1out;

	LinearLayout li;
	HPDatabase hpdb;
	Cursor c;
	SQLiteDatabase sql;
	TextView tv1,tv2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 자동 생성된 메소드 스텁
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpfriend);
		li = (LinearLayout)findViewById(R.id.lihpfriend);
		lv1 = (ListView) findViewById(R.id.friendlist1);
		lv2 = (ListView) findViewById(R.id.friendlist2);
		tv1 = (TextView) findViewById(R.id.tvhpfriend1);
		tv2 = (TextView) findViewById(R.id.tvhpfriend2);
		
		hpdb = new HPDatabase(HPFriendList.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.addfriend_blue);
				tv1.setBackgroundColor(Color.argb(153, 178, 235, 244));
				tv2.setBackgroundColor(Color.argb(153, 178, 235, 244));
				break;
			case 1:
				li.setBackgroundResource(R.drawable.addfriend_pupple);
				tv1.setBackgroundColor(Color.argb(153, 243, 97, 220));
				tv2.setBackgroundColor(Color.argb(153, 243, 97, 220));
				break;
			case 2:
				li.setBackgroundResource(R.drawable.addfriend_green);
				tv1.setBackgroundColor(Color.argb(153, 134, 229, 127));
				tv2.setBackgroundColor(Color.argb(153, 134, 229, 127));
				break;
			case 3:
				li.setBackgroundResource(R.drawable.addfriend_red);
				tv1.setBackgroundColor(Color.argb(153, 255, 167, 167));
				tv2.setBackgroundColor(Color.argb(153, 255, 167, 167));
				break;
			case 4:
				li.setBackgroundResource(R.drawable.addfriend_gold);
				tv1.setBackgroundColor(Color.argb(153, 229, 216, 92));
				tv2.setBackgroundColor(Color.argb(153, 229, 216, 92));
				break;
			}
		}
		
		arraylist1 = new ArrayList<String>();
		arraylist2 = new ArrayList<String>();

		SetFriendList();

		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout abc = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);
				abc.setOrientation(LinearLayout.VERTICAL);

				TextView nic = (TextView) abc.findViewById(R.id.textView1);
				TextView sex = (TextView) abc.findViewById(R.id.textView2);
				TextView univ_1 = (TextView) abc.findViewById(R.id.textView3);
				TextView phone = (TextView) abc.findViewById(R.id.textView4);
				TextView heart = (TextView) abc.findViewById(R.id.textView5);

				nic.setTextSize(25);
				sex.setTextSize(25);
				univ_1.setTextSize(25);
				phone.setTextSize(25);
				heart.setTextSize(25);
				String sexstr = "";
				String phonestr = "";
				String starstr = "";

				for (int i = 0; i < (int) (strphoneout[position].length() / 2.0 + 0.5); i++) {
					starstr += "*";
				}

				if (strsexout[position].equals("0")) {
					sexstr = "남자";
				} else {
					sexstr = "여자";
				}

				phonestr = strphoneout[position].substring(0,
						strphoneout[position].length() / 2) + starstr;

				nic.setText("닉네임 : " + friendOut[position]);
				sex.setText("성별 : " + sexstr);
				univ_1.setText("대학교 : " + struniv_1out[position] + "대학교");
				phone.setText("연락처 : " + phonestr);
				heart.setText("두근두근 : " + strheartpointout[position] + " 점");

				AlertDialog dia1 = new AlertDialog.Builder(
						HPFriendList.this).create();
				dia1.setTitle("정보");
				dia1.setView(abc);
				dia1.setCanceledOnTouchOutside(true);
				dia1.setCancelable(true);
				dia1.show();
			}
		});

		lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				final int index = position;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout abc = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);
				abc.setOrientation(LinearLayout.VERTICAL);

				TextView nic = (TextView) abc.findViewById(R.id.textView1);
				TextView sex = (TextView) abc.findViewById(R.id.textView2);
				TextView univ_1 = (TextView) abc.findViewById(R.id.textView3);
				TextView phone = (TextView) abc.findViewById(R.id.textView4);
				TextView heart = (TextView) abc.findViewById(R.id.textView5);

				nic.setTextSize(25);
				sex.setTextSize(25);
				univ_1.setTextSize(25);
				phone.setTextSize(25);
				heart.setTextSize(25);
				String sexstr = "";
				String phonestr = "";
				String starstr = "";

				for (int i = 0; i < (int) (strphonein[position].length() / 2.0 + 0.5); i++) {
					starstr += "*";
				}

				if (strsexin[position].equals("0")) {
					sexstr = "남자";
				} else {
					sexstr = "여자";
				}

				phonestr = strphonein[position].substring(0,
						strphonein[position].length() / 2) + starstr;

				nic.setText("닉네임 : " + friendIn[position]);
				sex.setText("성별 : " + sexstr);
				univ_1.setText("대학교 : " + struniv_1in[position] + "대학교");
				phone.setText("연락처 : " + phonestr);
				heart.setText("두근두근 : " + strheartpointin[position] + " 점");

				AlertDialog dia1 = new AlertDialog.Builder(
						HPFriendList.this).create();
				dia1.setTitle("정보");
				dia1.setView(abc);
				dia1.setCancelable(true);
				dia1.setButton("수락", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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

									HPObject hp = new HPObject(
											MainActivity.Loginid,
											friendInid[index], "요청", null,
											null, null, null, null, null, null,null,
											"setfriend");

									Object obc = hp;

									out.writeObject(obc);

									if (MainActivity.connectthread.channel
											.isConnected()) {
										MainActivity.connectthread.channel
												.write(ByteBuffer.wrap(baos
														.toByteArray()));
									} else {
										handler2.sendEmptyMessage(0);
									}

									ByteBuffer data = ByteBuffer.allocate(1024);
									MainActivity.connectthread.selector
											.select();
									MainActivity.connectthread.channel
											.read(data);

									bais = new ByteArrayInputStream(data
											.array());

									ois = new ObjectInputStream(bais);

									inhp = (HPObject) ois.readObject();

									if (inhp.message.equals("frienderror-1")) {
										handler2.sendEmptyMessage(3);
									} else if (inhp.message
											.equals("frienderror-2")) {
										handler2.sendEmptyMessage(3);
									} else if (inhp.message
											.equals("frienderror-3")) {
										// 차단했는데 친구신청

									} else if (inhp.message
											.equals("frienderror-4")) {
										handler2.sendEmptyMessage(4);
									} else if (inhp.message
											.equals("frienderror-5")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-6")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-7")) {
										handler2.sendEmptyMessage(5);
									} else if (inhp.message
											.equals("frienderror-8")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-9")) {
										handler2.sendEmptyMessage(5);
									} else if (inhp.message
											.equals("frienderror-10")) {
										handler2.sendEmptyMessage(6);
									}

									arraylist2.clear();
									for (int i = 0; i < lv2.getCount(); i++) {
										if (i == lv2.getCount() - 1) {
											strheartpointin[i] = "";
											strphonein[i] = "";
											strsexin[i] = "";
											struniv_1in[i] = "";
											friendIn[i] = "";
											friendInid[i] = "";
											break;
										}
										if (i < index) {
											arraylist2.add("닉네임 : "
													+ friendIn[i]);
										} else {
											strheartpointin[i] = strheartpointin[i + 1];
											strphonein[i] = strphonein[i + 1];
											strsexin[i] = strsexin[i + 1];
											struniv_1in[i] = struniv_1in[i + 1];
											friendIn[i] = friendIn[i + 1];
											friendInid[i] = friendInid[i + 1];
											arraylist2.add("닉네임 : "
													+ friendIn[i]);
										}

									}

									handler2.sendEmptyMessage(2);

									Hpthread.interrupt();
								} catch (Exception e) {
									// TODO 자동 생성된 catch 블록
									if (MainActivity.connectthread.channel
											.isConnected()) {
										Hpthread.interrupt();
										MainActivity.connectthread.interrupt();
										MainActivity.connectthread = new HPConnectThread(
												MainActivity.context);
										MainActivity.connectthread.start();
									}
								}

							}
						});
						Hpthread.start();
					}
				});
				dia1.setCanceledOnTouchOutside(true);
				dia1.show();
			}
		});
	}

	public void SetFriendList() {

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

					HPObject hp = new HPObject(MainActivity.Loginid, null,
							null, null, null, null, null, null, null, null,null,
							"findfndout");

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

					inhp = (HPObject) ois.readObject();

					friendOut = inhp.message.split("/");
					friendOutid = inhp.ID.split("/");
					strheartpointout = inhp.HeartPoint.split("/");
					strphoneout = inhp.Phone.split("/");
					strsexout = inhp.Sex.split("/");
					struniv_1out = inhp.Univ_1.split("/");
					if (inhp.message.equals("falseresult")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for (int i = 0; i < friendOut.length; i++) {
							arraylist1.add("닉네임 : " + friendOut[i]);
						}

						handler2.sendEmptyMessage(1);
					}

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject hp2 = new HPObject(MainActivity.Loginid, null,
							null, null, null, null, null, null, null, null,null,
							"findfndin");

					Object obc2 = hp2;

					out.writeObject(obc2);

					if (MainActivity.connectthread.channel.isConnected()) {
						MainActivity.connectthread.channel.write(ByteBuffer
								.wrap(baos.toByteArray()));
					} else {
						handler2.sendEmptyMessage(0);
					}

					data = ByteBuffer.allocate(1024);
					MainActivity.connectthread.selector.select();
					MainActivity.connectthread.channel.read(data);

					bais = new ByteArrayInputStream(data.array());

					ois = new ObjectInputStream(bais);

					inhp2 = (HPObject) ois.readObject();

					friendIn = inhp2.message.split("/");
					friendInid = inhp2.ID.split("/");
					strheartpointin = inhp2.HeartPoint.split("/");
					strphonein = inhp2.Phone.split("/");
					strsexin = inhp2.Sex.split("/");
					struniv_1in = inhp2.Univ_1.split("/");

					if (inhp2.message.equals("falseresult")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for (int i = 0; i < friendIn.length; i++) {
							arraylist2.add("닉네임 : " + friendIn[i]);
						}

						handler2.sendEmptyMessage(2);
					}
				} catch (Exception e) {

				}
			}
		});

		Hpthread.start();
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
				mtoast = Toast.makeText(HPFriendList.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				adapter1 = new MyAdapterOut(HPFriendList.this,
						android.R.layout.simple_list_item_1, arraylist1);

				lv1.setAdapter(adapter1);
				adapter1.notifyDataSetChanged();
				break;
			case 2:
				adapter2 = new MyAdapterIn(HPFriendList.this,
						android.R.layout.simple_list_item_1, arraylist2);

				lv2.setAdapter(adapter2);
				adapter2.notifyDataSetChanged();
				break;
			case 3:
				mtoast = Toast.makeText(HPFriendList.this,
						"이미 친구신청하였거나 친구인 회원입니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 4:
				mtoast = Toast.makeText(HPFriendList.this,
						"축하드립니다! 서로 친구가 되셨어요!", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 5:
				mtoast = Toast.makeText(HPFriendList.this, "친구요청이 완료되었습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 6:
				mtoast = Toast.makeText(HPFriendList.this, "죄송합니다. 다시 시도해주세요", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			}
		}
	};

	public class MyAdapterIn extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapterIn(Activity context, int textViewResourceId,
				ArrayList<String> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			m_context = context;
			m_arrayList = objects;
			//
			setFriendList();
		}

		private void setFriendList() {
			listlayout = new ArrayList<LinearLayout>();
			btnv1 = new Button[m_arrayList.size()];
			btnv3 = new Button[m_arrayList.size()];
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				TextView tv = new TextView(m_context);
				DisplayMetrics outMetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
				int pixel = (int) (1.5 * outMetrics.densityDpi);
				tv.setWidth(pixel);
				btnv1[i] = new Button(m_context);
				btnv3[i] = new Button(m_context);
				tv.setFocusable(false);
				tv.setClickable(false);
				btnv1[i].setFocusable(false);
				btnv3[i].setFocusable(false);
				btnv1[i].setClickable(true);
				btnv3[i].setClickable(true);
				//btnv1[i].setText("거절");
				//btnv3[i].setText("수락");
				
				if(c.moveToFirst())
				{
					switch(c.getInt(6))
					{
					case 0:
						btnv1[i].setBackgroundResource(R.drawable.btnrefuse_blue);
						btnv3[i].setBackgroundResource(R.drawable.btnagree_blue);
						break;
					case 1:
						btnv1[i].setBackgroundResource(R.drawable.btnrefuse_pupple);
						btnv3[i].setBackgroundResource(R.drawable.btnagree_pupple);
						break;
					case 2:
						btnv1[i].setBackgroundResource(R.drawable.btnrefuse_green);
						btnv3[i].setBackgroundResource(R.drawable.btnagree_green);
						break;
					case 3:
						btnv1[i].setBackgroundResource(R.drawable.btnrefuse_red);
						btnv3[i].setBackgroundResource(R.drawable.btnagree_red);
						break;
					case 4:
						btnv1[i].setBackgroundResource(R.drawable.btnrefuse_gold);
						btnv3[i].setBackgroundResource(R.drawable.btnagree_gold);
						break;
					}
				}
				
				
				btnv3[i].setOnClickListener(new View.OnClickListener() {
					
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

									HPObject hp = new HPObject(
											MainActivity.Loginid,
											friendInid[index], "요청", null,
											null, null, null, null, null, null,null,
											"setfriend");

									Object obc = hp;

									out.writeObject(obc);

									if (MainActivity.connectthread.channel
											.isConnected()) {
										MainActivity.connectthread.channel
												.write(ByteBuffer.wrap(baos
														.toByteArray()));
									} else {
										handler2.sendEmptyMessage(0);
									}

									ByteBuffer data = ByteBuffer.allocate(1024);
									MainActivity.connectthread.selector
											.select();
									MainActivity.connectthread.channel
											.read(data);

									bais = new ByteArrayInputStream(data
											.array());

									ois = new ObjectInputStream(bais);

									inhp = (HPObject) ois.readObject();

									if (inhp.message.equals("frienderror-1")) {
										handler2.sendEmptyMessage(3);
									} else if (inhp.message
											.equals("frienderror-2")) {
										handler2.sendEmptyMessage(3);
									} else if (inhp.message
											.equals("frienderror-3")) {
										// 차단했는데 친구신청

									} else if (inhp.message
											.equals("frienderror-4")) {
										handler2.sendEmptyMessage(4);
									} else if (inhp.message
											.equals("frienderror-5")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-6")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-7")) {
										handler2.sendEmptyMessage(5);
									} else if (inhp.message
											.equals("frienderror-8")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-9")) {
										handler2.sendEmptyMessage(5);
									} else if (inhp.message
											.equals("frienderror-10")) {
										handler2.sendEmptyMessage(6);
									}

									arraylist2.clear();
									for (int i = 0; i < lv2.getCount(); i++) {
										if (i == lv2.getCount() - 1) {
											strheartpointin[i] = "";
											strphonein[i] = "";
											strsexin[i] = "";
											struniv_1in[i] = "";
											friendIn[i] = "";
											friendInid[i] = "";
											break;
										}
										if (i < index) {
											arraylist2.add("닉네임 : "
													+ friendIn[i]);
										} else {
											strheartpointin[i] = strheartpointin[i + 1];
											strphonein[i] = strphonein[i + 1];
											strsexin[i] = strsexin[i + 1];
											struniv_1in[i] = struniv_1in[i + 1];
											friendIn[i] = friendIn[i + 1];
											friendInid[i] = friendInid[i + 1];
											arraylist2.add("닉네임 : "
													+ friendIn[i]);
										}

									}

									handler2.sendEmptyMessage(2);

									Hpthread.interrupt();
								} catch (Exception e) {
									// TODO 자동 생성된 catch 블록
									if (MainActivity.connectthread.channel
											.isConnected()) {
										Hpthread.interrupt();
										MainActivity.connectthread.interrupt();
										MainActivity.connectthread = new HPConnectThread(
												MainActivity.context);
										MainActivity.connectthread.start();
									}
								}

							}
						});
						Hpthread.start();
					}
				});
				btnv1[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						AlertDialog.Builder dia1 = new AlertDialog.Builder(
								HPFriendList.this);
						dia1.setTitle("정보");
						dia1.setMessage("친구요청을 거절하시겠습니까?");
						dia1.setCancelable(true);
						dia1.setPositiveButton("예", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO 자동 생성된 메소드 스텁
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
										// TODO 자동 생성된 메소드 스텁
										try {

											baos = new ByteArrayOutputStream();
											out = new ObjectOutputStream(baos);

											HPObject hp = new HPObject(
													MainActivity.Loginid,
													friendInid[index], "삭제2",
													null, null, null, null,
													null, null, null,null,
													"cancelban");

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
													.equals("falseresult")) {
												// handler2.sendEmptyMessage(1);
											} else {
												arraylist2.clear();
												for (int i = 0; i < lv2
														.getCount(); i++) {
													if (i == lv2.getCount() - 1) {
														friendIn[i] = "";
														friendInid[i] = "";
														strheartpointin[i] = "";
														strphonein[i] = "";
														strsexin[i] = "";
														struniv_1in[i] = "";
														break;
													}
													if (i < index) {
														arraylist2.add("닉네임 : "
																+ friendIn[i]);
													} else {
														friendIn[i] = friendIn[i + 1];
														friendInid[i] = friendInid[i + 1];
														strheartpointin[i] = strheartpointin[i + 1];
														strphonein[i] = strphonein[i + 1];
														strsexin[i] = strsexin[i + 1];
														struniv_1in[i] = struniv_1in[i + 1];
														arraylist2.add("닉네임 : "
																+ friendIn[i]);
													}
												}

												handler2.sendEmptyMessage(2);

												Hpthread.interrupt();
											}
										} catch (Exception e) {
											if (MainActivity.connectthread.channel
													.isConnected()) {
												Hpthread.interrupt();
												MainActivity.connectthread
														.interrupt();
												MainActivity.connectthread = new HPConnectThread(
														MainActivity.context);
												MainActivity.connectthread
														.start();
											}
										}
									}
								});

								Hpthread.start();
							}
						});

						dia1.show();

					}
				});

				tv.setText(m_arrayList.get(i));
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				
				a1.addView(tv);
				a1.addView(btnv3[i]);
				a1.addView(btnv1[i]);
				
				listlayout.add(a1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return listlayout.get(position);

		}
	}

	public class MyAdapterOut extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapterOut(Activity context, int textViewResourceId,
				ArrayList<String> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			m_context = context;
			m_arrayList = objects;
			//
			setFriendList();
		}

		private void setFriendList() {
			listlayout = new ArrayList<LinearLayout>();
			btnv2 = new Button[m_arrayList.size()];
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				TextView tv = new TextView(m_context);
				btnv2[i] = new Button(m_context);
				tv.setFocusable(false);
				tv.setClickable(false);

				DisplayMetrics outMetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
				int pixel = (int) (1.9 * outMetrics.densityDpi);
				tv.setWidth(pixel);

				btnv2[i].setFocusable(false);
				btnv2[i].setClickable(true);
				//btnv2[i].setText("취소");
				
				if(c.moveToFirst())
				{
					switch(c.getInt(6))
					{
					case 0:
						btnv2[i].setBackgroundResource(R.drawable.btncancel_blue);
						break;
					case 1:
						btnv2[i].setBackgroundResource(R.drawable.btncancel_pupple);
						break;
					case 2:
						btnv2[i].setBackgroundResource(R.drawable.btncancel_green);
						break;
					case 3:
						btnv2[i].setBackgroundResource(R.drawable.btncancel_red);
						break;
					case 4:
						btnv2[i].setBackgroundResource(R.drawable.btncancel_gold);
						break;
					}
				}
				btnv2[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder dia1 = new AlertDialog.Builder(
								HPFriendList.this);
						dia1.setTitle("정보");
						dia1.setMessage("친구신청을 취소하시겠습니까?");
						dia1.setCancelable(true);
						dia1.setPositiveButton("예", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
										// TODO 자동 생성된 메소드 스텁
										try {

											baos = new ByteArrayOutputStream();
											out = new ObjectOutputStream(baos);

											HPObject hp = new HPObject(
													MainActivity.Loginid,
													friendOutid[index], "삭제",
													null, null, null, null,
													null, null, null,null,
													"cancelban");

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
													.equals("falseresult")) {
												// handler2.sendEmptyMessage(1);
											} else {
												arraylist1.clear();
												for (int i = 0; i < lv1
														.getCount(); i++) {
													if (i == lv1.getCount() - 1) {
														friendOut[i] = "";
														friendOutid[i] = "";
														strheartpointout[i] = "";
														strphoneout[i] = "";
														strsexout[i] = "";
														struniv_1out[i] = "";
														break;
													}

													if (i < index) {
														arraylist1.add("닉네임 : "
																+ friendOut[i]);
													} else {
														friendOut[i] = friendOut[i + 1];
														friendOutid[i] = friendOutid[i + 1];
														strheartpointout[i] = strheartpointout[i + 1];
														strphoneout[i] = strphoneout[i + 1];
														strsexout[i] = strsexout[i + 1];
														struniv_1out[i] = struniv_1out[i + 1];
														arraylist1.add("닉네임 : "
																+ friendOut[i]);
													}
												}

												handler2.sendEmptyMessage(1);

												Hpthread.interrupt();
											}
										} catch (Exception e) {
											if (MainActivity.connectthread.channel
													.isConnected()) {
												Hpthread.interrupt();
												MainActivity.connectthread
														.interrupt();
												MainActivity.connectthread = new HPConnectThread(
														MainActivity.context);
												MainActivity.connectthread
														.start();
											}
										}
									}
								});

								Hpthread.start();
							}
						});
						dia1.setNegativeButton("아니요", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						dia1.show();
					}
				});

				tv.setText(m_arrayList.get(i));
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

				a1.addView(tv);
				a1.addView(btnv2[i]);
				
				listlayout.add(a1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return listlayout.get(position);

		}
	}

	@Override
	public void onBackPressed() {
		// TODO 자동 생성된 메소드 스텁
		super.onBackPressed();
		result = new Intent();
		setResult(2,result);
	}
	
}
