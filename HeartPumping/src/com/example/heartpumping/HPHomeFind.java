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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HPHomeFind extends Activity {

	private Spinner spin;
	private Button btn1;
	private TextView[] tv;
	private EditText et1, et2;
	private ListView listview;
	private ArrayList<String> arraylist;
	private ArrayAdapter<String> arrayadapter;
	private ArrayAdapter<String> spinadapter;
	private String[] strsplitfriend;
	private String[] strsplitsex;
	private String[] strsplituinv_1;
	private String[] strsplitphone;
	private String[] strsplitid;
	private String[] strheartpoint;
	private Button[] btnv;
	String[] isex = { "성별(전체)", "남자", "여자" };
	private String[] list = { "추가", "차단" };
	String sexstr = "";
	String univstr ="";
	private int personalindex;
	private Intent result;
	private BackPressCloseHandler back;
	
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	TextView tv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpfindfriend);
		li = (LinearLayout)findViewById(R.id.lihpfindfriend);
		tv1 = (TextView)findViewById(R.id.tvhpfindfriend1);
		btn1 = (Button) findViewById(R.id.btnfindfriend);
		hpdb = new HPDatabase(HPHomeFind.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.findfriend_blue);
				tv1.setBackgroundColor(Color.argb(153, 178, 235, 244));
				btn1.setBackgroundResource(R.drawable.btnfriendadd_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.findfriend_pupple);
				tv1.setBackgroundColor(Color.argb(153, 243, 97, 220));
				btn1.setBackgroundResource(R.drawable.btnfriendadd_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.findfriend_green);
				tv1.setBackgroundColor(Color.argb(153, 134, 229, 127));
				btn1.setBackgroundResource(R.drawable.btnfriendadd_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.findfriend_red);
				tv1.setBackgroundColor(Color.argb(153, 255, 167, 167));
				btn1.setBackgroundResource(R.drawable.btnfriendadd_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.findfriend_gold);
				tv1.setBackgroundColor(Color.argb(153, 229, 216, 92));
				btn1.setBackgroundResource(R.drawable.btnfriendadd_gold);
				break;
			}
		}
		
		back = new BackPressCloseHandler(this);
		
		et1 = (EditText) findViewById(R.id.etfindfriend);
		et2 = (EditText) findViewById(R.id.findet);
		listview = (ListView) findViewById(R.id.lvfindfriend);
		spin = (Spinner) findViewById(R.id.findspinner);

		arraylist = new ArrayList<String>();

		spinadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, isex);
		spin.setAdapter(spinadapter);

		et1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(8) });
		et2.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(5) });

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				final int listposition = position;

				AlertDialog.Builder dia1 = new AlertDialog.Builder(
						HPHomeFind.this);
				dia1.setItems(list, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
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
												strsplitid[listposition], "요청",
												null, null, null, null, null,null,
												null, null, "setfriend");

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
												.equals("frienderror-1")) {
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

										arraylist.clear();
										for (int i = 0; i < listview.getCount(); i++) {
											if (i == listview.getCount() - 1) {
												strsplitfriend[i] = "";
												strsplitsex[i] = "";
												strsplituinv_1[i] = "";
												strsplitphone[i] = "";
												strsplitid[i] = "";
												strheartpoint[i] ="";
												break;
											}
											if (i < listposition) {
												arraylist.add("닉네임 : "
														+ strsplitfriend[i]);
											} else {
												strsplitfriend[i] = strsplitfriend[i + 1];
												strsplitsex[i] = strsplitsex[i + 1];
												strsplituinv_1[i] = strsplituinv_1[i + 1];
												strsplitphone[i] = strsplitphone[i + 1];
												strsplitid[i] = strsplitid[i + 1];
												strheartpoint[i] = strheartpoint[i + 1];
												arraylist.add("닉네임 : "
														+ strsplitfriend[i]);
											}

										}

										handler2.sendEmptyMessage(7);

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
							break;
						case 1:
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
												strsplitid[listposition], "차단",
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

										arraylist.clear();
										for (int i = 0; i < listview.getCount(); i++) {
											if (i == listview.getCount() - 1) {
												strsplitfriend[i] = "";
												strsplitsex[i] = "";
												strsplituinv_1[i] = "";
												strsplitphone[i] = "";
												strsplitid[i] = "";
												strheartpoint[i] ="";
												break;
											}
											if (i < listposition) {
												arraylist.add("닉네임 : "
														+ strsplitfriend[i]);
											} else {
												strsplitfriend[i] = strsplitfriend[i + 1];
												strsplitsex[i] = strsplitsex[i + 1];
												strsplituinv_1[i] = strsplituinv_1[i + 1];
												strsplitphone[i] = strsplitphone[i + 1];
												strsplitid[i] = strsplitid[i + 1];
												strheartpoint[i] = strheartpoint[i+1];
												arraylist.add("닉네임 : "
														+ strsplitfriend[i]);
											}

										}

										handler2.sendEmptyMessage(7);

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
							break;
						default:
							break;
						}

					}
				});
				dia1.setCancelable(true);
				dia1.show();
			}

		});
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et1.getText().toString().isEmpty() && et1.getText().toString().length() > 2) {
					arraylist.clear();

					if (spin.getSelectedItem().equals(isex[0])) {
						sexstr="-1";
					}
					else if(spin.getSelectedItem().equals(isex[1]))
					{
						sexstr="0";
					}
					else
					{
						sexstr="1";
					}
					
					if(et2.getText().toString().equals(""))
					{
						univstr="-1";
					}
					else
					{
						univstr = et2.getText().toString();
					}
					
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

								HPObject hp = new HPObject(
										MainActivity.Loginid, null, et1
												.getText().toString(), sexstr,
										univstr, null, null,null,
										null, null, null, "findfriend");

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

								inhp = (HPObject) ois.readObject();
								strsplitfriend = inhp.message.split("/");
								strsplitsex = inhp.Sex.split("/");
								strsplituinv_1 = inhp.Univ_1.split("/");
								strsplitphone = inhp.Phone.split("/");
								strsplitid = inhp.ID.split("/");
								strheartpoint = inhp.HeartPoint.split("/");
								if (inhp.message.equals("falsefriend")) {
									handler2.sendEmptyMessage(1);
								} else {
									for (int i = 0; i < strsplitfriend.length; i++) {
										arraylist.add("닉네임 : "
												+ strsplitfriend[i]);
									}
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
				} else {
					mtoast = Toast.makeText(HPHomeFind.this,
							"검색어가 너무 짧습니다!", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
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
				btn1.setEnabled(true);
				break;
			case 2:
				btn1.performClick();
				break;
			}
		}
	};

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPHomeFind.this, "네트워크 상태가 원활하지 않습니다.",
						0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPHomeFind.this, "일치하는 정보가 없습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				arrayadapter = new MyAdapter(HPHomeFind.this,
						android.R.layout.simple_list_item_1, arraylist);
				listview.setAdapter(arrayadapter);
				arrayadapter.notifyDataSetChanged();
				break;
			case 2:
				arrayadapter = new MyAdapter(HPHomeFind.this,
						android.R.layout.simple_list_item_1, arraylist);
				listview.setAdapter(arrayadapter);
				arrayadapter.notifyDataSetChanged();
				et1.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
				mtoast = Toast.makeText(HPHomeFind.this, "검색성공", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(HPHomeFind.this,
						"이미 친구신청하였거나 친구인 회원입니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 4:
				mtoast = Toast.makeText(HPHomeFind.this,
						"축하드립니다! 서로 친구가 되셨어요!", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 5:
				mtoast = Toast.makeText(HPHomeFind.this, "친구요청이 완료되었습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 6:
				mtoast = Toast.makeText(HPHomeFind.this, "죄송합니다. 다시 시도해주세요", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 7:
				arrayadapter = new MyAdapter(HPHomeFind.this,
						android.R.layout.simple_list_item_1, arraylist);
				listview.setAdapter(arrayadapter);
				arrayadapter.notifyDataSetChanged();
				break;
			case 8:
				mtoast = Toast.makeText(HPHomeFind.this, "차단이 완료되었습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 9:
				mtoast = Toast.makeText(HPHomeFind.this, "이미 차단한 회원입니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			}
		}
	};

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
			btnv = new Button[m_arrayList.size()];
			tv = new TextView[m_arrayList.size()];
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				tv[i] = new TextView(m_context);
				btnv[i] = new Button(m_context);
				tv[i].setFocusable(false);
				tv[i].setClickable(false);
				btnv[i].setFocusable(false);
				btnv[i].setClickable(true);
				//btnv[i].setText("정보");
				
				if(c.moveToFirst())
				{
					switch(c.getInt(6))
					{
					case 0:
						btnv[i].setBackgroundResource(R.drawable.btninfo_blue);
						break;
					case 1:
						btnv[i].setBackgroundResource(R.drawable.btninfo_pupple);
						break;
					case 2:
						btnv[i].setBackgroundResource(R.drawable.btninfo_green);
						break;
					case 3:
						btnv[i].setBackgroundResource(R.drawable.btninfo_red);
						break;
					case 4:
						btnv[i].setBackgroundResource(R.drawable.btninfo_gold);
						break;
					}
				}
				
				btnv[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						LinearLayout abc = (LinearLayout) vi.inflate(
								R.layout.hpfindfriendlist, null);
						abc.setOrientation(LinearLayout.VERTICAL);

						TextView nic = (TextView) abc
								.findViewById(R.id.textView1);
						TextView sex = (TextView) abc
								.findViewById(R.id.textView2);
						TextView univ_1 = (TextView) abc
								.findViewById(R.id.textView3);
						TextView phone = (TextView) abc
								.findViewById(R.id.textView4);
						TextView heart = (TextView) abc.findViewById(R.id.textView5);
						
						nic.setTextSize(25);
						sex.setTextSize(25);
						univ_1.setTextSize(25);
						phone.setTextSize(25);
						heart.setTextSize(25);
						String sexstr = "";
						String phonestr = "";
						String starstr = "";

						for (int i = 0; i < (int) (strsplitphone[index]
								.length() / 2.0 + 0.5); i++) {
							starstr += "*";
						}

						if (strsplitsex[index].equals("0")) {
							sexstr = "남자";
						} else {
							sexstr = "여자";
						}

						phonestr = strsplitphone[index].substring(0,
								strsplitphone[index].length() / 2) + starstr;

						nic.setText("닉네임 : " + strsplitfriend[index]);
						sex.setText("성별 : " + sexstr);
						univ_1.setText("대학교 : " + strsplituinv_1[index] + "대학교");
						phone.setText("연락처 : " + phonestr);
						heart.setText("두근두근 : " + strheartpoint[index] + " 점");
						
						AlertDialog.Builder dia1 = new AlertDialog.Builder(
								HPHomeFind.this);
						dia1.setTitle("정보");
						dia1.setView(abc);
						dia1.setCancelable(true);
						dia1.setPositiveButton("친구추가",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (!MainActivity.connectthread.channel
												.isConnected()) {
											if (!MainActivity.connectthread
													.isAlive()) {
												MainActivity.connectthread = new HPConnectThread(
														MainActivity.context);
												MainActivity.connectthread
														.start();
											} else {
												if (MainActivity.connectthread
														.isInterrupted()) {
													MainActivity.connectthread = new HPConnectThread(
															MainActivity.context);
													MainActivity.connectthread
															.start();
												} else {
													MainActivity.connectthread
															.interrupt();
													MainActivity.connectthread = new HPConnectThread(
															MainActivity.context);
													MainActivity.connectthread
															.start();
												}
											}
										}

										if (Hpthread != null
												&& Hpthread.isAlive()) {
											Hpthread.interrupt();
										}

										Hpthread = new Thread(new Runnable() {

											@Override
											public void run() {
												// TODO 자동 생성된 메소드 스텁
												try {

													baos = new ByteArrayOutputStream();
													out = new ObjectOutputStream(
															baos);

													HPObject hp = new HPObject(
															MainActivity.Loginid,
															strsplitid[index],
															"요청", null, null,
															null, null, null,
															null, null,null,
															"setfriend");

													Object obc = hp;

													out.writeObject(obc);

													if (MainActivity.connectthread.channel
															.isConnected()) {
														MainActivity.connectthread.channel
																.write(ByteBuffer
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

													ois = new ObjectInputStream(
															bais);

													inhp = (HPObject) ois
															.readObject();

													if (inhp.message
															.equals("frienderror-1")) {
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

													arraylist.clear();
													for (int i = 0; i < listview
															.getCount(); i++) {
														if (i == listview
																.getCount() - 1) {
															strsplitfriend[i] = "";
															strsplitsex[i] = "";
															strsplituinv_1[i] = "";
															strsplitphone[i] = "";
															strsplitid[i] = "";
															strheartpoint[i] = "";
															break;
														}
														if (i < index) {
															arraylist
																	.add("닉네임 : "
																			+ strsplitfriend[i]);
														} else {
															strsplitfriend[i] = strsplitfriend[i + 1];
															strsplitsex[i] = strsplitsex[i + 1];
															strsplituinv_1[i] = strsplituinv_1[i + 1];
															strsplitphone[i] = strsplitphone[i + 1];
															strsplitid[i] = strsplitid[i + 1];
															strheartpoint[i] = strheartpoint[i+1];
															arraylist
																	.add("닉네임 : "
																			+ strsplitfriend[i]);
														}

													}

													handler2.sendEmptyMessage(7);

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

				tv[i].setText(m_arrayList.get(i));
				tv[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

				a1.addView(btnv[i]);
				a1.addView(tv[i]);

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
			toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}
}
