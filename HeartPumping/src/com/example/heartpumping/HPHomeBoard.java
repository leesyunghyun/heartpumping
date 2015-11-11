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
import android.text.InputFilter;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartpumping.HPHomeBoard.MyAdapter1;
import com.example.heartpumping.HPHomeHome.BackPressCloseHandler;

public class HPHomeBoard extends Activity {

	int a = 0;
	Spinner spin1, spin2, spin3, spin4;
	Button btn1, btn2, btn3, btn4;
	EditText et1;
	ListView listview1, listview2;
	private ArrayList<String> arraylistmy;
	private ArrayAdapter<String> arrayadaptermy;
	private ArrayList<String> arraylist;
	private ArrayAdapter<String> arrayadapter;
	private ArrayAdapter<String>[] spinadapter;
	String[] iworld = { "지역(전체)", "서울", "대전", "대구", "부산", "울산", "광주", "전남",
			"전북", "경남", "경북", "충남", "충북", "제주", "강원", "경기" };
	String[] isex = { "성별(전체)", "남자", "여자" };
	String[] ipeople = { "인원(전체)", "1 & 1", "2 & 2", "3 & 3", "4 & 4" };
	String[] ipumping = { "두근(전체)", "20", "40", "60", "80", "100" };

	String[] setmylist ={ "요청목록" };
	
	private String writtenmy;
	private String writtenmynic;
	private String subjectmy;
	private String boardidmy;
	private String contentmy;
	private String meetzonemy;
	private String peoplecountmy;
	private String[] peoplemy;
	private String avrpointmy;
	private String[] okpeoplemy;
	private String[] nopeoplemy;
	private String[] okpeoplenic;
	private String[] nopeoplenic;
	private String[] waitpeoplenic;
	private String requestcount;
	private String view_able;
	
	private String[] writtenboard;
	private String[] writtenboardnic;
	private String[] boardid;
	private String[] subjectboard;
	private String[] contentboard;
	private String[] meetzoneboard;
	private String[] peoplecountboard;
	private String[] avrpointboard;
	private String[] peopleboard;
	
	private Intent result;
	private BackPressCloseHandler back;
	private Intent makeintent;

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
	Cursor c;
	SQLiteDatabase sql;
	TextView tv1,tv2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpmeetingboard);
		li = (LinearLayout)findViewById(R.id.lihpmeetingboard);
		tv1 = (TextView)findViewById(R.id.tvhpmeetingboard1);
		tv2 = (TextView)findViewById(R.id.tvhpmeetingboard2);
		btn1 = (Button) findViewById(R.id.meetingBtn2);
		btn2 = (Button) findViewById(R.id.meetingBtn3);
		btn3 = (Button) findViewById(R.id.meetingBtn4);
		btn4 = (Button) findViewById(R.id.meetingBtn5);
		spin1 = (Spinner) findViewById(R.id.meetingSp1);
		spin2 = (Spinner) findViewById(R.id.meetingSp2);
		spin3 = (Spinner) findViewById(R.id.meetingSp3);
		spin4 = (Spinner) findViewById(R.id.meetingSp4);
		
		
		hpdb = new HPDatabase(HPHomeBoard.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.board_blue);
				tv1.setBackgroundColor(Color.argb(153, 178, 235, 244));
				tv2.setBackgroundColor(Color.argb(153, 178, 235, 244));
				btn1.setBackgroundResource(R.drawable.btnsearch_blue);
				btn2.setBackgroundResource(R.drawable.btnbefore_blue);
				btn3.setBackgroundResource(R.drawable.btndr_blue);
				btn4.setBackgroundResource(R.drawable.btnnext_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.board_pupple);
				tv1.setBackgroundColor(Color.argb(153, 243, 97, 220));
				tv2.setBackgroundColor(Color.argb(153, 243, 97, 220));
				btn1.setBackgroundResource(R.drawable.btnsearch_pupple);
				btn2.setBackgroundResource(R.drawable.btnbefore_pupple);
				btn3.setBackgroundResource(R.drawable.btndr_pupple);
				btn4.setBackgroundResource(R.drawable.btnnext_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.board_green);
				tv1.setBackgroundColor(Color.argb(153, 134, 229, 127));
				tv2.setBackgroundColor(Color.argb(153, 134, 229, 127));
				btn1.setBackgroundResource(R.drawable.btnsearch_green);
				btn2.setBackgroundResource(R.drawable.btnbefore_green);
				btn3.setBackgroundResource(R.drawable.btndr_green);
				btn4.setBackgroundResource(R.drawable.btnnext_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.board_red);
				tv1.setBackgroundColor(Color.argb(153, 255, 167, 167));
				tv2.setBackgroundColor(Color.argb(153, 255, 167, 167));
				btn1.setBackgroundResource(R.drawable.btnsearch_red);
				btn2.setBackgroundResource(R.drawable.btnbefore_red);
				btn3.setBackgroundResource(R.drawable.btndr_red);
				btn4.setBackgroundResource(R.drawable.btnnext_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.board_gold);
				tv1.setBackgroundColor(Color.argb(153, 229, 216, 92));
				tv2.setBackgroundColor(Color.argb(153, 229, 216, 92));
				btn1.setBackgroundResource(R.drawable.btnsearch_gold);
				btn2.setBackgroundResource(R.drawable.btnbefore_gold);
				btn3.setBackgroundResource(R.drawable.btndr_gold);
				btn4.setBackgroundResource(R.drawable.btnnext_gold);
				break;
			}
		}
		
		back = new BackPressCloseHandler(this);
		
		
		et1 = (EditText) findViewById(R.id.meetingEt1);
		listview1 = (ListView) findViewById(R.id.meetingLV1);
		listview2 = (ListView) findViewById(R.id.meetingLV2);

		arraylist = new ArrayList<String>();
		arraylistmy = new ArrayList<String>();
		SetMyList();
		SetBoardList();

		spinadapter = new ArrayAdapter[4];
		spinadapter[0] = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, iworld); // 지역
		spinadapter[1] = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, isex); // 성별
		spinadapter[2] = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, ipeople); // 인원
		spinadapter[3] = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, ipumping); // 하트지수

		spin1.setAdapter(spinadapter[0]);
		spin2.setAdapter(spinadapter[1]);
		spin3.setAdapter(spinadapter[2]);
		spin4.setAdapter(spinadapter[3]);
		et1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(30) });

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				if (!et1.getText().toString().equals("")
						&& et1.getText().toString().length() > 2) {
					arraylist.clear();
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

								HPBoardObject board = new HPBoardObject(null,
										et1.getText().toString(), null, spin1
												.getSelectedItem().toString(),
										spin3.getSelectedItem().toString(),
										null, null, null, spin4
												.getSelectedItem().toString(),
										null, null, null, null, null);

								HPObject hp = new HPObject(
										MainActivity.Loginid, null, null, spin2.getSelectedItem().toString(),
										null, null, null, null, null, null,
										board, "finduserboard");

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
								MainActivity.connectthread.selector.select();
								MainActivity.connectthread.channel.read(data);

								bais = new ByteArrayInputStream(data.array());

								ois = new ObjectInputStream(bais);

								inhp = (HPObject) ois.readObject();

								writtenboard = inhp.board.Written.split("/");
								writtenboardnic = inhp.message.split("//");
								subjectboard = inhp.board.Subject.split("/");
								contentboard = inhp.board.Content.split("/");
								meetzoneboard = inhp.board.MeetZone.split("/");
								peoplecountboard = inhp.board.PeoPleCount.split("/");
								avrpointboard = inhp.board.AvrPoint.split("/");
								peopleboard = inhp.board.OkPeoPle.split("//");
								boardid = inhp.board.boardid.split("/");
								
								if (inhp.message.equals("falseuserboard")) {									
									handler.sendEmptyMessage(3);
									handler2.sendEmptyMessage(2);
								} else {
									for(int i=0;i<writtenboard.length;i++)
									{
										arraylist.add(subjectboard[i]);
									}
									handler2.sendEmptyMessage(2);
								}

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
				else
				{
					handler.sendEmptyMessage(2);
				}
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁

			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				makeintent = new Intent(HPHomeBoard.this, HPHomeMakeBoard.class);
				startActivityForResult(makeintent, 0);
			}
		});

		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁

			}
		});

		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(arraylistmy.get(0).equals("참여한 미팅기록이 없습니다."))
				{
					return ;
				}
				AlertDialog.Builder dlg = new AlertDialog.Builder(HPHomeBoard.this);
				dlg.setItems(setmylist, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						Intent setlist = new Intent(HPHomeBoard.this, hpReqMyMeetBoard.class);
						setlist.putExtra("boardid", boardidmy);
						startActivity(setlist);
					}
				});
				dlg.setPositiveButton("닫기", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.setCancelable(true);
				dlg.show();
			}
		});
		
		listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				final int index = position;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);
				a1.setOrientation(LinearLayout.VERTICAL);
				TextView tv1 = (TextView)a1.findViewById(R.id.textView1);
				TextView tv2 = (TextView)a1.findViewById(R.id.textView2);
				TextView tv3 = (TextView)a1.findViewById(R.id.textView3);
				TextView tv4 = (TextView)a1.findViewById(R.id.textView4);
				
				tv1.setText("게시판 제목 : " + subjectboard[position]);
				tv2.setText("작성자 : " + writtenboardnic[position]);
				tv3.setText("만남선호지역 : " + meetzoneboard[position]);
				tv4.setText("미팅인원 : " + peoplecountboard[position] + " & " + peoplecountboard[position]);
				
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				
				
				AlertDialog dlg = new AlertDialog.Builder(HPHomeBoard.this).create();
				dlg.setTitle("정보");
				dlg.setView(a1);
				dlg.setCancelable(true);
				dlg.setCanceledOnTouchOutside(true);
				dlg.setButton("자세히보기", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 자동 생성된 메소드 스텁
						Intent intent = new Intent(HPHomeBoard.this, HPBoardInfo.class);
						intent.putExtra("written", writtenboard[index]);
						intent.putExtra("writtennic", writtenboardnic[index]);
						intent.putExtra("subject", subjectboard[index]);
						intent.putExtra("content", contentboard[index]);
						intent.putExtra("meetzone", meetzoneboard[index]);
						intent.putExtra("peoplecount", peoplecountboard[index]);
						intent.putExtra("boardid", boardid[index]);
						intent.putExtra("listindex", index);
						intent.putExtra("flag", 3);
						startActivityForResult(intent, 0);
						dialog.dismiss();
					}
				});
				
			dlg.show();	
			}
		});
		
		
	}

	public void SetMyList() {
		arraylistmy.clear();
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
							null, null, null, null, null, null, null, null,
							null, "findmyboard");

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

					writtenmy = inhpmy.board.Written;
					writtenmynic = inhpmy.NicName;
					subjectmy = inhpmy.board.Subject;
					boardidmy = inhpmy.board.boardid;
					contentmy = inhpmy.board.Content;
					meetzonemy = inhpmy.board.MeetZone;
					peoplemy = inhpmy.board.PeoPle.split("/");
					peoplecountmy = inhpmy.board.PeoPleCount;
					avrpointmy = inhpmy.board.AvrPoint;
					okpeoplemy = inhpmy.board.OkPeoPle.split("/");
					nopeoplemy = inhpmy.board.NoPeoPle.split("/");
					requestcount = inhpmy.board.RequestCount;

					if (inhpmy.message.equals("falsereqboard")) {
						arraylistmy.add("참여한 미팅기록이 없습니다.");
						handler2.sendEmptyMessage(1);
					} else {
						arraylistmy.add(subjectmy);
						handler2.sendEmptyMessage(1);
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

	public void SetBoardList() {

	}

	public class MyAdapter1 extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapter1(Activity context, int textViewResourceId,
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
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
				li2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3));
				li3.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3));

				li1.setGravity(Gravity.CENTER);
				li2.setGravity(Gravity.CENTER);
				li3.setGravity(Gravity.CENTER);

				TextView tv1 = new TextView(m_context);
				TextView tv2 = new TextView(m_context);
				final Button btn = new Button(m_context);
				tv1.setFocusable(false);
				tv1.setText(m_arrayList.get(i));
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv1.setMaxLines(1);
				tv1.setEllipsize(TruncateAt.END);
				tv1.setClickable(false);
				tv2.setFocusable(false);
				tv2.setClickable(false);
				tv2.setText(requestcount);
				
				tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				li1.addView(tv1);
				li2.addView(tv2);
				li3.addView(btn);
				btn.setFocusable(false);
				btn.setClickable(true);

				if (MainActivity.Loginid.equals(writtenmy)) {
					//btn.setText("수정");
					btn.setTag("수정");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn.setBackgroundResource(R.drawable.btnmodify_blue);
							break;
						case 1:
							btn.setBackgroundResource(R.drawable.btnmodify_pupple);
							break;
						case 2:
							btn.setBackgroundResource(R.drawable.btnmodify_green);
							break;
						case 3:
							btn.setBackgroundResource(R.drawable.btnmodify_red);
							break;
						case 4:
							btn.setBackgroundResource(R.drawable.btnmodify_gold);
							break;
						}
					}
					
				} else {
					tv1.setText("(참여중)" + m_arrayList.get(i));
					//btn.setText("정보");
					btn.setTag("정보");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn.setBackgroundResource(R.drawable.btninfo_blue);
							break;
						case 1:
							btn.setBackgroundResource(R.drawable.btninfo_pupple);
							break;
						case 2:
							btn.setBackgroundResource(R.drawable.btninfo_green);
							break;
						case 3:
							btn.setBackgroundResource(R.drawable.btninfo_red);
							break;
						case 4:
							btn.setBackgroundResource(R.drawable.btninfo_gold);
							break;
						}
					}
					
					if (writtenmy.equals("falsereqboard")) {
						tv1.setText(m_arrayList.get(i));
						tv2.setText("");
						btn.setVisibility(View.INVISIBLE);
					}
				}

				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (btn.getTag().equals("수정")) {
							AlertDialog.Builder dlg = new AlertDialog.Builder(
									HPHomeBoard.this);
							dlg.setTitle("선택");
							dlg.setCancelable(true);
							String[] str = { "수정", "상태" };
							dlg.setItems(str, new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 자동 생성된 메소드 스텁
									switch (which) {
									case 0:
										Intent intent = new Intent(
												HPHomeBoard.this,
												HPBoardInfo.class);
										intent.putExtra("written", writtenmy);
										intent.putExtra("writtennic",
												writtenmynic);
										intent.putExtra("subject", subjectmy);
										intent.putExtra("content", contentmy);
										intent.putExtra("meetzone", meetzonemy);
										intent.putExtra("peoplecount",
												peoplecountmy);
										intent.putExtra("boardid", boardidmy);
										intent.putExtra("flag", 1);
										startActivityForResult(intent, 0);
										break;
									case 1:
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
															boardidmy, null,
															null, null, null,
															null, null, null,
															null, null,
															"boardinfo");

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

													inhpmy = (HPObject) ois
															.readObject();

													okpeoplenic = inhpmy.NicName
															.split("/");
													nopeoplenic = inhpmy.Sex
															.split("/");
													waitpeoplenic = inhpmy.ID
															.split("/");
													view_able = inhpmy.board.Viewable;
													if (inhpmy.message
															.equals("falseboardinfo")) {
														// handler2.sendEmptyMessage(1);
													} else {
														// handler2.sendEmptyMessage(1);
													}
													handler2.sendEmptyMessage(3);
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
										break;
									}
								}
							});
							dlg.show();
						} else if (btn.getTag().equals("정보")) {
							AlertDialog.Builder dlg = new AlertDialog.Builder(
									HPHomeBoard.this);
							dlg.setTitle("선택");
							dlg.setCancelable(true);
							String[] str = { "정보", "상태" };
							dlg.setItems(str, new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 자동 생성된 메소드 스텁
									switch (which) {
									case 0:
										Intent intent = new Intent(
												HPHomeBoard.this,
												HPBoardInfo.class);
										intent.putExtra("written", writtenmy);
										intent.putExtra("writtennic",
												writtenmynic);
										intent.putExtra("subject", subjectmy);
										intent.putExtra("content", contentmy);
										intent.putExtra("meetzone", meetzonemy);
										intent.putExtra("peoplecount",
												peoplecountmy);
										intent.putExtra("boardid", boardidmy);
										intent.putExtra("flag", 0);
										startActivityForResult(intent, 0);
										break;
									case 1:
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
															boardidmy, null,
															null, null, null,
															null, null, null,
															null, null,
															"boardinfo");

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

													inhpmy = (HPObject) ois
															.readObject();

													okpeoplenic = inhpmy.NicName
															.split("/");
													nopeoplenic = inhpmy.Sex
															.split("/");
													waitpeoplenic = inhpmy.ID
															.split("/");
													view_able = inhpmy.board.Viewable;
													if (inhpmy.message
															.equals("falseboardinfo")) {
														// handler2.sendEmptyMessage(1);
													} else {
														// handler2.sendEmptyMessage(1);
													}
													handler2.sendEmptyMessage(4);
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
										break;
									}
								}
							});
							dlg.show();

						}
					}
				});

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

	public class MyAdapter2 extends ArrayAdapter {

		private Activity m_context;
		private ArrayList<String> m_arrayList;
		private ArrayList<LinearLayout> listlayout;

		public MyAdapter2(Activity context, int textViewResourceId,
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
				LinearLayout li4 = new LinearLayout(m_context);
				
				li1.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
				li2.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2));
				li3.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2));
				li4.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2));

				li1.setMinimumHeight(105);
				li1.setGravity(Gravity.CENTER);
				li2.setGravity(Gravity.CENTER);
				li3.setGravity(Gravity.CENTER);
				li4.setGravity(Gravity.CENTER);

				TextView tv1 = new TextView(m_context);
				TextView tv2 = new TextView(m_context);
				TextView tv3 = new TextView(m_context);
				TextView tv4 = new TextView(m_context);

				tv1.setFocusable(false);
				tv1.setMaxLines(1);
				tv1.setEllipsize(TruncateAt.END);
				tv1.setClickable(false);
				tv1.setText(m_arrayList.get(i));
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				
				tv2.setFocusable(false);
				tv2.setText(writtenboardnic[i]);
				tv2.setMaxLines(1);
				tv2.setEllipsize(TruncateAt.END);
				tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				tv2.setClickable(false);
				
				tv3.setFocusable(false);
				tv3.setText(peoplecountboard[i] + " & " + peoplecountboard[i]);
				tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				tv3.setClickable(false);
				
				tv4.setFocusable(false);
				tv4.setText(avrpointboard[i]);
				tv4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				tv4.setClickable(false);
				
				
				li1.addView(tv1);
				li2.addView(tv2);
				li3.addView(tv3);
				li4.addView(tv4);		
				
				a1.addView(li1);
				a1.addView(li2);
				a1.addView(li3);
				a1.addView(li4);

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
				mtoast = Toast.makeText(HPHomeBoard.this, "오류발생, 다시 시도해주세요", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPHomeBoard.this, "정상적으로 처리되었습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 2:
				mtoast = Toast.makeText(HPHomeBoard.this, "검색어가 너무 짧습니다!", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(HPHomeBoard.this, "일치하는 정보가 없습니다.\n검색조건을 수정해보세요!", 0);
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
				mtoast = Toast.makeText(HPHomeBoard.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				arrayadaptermy = new MyAdapter1(HPHomeBoard.this,
						android.R.layout.simple_list_item_1, arraylistmy);
				listview1.setAdapter(arrayadaptermy);
				arrayadaptermy.notifyDataSetChanged();
				break;
			case 2:
				arrayadapter = new MyAdapter2(HPHomeBoard.this,
						android.R.layout.simple_list_item_1, arraylist);
				listview2.setAdapter(arrayadapter);
				arrayadapter.notifyDataSetChanged();
				break;
			case 3:
				AlertDialog dlg2 = new AlertDialog.Builder(HPHomeBoard.this)
						.create();
				dlg2.setCancelable(true);
				dlg2.setTitle("정보");
				dlg2.setCanceledOnTouchOutside(true);
				String view = "";
				String ok = "";
				String no = "";
				String wait = "";
				String view_state = "";
				for (int i = 0; i < okpeoplenic.length; i++) {
					ok += "닉네임 : " + okpeoplenic[i] + "\n";
				}

				for (int i = 0; i < nopeoplenic.length; i++) {
					if (!nopeoplenic[i].equals("falseboardinfo"))
						no += "닉네임 : " + nopeoplenic[i] + "\n";
					else
						no = "없음\n";
				}

				for (int i = 0; i < waitpeoplenic.length; i++) {
					if (!waitpeoplenic[i].equals("falseboardinfo"))
						wait += "닉네임 : " + waitpeoplenic[i] + "\n";
					else
						wait = "없음\n";
				}

				if (view_able.equals("1")) {
					view = "비공개로 전환";
					view_state = "공개";
				} else {
					view = "공개로 전환";
					view_state = "비공개";
				}

				dlg2.setMessage("-참석자-\n" + ok + "\n-불참석-\n" + no + "\n-대기-\n"
						+ wait + "\n현재 상태 : " + view_state);

				dlg2.setButton(view, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 자동 생성된 메소드 스텁
						if(view_able.equals("1"))
						{
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

										HPObject hp = new HPObject(boardidmy, null,
												null, null, null, null, null, null,
												null, null, null, "pushview");

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

										HPObject push = (HPObject) ois.readObject();

										if (push.message.equals("falsepush")) {
											handler.sendEmptyMessage(0);
										} else {
											handler.sendEmptyMessage(1);
										}
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
						else
						{
							AlertDialog.Builder dlgpush = new AlertDialog.Builder(HPHomeBoard.this);
							dlgpush.setCancelable(false);
							dlgpush.setTitle("경고");
							dlgpush.setMessage("정말 강제로 공개전환하시겠습니까?\n강제로 공개전환하신다면 현재 인원으로 게시글이 표시됩니다.");
							dlgpush.setPositiveButton("예", new OnClickListener() {
								
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

												HPObject hp = new HPObject(boardidmy, null,
														null, null, null, null, null, null,
														null, null, null, "pushview");

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

												HPObject push = (HPObject) ois.readObject();

												if (push.message.equals("falsepush")) {
													handler.sendEmptyMessage(0);
												} else {
													handler.sendEmptyMessage(1);
												}
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
							dlgpush.setNegativeButton("아니요",new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO 자동 생성된 메소드 스텁
									dialog.dismiss();
								}
							});
							
							dlgpush.show();
						}
						
					}
				});
				dlg2.show();
				break;
			case 4:
				AlertDialog dlg3 = new AlertDialog.Builder(HPHomeBoard.this)
						.create();
				dlg3.setCancelable(true);
				dlg3.setTitle("정보");
				dlg3.setCanceledOnTouchOutside(true);
				String ok2 = "";
				String no2 = "";
				String wait2 = "";
				String view_state2 = "";
				for (int i = 0; i < okpeoplenic.length; i++) {
					ok2 += "닉네임 : " + okpeoplenic[i] + "\n";
				}

				for (int i = 0; i < nopeoplenic.length; i++) {
					if (!nopeoplenic[i].equals("falseboardinfo"))
						no2 += "닉네임 : " + nopeoplenic[i] + "\n";
					else
						no2 = "없음\n";
				}

				for (int i = 0; i < waitpeoplenic.length; i++) {
					if (!waitpeoplenic[i].equals("falseboardinfo"))
						wait2 += "닉네임 : " + waitpeoplenic[i] + "\n";
					else
						wait2 = "없음\n";
				}

				if (view_able.equals("1")) {
					view_state2 = "공개";
				} else {
					view_state2 = "비공개";
				}

				dlg3.setMessage("-참석자-\n" + ok2 + "\n-불참석-\n" + no2
						+ "\n-대기-\n" + wait2 + "\n현재 상태 : " + view_state2);

				dlg3.setButton("닫기", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog2, int which) {
						// TODO 자동 생성된 메소드 스텁
						dialog2.dismiss();
					}
				});
				dlg3.show();
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 자동 생성된 메소드 스텁
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 1) {
				SetMyList();
			}
		}
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
}
