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
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HPHomeHome extends Activity {

	private ListView list;
	public ArrayList<String> homearraylist;
	public ArrayAdapter<String> homearrayadapter;
	private ListView listmy;
	public ArrayList<String> homearraylistmy;
	public ArrayAdapter<String> homearrayadaptermy;

	private Button btn1, btn2, btn3, btn4;
	private int listCount = 0;
	public static String[] friendlistid;
	public static String[] friendlist;
	public static String[] friendprofile;
	public static String[] friendpoint;
	public static String[] friendphone;
	public static String[] frienduniv_1;
	public static String[] friendsex;
	public static String mypoint;
	private Button btnv[];
	private Intent result;
	BackPressCloseHandler back;
	TextView tvmy1,tvmy2, tvmy3;
	TextView tv1,tv2;
	
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hphome);
		li = (LinearLayout)findViewById(R.id.lihphome);
		tv1 = (TextView)findViewById(R.id.tvhphome1);
		tv2 = (TextView)findViewById(R.id.tvhphome2);
		btn1 = (Button) findViewById(R.id.btnban);
		btn2 = (Button) findViewById(R.id.btnadd);
		btn3 = (Button) findViewById(R.id.btnreq);
		btn4 = (Button) findViewById(R.id.btnhometeam);
		hpdb = new HPDatabase(HPHomeHome.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.home_blue);
				tv1.setBackgroundColor(Color.argb(153, 178, 235, 244));
				tv2.setBackgroundColor(Color.argb(153, 178, 235, 244));
				btn1.setBackgroundResource(R.drawable.btnblocklist_blue);
				btn2.setBackgroundResource(R.drawable.btnfriendadd_blue);
				btn3.setBackgroundResource(R.drawable.btnmeetlist_blue);
				btn4.setBackgroundResource(R.drawable.btnteamad_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.home_pupple);
				tv1.setBackgroundColor(Color.argb(153, 243, 97, 220));
				tv2.setBackgroundColor(Color.argb(153, 243, 97, 220));
				btn1.setBackgroundResource(R.drawable.btnblocklist_pupple);
				btn2.setBackgroundResource(R.drawable.btnfriendadd_pupple);
				btn3.setBackgroundResource(R.drawable.btnmeetlist_pupple);
				btn4.setBackgroundResource(R.drawable.btnteamad_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.home_green);
				tv1.setBackgroundColor(Color.argb(153, 134, 229, 127));
				tv2.setBackgroundColor(Color.argb(153, 134, 229, 127));
				btn1.setBackgroundResource(R.drawable.btnblocklist_green);
				btn2.setBackgroundResource(R.drawable.btnfriendadd_green);
				btn3.setBackgroundResource(R.drawable.btnmeetlist_green);
				btn4.setBackgroundResource(R.drawable.btnteamad_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.home_red);
				tv1.setBackgroundColor(Color.argb(153, 255, 167, 167));
				tv2.setBackgroundColor(Color.argb(153, 255, 167, 167));
				btn1.setBackgroundResource(R.drawable.btnblocklist_red);
				btn2.setBackgroundResource(R.drawable.btnfriendadd_red);
				btn3.setBackgroundResource(R.drawable.btnmeetlist_red);
				btn4.setBackgroundResource(R.drawable.btnteamad_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.home_gold);
				tv1.setBackgroundColor(Color.argb(153, 229, 216, 92));
				tv2.setBackgroundColor(Color.argb(153, 229, 216, 92));
				btn1.setBackgroundResource(R.drawable.btnblocklist_gold);
				btn2.setBackgroundResource(R.drawable.btnfriendadd_gold);
				btn3.setBackgroundResource(R.drawable.btnmeetlist_gold);
				btn4.setBackgroundResource(R.drawable.btnteamad_gold);
				break;
			}
		}
		
		back = new BackPressCloseHandler(this);
		list = (ListView) findViewById(R.id.listView2);
		listmy = (ListView) findViewById(R.id.listView1);
		
		homearraylist = new ArrayList<String>();
		homearraylistmy = new ArrayList<String>();

		SetHomeList();
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				Intent intent = new Intent(HPHomeHome.this, HPBanList.class);
				startActivity(intent);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				Intent intent = new Intent(HPHomeHome.this, HPFriendList.class);
				startActivityForResult(intent, 2);
			}
		});
		
		btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPHomeHome.this, HPReqMeetBoard.class);
				startActivity(intent);
			}
		});
		
		btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPHomeHome.this, HPHomeTeam.class);
				startActivity(intent);
			}
		});
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
				TextView point = (TextView) abc.findViewById(R.id.textView5);

				nic.setTextSize(25);
				sex.setTextSize(25);
				univ_1.setTextSize(25);
				phone.setTextSize(25);
				point.setTextSize(25);
				String sexstr = "";
				String phonestr = "";
				String starstr = "";

				for (int i = 0; i < (int) (friendphone[position].length() / 2.0 + 0.5); i++) {
					starstr += "*";
				}

				if (friendsex[position].equals("0")) {
					sexstr = "남자";
				} else {
					sexstr = "여자";
				}

				phonestr = friendphone[position].substring(0,
						friendphone[position].length() / 2) + starstr;

				nic.setText("닉네임 : " + friendlist[position]);
				sex.setText("성별 : " + sexstr);
				univ_1.setText("대학교 : " + frienduniv_1[position] + "대학교");
				phone.setText("연락처 : " + phonestr);
				point.setText("두근두근 : " + friendpoint[position] + " 점");

				AlertDialog dia1 = new AlertDialog.Builder(
						HPHomeHome.this).create();
				dia1.setTitle("'" + friendlist[position] + "'님의 정보");
				dia1.setView(abc);
				dia1.setCancelable(true);
				dia1.setCanceledOnTouchOutside(true);
				dia1.setButton("차단", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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
											friendlistid[index], "차단",
											null, null, null, null, null,null,
											null, null, "setfriendban");

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

									ByteBuffer data = ByteBuffer
											.allocate(1024);
									MainActivity.connectthread.selector
											.select();
									MainActivity.connectthread.channel
											.read(data);

									bais = new ByteArrayInputStream(data
											.array());

									ois = new ObjectInputStream(bais);

									inhp = (HPObject) ois.readObject();

									if (inhp.message
											.equals("frienderror-17")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-18")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-19")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-20")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-21")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-22")) {
										handler2.sendEmptyMessage(9);
									} else if (inhp.message
											.equals("frienderror-23")) {
										handler2.sendEmptyMessage(9);
									} else if (inhp.message
											.equals("frienderror-24")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-25")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-26")) {
										handler2.sendEmptyMessage(8);
									}

									homearraylist.clear();
									for (int i = 0; i < list.getCount(); i++) {
										if (i == list.getCount() - 1) {
											friendlist[i] = "";
											friendsex[i] = "";
											frienduniv_1[i] = "";
											friendphone[i] = "";
											friendlistid[i] = "";
											friendpoint[i] ="";
											break;
										}
										if (i < index) {
											homearraylist.add(friendlist[i]);
										} else {
											friendlist[i] = friendlist[i + 1];
											friendsex[i] = friendsex[i + 1];
											frienduniv_1[i] = frienduniv_1[i + 1];
											friendphone[i] = friendphone[i + 1];
											friendlistid[i] = friendlistid[i + 1];
											friendpoint[i] = friendpoint[i + 1];
											homearraylist.add(friendlist[i]);
										}

									}

									handler2.sendEmptyMessage(2);

									Hpthread.interrupt();
								} catch (Exception e) {
									// TODO 자동 생성된 catch 블록
									if (MainActivity.connectthread.channel
											.isConnected()) {
										Hpthread.interrupt();
										MainActivity.connectthread
												.interrupt();
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

				dia1.setButton2("삭제", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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
											friendlistid[index], "삭제",
											null, null, null, null, null,null,
											null, null, "setfriendban");

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

									ByteBuffer data = ByteBuffer
											.allocate(1024);
									MainActivity.connectthread.selector
											.select();
									MainActivity.connectthread.channel
											.read(data);

									bais = new ByteArrayInputStream(data
											.array());

									ois = new ObjectInputStream(bais);

									inhp = (HPObject) ois.readObject();

									if (inhp.message
											.equals("frienderror-17")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-18")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-19")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-20")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-21")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-22")) {
										handler2.sendEmptyMessage(9);
									} else if (inhp.message
											.equals("frienderror-23")) {
										handler2.sendEmptyMessage(9);
									} else if (inhp.message
											.equals("frienderror-24")) {
										handler2.sendEmptyMessage(6);
									} else if (inhp.message
											.equals("frienderror-25")) {
										handler2.sendEmptyMessage(8);
									} else if (inhp.message
											.equals("frienderror-26")) {
										handler2.sendEmptyMessage(8);
									}

									homearraylist.clear();
									for (int i = 0; i < list.getCount(); i++) {
										if (i == list.getCount() - 1) {
											friendlist[i] = "";
											friendsex[i] = "";
											frienduniv_1[i] = "";
											friendphone[i] = "";
											friendlistid[i] = "";
											friendpoint[i] ="";
											break;
										}
										if (i < index) {
											homearraylist.add(friendlist[i]);
										} else {
											friendlist[i] = friendlist[i + 1];
											friendsex[i] = friendsex[i + 1];
											frienduniv_1[i] = frienduniv_1[i + 1];
											friendphone[i] = friendphone[i + 1];
											friendlistid[i] = friendlistid[i + 1];
											friendpoint[i] = friendpoint[i + 1];
											homearraylist.add(friendlist[i]);
										}

									}

									handler2.sendEmptyMessage(2);

									Hpthread.interrupt();
								} catch (Exception e) {
									// TODO 자동 생성된 catch 블록
									if (MainActivity.connectthread.channel
											.isConnected()) {
										Hpthread.interrupt();
										MainActivity.connectthread
												.interrupt();
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
				dia1.show();
			}
		});

		listmy.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
				TextView point = (TextView) abc.findViewById(R.id.textView5);

				nic.setTextSize(25);
				sex.setTextSize(25);
				univ_1.setTextSize(25);
				phone.setTextSize(25);
				point.setTextSize(25);
				String sexstr = "";

				if (inhpmy.Sex.equals("0")) {
					sexstr = "남자";
				} else {
					sexstr = "여자";
				}

				nic.setText("닉네임 : " + inhpmy.message);
				sex.setText("성별 : " + sexstr);
				univ_1.setText("대학교 : " + inhpmy.Univ_1 + "대학교");
				phone.setText("연락처 : " + inhpmy.Phone);
				point.setText("두근두근 : " + inhpmy.HeartPoint + " 점");
		
				AlertDialog dia1 = new AlertDialog.Builder(
						HPHomeHome.this).create();
				dia1.setTitle("내 정보");
				dia1.setView(abc);
				dia1.setCancelable(true);
				dia1.setCanceledOnTouchOutside(true);
				dia1.setButton("대화명편집", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 자동 생성된 메소드 스텁
						
						Intent intent = new Intent(HPHomeHome.this, HPHomeProFModify.class);
						intent.putExtra("message", inhpmy.Profile);
						startActivityForResult(intent,1);
						
					}
					
				});

				dia1.setButton2("프로필편집", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 자동 생성된 메소드 스텁
						Intent intent = new Intent(HPHomeHome.this, HPModify.class);
						intent.putExtra("id", MainActivity.Loginid);
						intent.putExtra("nicname", inhpmy.message);
						intent.putExtra("sex", inhpmy.Sex);
						intent.putExtra("univ_1", inhpmy.Univ_1);
						intent.putExtra("univ_2", inhpmy.Univ_2);
						intent.putExtra("phone", inhpmy.Phone);
						intent.putExtra("schoolnumber", inhpmy.SchoolNum);
						startActivityForResult(intent,0);
						
					}
				});
				dia1.show();

			}
		});
	}

	public void SetHomeList() {
		homearraylist.clear();
		homearraylistmy.clear();
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
							"findhomemy");

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

					 mypoint = inhpmy.HeartPoint;
					if (inhpmy.message.equals("falseresultmy")) {
						// handler2.sendEmptyMessage(1);
					} else {
						homearraylistmy.add(inhpmy.message);
						handler2.sendEmptyMessage(1);
					}

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					hp = new HPObject(MainActivity.Loginid, null, null, null,null,
							null, null, null, null, null, null, "findhomefri");

					obc = hp;

					out.writeObject(obc);

					if (MainActivity.connectthread.channel.isConnected()) {
						MainActivity.connectthread.channel.write(ByteBuffer
								.wrap(baos.toByteArray()));
					} else {
						handler2.sendEmptyMessage(0);
					}

					ByteBuffer data2 = ByteBuffer.allocate(1024);
					MainActivity.connectthread.selector.select();
					MainActivity.connectthread.channel.read(data2);

					bais = new ByteArrayInputStream(data2.array());

					ois = new ObjectInputStream(bais);

					inhp = (HPObject) ois.readObject();

					friendlist = inhp.message.split("/");
					friendlistid = inhp.ID.split("/");
					friendprofile = inhp.Profile.split("/");
					friendpoint = inhp.HeartPoint.split("/");
					friendsex = inhp.Sex.split("/");
					friendphone = inhp.Phone.split("/");
					frienduniv_1 = inhp.Univ_1.split("/");

					if (inhp.message.equals("falseresult")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for (int i = 0; i < friendlist.length; i++) {
							homearraylist.add(friendlist[i]);
						}

						 handler2.sendEmptyMessage(2);
					}

					Hpthread.interrupt();
				} catch (Exception e) {
					// TODO 자동 생성된 catch 블록
					if (MainActivity.connectthread.channel.isConnected()) {
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

	public class MyAdapter extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapter(Activity context, int textViewResourceId,
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
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				a1.setGravity(Gravity.CENTER);
				LinearLayout li1 = new LinearLayout(m_context);
				LinearLayout li2 = new LinearLayout(m_context);
				LinearLayout li3 = new LinearLayout(m_context);
				li1.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4));
				li2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3));
				li3.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4));

				li1.setGravity(Gravity.CENTER);
				li2.setGravity(Gravity.CENTER);
				li3.setGravity(Gravity.CENTER);

				TextView tv1 = new TextView(m_context);
				TextView tv2 = new TextView(m_context);
				TextView tv3 = new TextView(m_context);

				tv1.setFocusable(false);
				tv1.setClickable(false);

				tv2.setFocusable(false);
				tv2.setClickable(false);

				tv3.setFocusable(false);
				tv3.setClickable(false);

				tv1.setText(friendlist[index]);
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv1.setMaxLines(1);
				tv1.setEllipsize(TruncateAt.END);

				tv2.setMaxLines(1);
				tv2.setEllipsize(TruncateAt.END);

				tv2.setText(friendprofile[index]);
				tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv3.setText(friendpoint[index]);
				tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

				li1.setMinimumHeight(105);
				li1.addView(tv1);
				li2.addView(tv2);
				li3.addView(tv3);

				a1.addView(li1);
				a1.addView(li2);
				a1.addView(li3);

				listlayout.add(a1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return listlayout.get(position);

		}

	}

	public class MyAdapterMy extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapterMy(Activity context, int textViewResourceId,
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
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				a1.setGravity(Gravity.CENTER);
				LinearLayout li1 = new LinearLayout(m_context);
				LinearLayout li2 = new LinearLayout(m_context);
				LinearLayout li3 = new LinearLayout(m_context);
				li1.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4));
				li2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3));
				li3.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 4));

				li1.setGravity(Gravity.CENTER);
				li2.setGravity(Gravity.CENTER);
				li3.setGravity(Gravity.CENTER);

				tvmy1 = new TextView(m_context);
				tvmy2 = new TextView(m_context);
				tvmy3 = new TextView(m_context);

				tvmy1.setFocusable(false);
				tvmy1.setClickable(false);

				tvmy2.setFocusable(false);
				tvmy2.setClickable(false);

				tvmy3.setFocusable(false);
				tvmy3.setClickable(false);

				li1.setMinimumHeight(105);
				tvmy1.setText(inhpmy.message);
				tvmy1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tvmy1.setEllipsize(TruncateAt.END);
				tvmy2.setText(inhpmy.Profile);
				tvmy2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tvmy2.setMaxLines(1);
				tvmy2.setEllipsize(TruncateAt.END);
				tvmy3.setText(inhpmy.HeartPoint);
				tvmy3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

				li1.addView(tvmy1);
				li2.addView(tvmy2);
				li3.addView(tvmy3);

				a1.addView(li1);
				a1.addView(li2);
				a1.addView(li3);

				listlayout.add(a1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return listlayout.get(position);

		}

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
				mtoast = Toast.makeText(HPHomeHome.this, "네트워크 상태가 원활하지 않습니다.",
						0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				homearrayadaptermy = new MyAdapterMy(HPHomeHome.this,
						android.R.layout.simple_list_item_1,
						homearraylistmy);

				listmy.setAdapter(homearrayadaptermy);
				break;
			case 2:
				homearrayadapter = new MyAdapter(HPHomeHome.this,
						android.R.layout.simple_list_item_1, homearraylist);

				list.setAdapter(homearrayadapter);			
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 자동 생성된 메소드 스텁
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if(data != null)
			{
				homearraylistmy.remove(0);
				inhpmy.message = data.getStringExtra("nicname");
				inhpmy.Univ_1 = data.getStringExtra("univ_1");
				inhpmy.Sex = data.getStringExtra("sex");
				homearraylistmy.add(inhpmy.message);
				tvmy1.setText(inhpmy.message);
				homearrayadaptermy.notifyDataSetChanged();
			}
		}
		else if(requestCode ==1)
		{
			if(data != null)
			{
				homearraylistmy.remove(0);
				inhpmy.Profile = data.getStringExtra("message");
				homearraylistmy.add(inhpmy.Profile);
				tvmy2.setText(inhpmy.Profile);
				homearrayadaptermy.notifyDataSetChanged();
			}
		}
		else if(requestCode == 2)
		{
			SetHomeList();
			homearrayadapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO 자동 생성된 메소드 스텁
		if (back.onBackPressed()) {
			result = new Intent();
			result.putExtra("finish", "finish");
			setResult(0,result);
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
			toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 앱이 종료됩니다.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}
	
}
