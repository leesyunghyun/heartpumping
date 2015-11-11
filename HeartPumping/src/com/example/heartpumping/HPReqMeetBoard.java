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

public class HPReqMeetBoard extends Activity {

	TextView tv;

	ListView lv1;
	public ArrayList<String> boardarraylist;
	public ArrayAdapter<String> boardarrayadapter;
	Button btnv1[];
	Button btnv2[];

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;

	private String[] written;
	private String[] subject;
	private String[] boardid;
	private String[] content;
	private String[] meetzone;
	private String[] peoplecount;
	private String[] avrpoint;
	private String[] okpeople;
	private String[] nopeople;
	private String[] writtennic;
	
	HPObject inhp;
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpbanlist);
		lv1 = (ListView) findViewById(R.id.banlist);
		tv = (TextView) findViewById(R.id.bantv);
		li = (LinearLayout)findViewById(R.id.lihpbanlist);
		
		hpdb = new HPDatabase(HPReqMeetBoard.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.meetingrequest_blue);
				tv.setBackgroundColor(Color.argb(153, 178, 235, 244));
				break;
			case 1:
				li.setBackgroundResource(R.drawable.meetingrequest_pupple);
				tv.setBackgroundColor(Color.argb(153, 243, 97, 220));
				break;
			case 2:
				li.setBackgroundResource(R.drawable.meetingrequest_green);
				tv.setBackgroundColor(Color.argb(153, 134, 229, 127));
				break;
			case 3:
				li.setBackgroundResource(R.drawable.meetingrequest_red);
				tv.setBackgroundColor(Color.argb(153, 255, 167, 167));
				break;
			case 4:
				li.setBackgroundResource(R.drawable.meetingrequest_gold);
				tv.setBackgroundColor(Color.argb(153, 229, 216, 92));
				break;
			}
		}
		
		tv.setText("미팅요청목록");
		boardarraylist = new ArrayList<String>();
		
		
		
		SetBoardList();

		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				final int index = position;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);
				a1.setOrientation(LinearLayout.VERTICAL);
				TextView tv1 = (TextView) a1.findViewById(R.id.textView1);
				TextView tv2 = (TextView) a1.findViewById(R.id.textView2);
				TextView tv3 = (TextView) a1.findViewById(R.id.textView3);
				TextView tv4 = (TextView) a1.findViewById(R.id.textView4);
				TextView tv5 = (TextView) a1.findViewById(R.id.textView5);

				tv5.setVisibility(View.GONE);
				tv1.setText("게시판 제목 : " + subject[position]);
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv2.setText("작성자 : " + writtennic[position]);
				tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv3.setText("만남선호지역 : " + meetzone[position]);
				tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				tv4.setText("미팅인원 : " + peoplecount[position] + " & "
						+ peoplecount[position]);
				tv4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

				AlertDialog dia1 = new AlertDialog.Builder(
						HPReqMeetBoard.this).create();
				dia1.setCancelable(true);
				dia1.setTitle("정보");
				dia1.setCanceledOnTouchOutside(true);
				dia1.setView(a1);
				dia1.setButton("자세히 보기", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						Intent intent = new Intent(HPReqMeetBoard.this, HPBoardInfo.class);
						intent.putExtra("written", written[index]);
						intent.putExtra("writtennic", writtennic[index]);
						intent.putExtra("subject", subject[index]);
						intent.putExtra("content", content[index]);
						intent.putExtra("meetzone", meetzone[index]);
						intent.putExtra("peoplecount", peoplecount[index]);
						intent.putExtra("boardid", boardid[index]);
						intent.putExtra("listindex", index);
						startActivityForResult(intent, 0);
						dialog.dismiss();
						
					}
				});
				dia1.show();

			}
		});
	}

	public void SetBoardList() {

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
							null, "findreqboard");

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

					written = inhp.board.Written.split("/");
					subject = inhp.board.Subject.split("/");
					boardid = inhp.board.boardid.split("/");
					content = inhp.board.Content.split("/");
					meetzone = inhp.board.MeetZone.split("/");
					peoplecount = inhp.board.PeoPleCount.split("/");
					avrpoint = inhp.board.AvrPoint.split("/");
					okpeople = inhp.board.OkPeoPle.split("/");
					nopeople = inhp.board.NoPeoPle.split("/");
					writtennic = inhp.NicName.split("/");
					
					if (inhp.message.equals("falsereqboard")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for (int i = 0; i < written.length; i++) {
							boardarraylist.add("게시판 제목 : " + subject[i]);
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
				mtoast = Toast.makeText(HPReqMeetBoard.this,
						"이미 미팅에 참여중이거나 참여할 수 없는 상태입니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
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
				mtoast = Toast.makeText(HPReqMeetBoard.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				boardarrayadapter.notifyDataSetChanged();
				break;
			case 2:
				boardarrayadapter = new MyAdapter(HPReqMeetBoard.this,
						android.R.layout.simple_list_item_1, boardarraylist);
				lv1.setAdapter(boardarrayadapter);
				boardarrayadapter.notifyDataSetChanged();
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
			btnv1 = new Button[m_arrayList.size()];
			btnv2 = new Button[m_arrayList.size()];
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				TextView tv = new TextView(m_context);
				btnv1[i] = new Button(m_context);
				btnv2[i] = new Button(m_context);
				btnv1[i].setFocusable(false);
				btnv1[i].setClickable(true);
				btnv2[i].setFocusable(false);
				btnv2[i].setClickable(true);
				tv.setFocusable(false);
				tv.setClickable(false);

				//btnv1[i].setText("수락");
				
				//btnv2[i].setText("거절");
				
				
				if(c.moveToFirst())
				{
					switch(c.getInt(6))
					{
					case 0:
						btnv1[i].setBackgroundResource(R.drawable.btnagree_blue);
						btnv2[i].setBackgroundResource(R.drawable.btnrefuse_blue);
						break;
					case 1:
						btnv1[i].setBackgroundResource(R.drawable.btnagree_pupple);
						btnv2[i].setBackgroundResource(R.drawable.btnrefuse_pupple);
						break;
					case 2:
						btnv1[i].setBackgroundResource(R.drawable.btnagree_green);
						btnv2[i].setBackgroundResource(R.drawable.btnrefuse_green);
						break;
					case 3:
						btnv1[i].setBackgroundResource(R.drawable.btnagree_red);
						btnv2[i].setBackgroundResource(R.drawable.btnrefuse_red);
						break;
					case 4:
						btnv1[i].setBackgroundResource(R.drawable.btnagree_gold);
						btnv2[i].setBackgroundResource(R.drawable.btnrefuse_gold);
						break;
					}
				}
				
				
				DisplayMetrics outMetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
				int pixel = (int) (1.5 * outMetrics.densityDpi);
				tv.setWidth(pixel);

				btnv1[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder dia1 = new AlertDialog.Builder(
								HPReqMeetBoard.this);
						dia1.setTitle("정보");
						dia1.setMessage("미팅요청을 수락하시겠습니까?");
						dia1.setCancelable(true);
						dia1.setPositiveButton("예", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
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
													boardid[index], "ok", null,
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
												boardarraylist.clear();

												for (int i = 0; i < lv1
														.getCount(); i++) {
													if (i == lv1.getCount() - 1) {
														writtennic[i] = "";
														written[i] = "";
														subject[i] = "";
														boardid[i] = "";
														content[i] = "";
														meetzone[i] = "";
														peoplecount[i] = "";
														avrpoint[i] = "";
														break;
													}
													if (i < index) {
														boardarraylist
																.add("게시판 제목 : "
																		+ subject[i]);
													} else {
														writtennic[i] = writtennic[i+1];
														written[i] = written[i + 1];
														subject[i] = subject[i + 1];
														boardid[i] = boardid[i + 1];
														content[i] = content[i + 1];
														meetzone[i] = meetzone[i + 1];
														peoplecount[i] = peoplecount[i + 1];
														avrpoint[i] = avrpoint[i + 1];
														boardarraylist
																.add("게시판 제목 : "
																		+ subject[i]);
													}

												}
												handler2.sendEmptyMessage(2);
											}
										} catch (Exception e) {

										}
									}
								});

								Hpthread.start();
							}
						});

						dia1.setNegativeButton("아니요", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						dia1.show();
					}
				});

				btnv2[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						AlertDialog.Builder dia1 = new AlertDialog.Builder(
								HPReqMeetBoard.this);
						dia1.setTitle("정보");
						dia1.setMessage("미팅요청을 거절하시겠습니까?");
						dia1.setCancelable(true);
						dia1.setPositiveButton("예", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
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
													boardid[index], "no", null,
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
												boardarraylist.clear();

												for (int i = 0; i < lv1
														.getCount(); i++) {
													if (i == lv1.getCount() - 1) {
														writtennic[i] = "";
														written[i] = "";
														subject[i] = "";
														boardid[i] = "";
														content[i] = "";
														meetzone[i] = "";
														peoplecount[i] = "";
														avrpoint[i] = "";
														break;
													}
													if (i < index) {
														boardarraylist
																.add("게시판 제목 : "
																		+ subject[i]);
													} else {
														writtennic[i] = writtennic[i+1];
														written[i] = written[i + 1];
														subject[i] = subject[i + 1];
														boardid[i] = boardid[i + 1];
														content[i] = content[i + 1];
														meetzone[i] = meetzone[i + 1];
														peoplecount[i] = peoplecount[i + 1];
														avrpoint[i] = avrpoint[i + 1];
														boardarraylist
																.add("게시판 제목 : "
																		+ subject[i]);
													}

												}
												handler2.sendEmptyMessage(2);
											}
										} catch (Exception e) {

										}
									}
								});

								Hpthread.start();
							}
						});

						dia1.setNegativeButton("아니요", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
				a1.addView(btnv1[i]);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 자동 생성된 메소드 스텁
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0)
		{
			if(resultCode == 1)
			{
				int index = data.getIntExtra("listposition", 0);
				boardarraylist.clear();

				for (int i = 0; i < lv1
						.getCount(); i++) {
					if (i == lv1.getCount() - 1) {
						writtennic[i] = "";
						written[i] = "";
						subject[i] = "";
						boardid[i] = "";
						content[i] = "";
						meetzone[i] = "";
						peoplecount[i] = "";
						avrpoint[i] = "";
						break;
					}
					if (i < index) {
						boardarraylist
								.add("게시판 제목 : "
										+ subject[i]);
					} else {
						writtennic[i] = writtennic[i+1];
						written[i] = written[i + 1];
						subject[i] = subject[i + 1];
						boardid[i] = boardid[i + 1];
						content[i] = content[i + 1];
						meetzone[i] = meetzone[i + 1];
						peoplecount[i] = peoplecount[i + 1];
						avrpoint[i] = avrpoint[i + 1];
						boardarraylist
								.add("게시판 제목 : "
										+ subject[i]);
					}

				}
				handler2.sendEmptyMessage(2);
			}
		}
	}

}
