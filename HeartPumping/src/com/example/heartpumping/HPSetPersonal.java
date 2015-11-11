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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HPSetPersonal extends Activity {

	Button btn1;
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	int nicflag = 0;
	Intent result;

	LinearLayout li;

	ListView list;
	ArrayAdapter<String> adapter;
	ArrayList<String> arraylist;

	CheckBox[] chb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpsetpersonal);
		li = (LinearLayout) findViewById(R.id.lihpsetpersonal);
		list = (ListView) findViewById(R.id.listpersonal);
		btn1 = (Button) findViewById(R.id.btnbreak);
		arraylist = new ArrayList<String>();
		arraylist.add("아이디저장");
		arraylist.add("자동로그인");
		arraylist.add("닉네임 검색 허용");

		adapter = new MyAdapter(HPSetPersonal.this,
				android.R.layout.simple_list_item_1, arraylist);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		hpdb = new HPDatabase(HPSetPersonal.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);

		if (c.moveToFirst()) {
			switch (c.getInt(6)) {
			case 0:
				li.setBackgroundResource(R.drawable.myinfo_blue);
				btn1.setBackgroundResource(R.drawable.btnmemberout_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.myinfo_pupple);
				btn1.setBackgroundResource(R.drawable.btnmemberout_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.myinfo_green);
				btn1.setBackgroundResource(R.drawable.btnmemberout_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.myinfo_red);
				btn1.setBackgroundResource(R.drawable.btnmemberout_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.myinfo_gold);
				btn1.setBackgroundResource(R.drawable.btnmemberout_gold);
				break;
			}
		}
		SetCheck();
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (chb[position].isChecked()) {
					chb[position].setChecked(false);
				} else {
					chb[position].setChecked(true);
				}

			}
		});

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dlg = new AlertDialog.Builder(
						HPSetPersonal.this);
				dlg.setTitle("경고!");
				dlg.setMessage("탈퇴하면 현재 정보가 모두 사라지며 복구할 수 없습니다. 또한 재가입은 7일 이후 가능합니다.\n회원탈퇴를 계속하시겠습니까?");
				dlg.setCancelable(false);
				dlg.setPositiveButton("예", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
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
											MainActivity.Loginid, null, null,
											null, null, null, null, null, null,
											null, null, "removemember");

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

									HPObject inhp = (HPObject) ois.readObject();

									if (inhp.message.equals("falseremove")) {
										handler.sendEmptyMessage(2);
									} else {
										handler.sendEmptyMessage(3);
									}

									Hpthread.interrupt();

								} catch (Exception e) {
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
				dlg.setNegativeButton("아니요", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dlg.show();
			}
		});
	}

	@Override
	public void onBackPressed() { // TODO 자동 생성된 메소드 스텁
		super.onBackPressed();
		if (chb[0].isChecked()) {
			sql.execSQL("update personal set saveid = 1 where id='set';");
		} else {
			sql.execSQL("update personal set saveid = 0 where id='set';");
		}

		if (chb[1].isChecked()) {
			sql.execSQL("update personal set autologin = 1 where id='set';");
		} else {
			sql.execSQL("update personal set autologin = 0 where id='set';");
		}

		if (chb[2].isChecked()) {
			sql.execSQL("update personal set searchnic = 1 where id='set';");
			nicflag = 1;
		} else {
			sql.execSQL("update personal set searchnic = 0 where id='set';");
			nicflag = 0;
		}

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
			public void run() { // TODO 자동 생성된 메소드 스텁
				try {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject hp = new HPObject(MainActivity.Loginid,
							Integer.toString(nicflag), null, null, null, null,
							null, null, null, null, null, "searchflag");

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

					HPObject inhp = (HPObject) ois.readObject();

					if (inhp.message.equals("falsesearchflag")) { //
						handler.sendEmptyMessage(0);
					} else { // handler.sendEmptyMessage(1);

					}

					Hpthread.interrupt();

				} catch (Exception e) { // TODO 자동 생성된 catch 블록
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

	public void SetCheck() {

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
			public void run() { // TODO 자동 생성된 메소드 스텁
				try {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject hp = new HPObject(MainActivity.Loginid, null,
							null, null, null, null, null, null, null, null,
							null, "joinsearchnic");

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

					HPObject inhp = (HPObject) ois.readObject();

					if (inhp.message.equals("falsesearchflag")) { //
						handler.sendEmptyMessage(0);
					} else { // handler.sendEmptyMessage(1);
						if (inhp.message.equals("1")) {
							chb[2].setChecked(true);
							nicflag = 1;
						}
					}

					Hpthread.interrupt();

				} catch (Exception e) { // TODO 자동 생성된 catch 블록
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

		if (c.moveToFirst()) {
			if (c.getInt(1) == 1) {
				chb[0].setChecked(true);
			}

			if (c.getInt(2) == 1) {
				chb[1].setChecked(true);
			}

		} else {

		}

		if (!chb[0].isChecked()) {
			chb[1].setChecked(false);
			chb[1].setEnabled(false);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPSetPersonal.this,
						"닉네임 검색 설정에 실패하였습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPSetPersonal.this, "닉네임 검색 설정 성공", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 2:
				mtoast = Toast.makeText(HPSetPersonal.this,
						"회원탈퇴에 실패하였습니다. 다시 시도해주세요.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(HPSetPersonal.this,
						"회원탈퇴가 정상적으로 이루어졌습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				chb[0].setChecked(false);
				chb[1].setChecked(false);
				chb[1].setEnabled(false);
				chb[2].setChecked(false);

				if (chb[0].isChecked()) {
					sql.execSQL("update personal set saveid = 1 where id='set';");
				} else {
					sql.execSQL("update personal set saveid = 0 where id='set';");
				}

				if (chb[1].isChecked()) {
					sql.execSQL("update personal set autologin = 1 where id='set';");
				} else {
					sql.execSQL("update personal set autologin = 0 where id='set';");
				}

				if (chb[2].isChecked()) {
					sql.execSQL("update personal set searchnic = 1 where id='set';");
					nicflag = 1;
				} else {
					sql.execSQL("update personal set searchnic = 0 where id='set';");
					nicflag = 0;
				}

				result = new Intent();
				result.putExtra("finish", "finish");
				setResult(0, result);
				finish();
				HPSetPersonal.this.onDestroy();
				break;
			}
		}
	};

	Handler handler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mtoast = Toast.makeText(HPSetPersonal.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
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
			chb = new CheckBox[m_arrayList.size()];

			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				a1.setGravity(Gravity.CENTER);
				a1.setMinimumHeight(105);
				chb[i] = new CheckBox(m_context);
				chb[i].setFocusable(false);
				chb[i].setClickable(false);
				chb[i].setText(m_arrayList.get(i));
				chb[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

				chb[i].setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						switch (index) {
						case 0:
							if (c.moveToFirst()) {
								switch (c.getInt(1)) {
								case 0:
									sql.execSQL("update personal set saveid = 1 where id='set';");
									break;
								case 1:
									sql.execSQL("update personal set saveid = 0 where id='set';");
									break;
								}

								if (!isChecked) {
									chb[1].setChecked(false);
									chb[1].setEnabled(false);
									switch (c.getInt(2)) {
									case 1:
										sql.execSQL("update personal set autologin = 0 where id='set';");
										break;
									}
								} else {
									chb[1].setEnabled(true);
								}
							}
							break;
						case 1:
							if (c.moveToFirst()) {
								switch (c.getInt(2)) {
								case 0:
									sql.execSQL("update personal set autologin = 1 where id='set';");
									break;
								case 1:
									sql.execSQL("update personal set autologin = 0 where id='set';");
									break;
								}
							}
							break;
						case 2:
							if (c.moveToFirst()) {
								switch (c.getInt(3)) {
								case 0:
									sql.execSQL("update personal set searchnic = 1 where id='set';");
									break;
								case 1:
									sql.execSQL("update personal set searchnic = 0 where id='set';");
									break;
								}
							}
							break;
						}
					}
				});

				a1.addView(chb[i]);

				listlayout.add(a1);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return listlayout.get(position);

		}
	}

}
