package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.example.heartpumping.HPHomeHome.MyAdapterMy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HPBanList extends Activity {

	ListView lv1;
	public ArrayList<String> banarraylist;
	public ArrayAdapter<String> banarrayadapter;

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	private String[] banlist;
	private String[] banlistid;
	private String[] list = {"차단해제"};
	HPObject inhp;

	Cursor c;
	HPDatabase hpdb;
	SQLiteDatabase sql;
	
	LinearLayout li;
	TextView tv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 자동 생성된 메소드 스텁
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpbanlist);
		li = (LinearLayout)findViewById(R.id.lihpbanlist);
		lv1 = (ListView) findViewById(R.id.banlist);
		tv1 = (TextView) findViewById(R.id.bantv);

		hpdb = new HPDatabase(HPBanList.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.friendblock_blue);
				tv1.setBackgroundColor(Color.argb(153, 178, 235, 244));
				break;
			case 1:
				li.setBackgroundResource(R.drawable.friendblock_pupple);
				tv1.setBackgroundColor(Color.argb(153, 243, 97, 220));
				break;
			case 2:
				li.setBackgroundResource(R.drawable.friendblock_green);
				tv1.setBackgroundColor(Color.argb(153, 134, 229, 127));
				break;
			case 3:
				li.setBackgroundResource(R.drawable.friendblock_red);
				tv1.setBackgroundColor(Color.argb(153, 255, 167, 167));
				break;
			case 4:
				li.setBackgroundResource(R.drawable.friendblock_gold);
				tv1.setBackgroundColor(Color.argb(153, 229, 216, 92));
				break;
				
			}
		}
		
		banarraylist = new ArrayList<String>();

		SetBanList();
		
		lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 자동 생성된 메소드 스텁
				final int index = position;
				AlertDialog.Builder dia1 = new AlertDialog.Builder(
						HPBanList.this);
				dia1.setItems(list,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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

											HPObject hp = new HPObject(MainActivity.Loginid, banlistid[index],
													"삭제", null, null, null, null, null, null, null,
													null,"cancelban");

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
											if (inhp.message.equals("falseresult")) {
												// handler2.sendEmptyMessage(1);
											} else {
												banarraylist.remove(index);
												for(int i=index;i<banlistid.length;i++)
												{
													if(i == banlistid.length-1)
													{
														banlistid[i] = "";
														break;
													}
													banlistid[i] = banlistid[i+1];
												}
												
												 handler2.sendEmptyMessage(1);
											}
										} catch (Exception e) {

										}
									}
								});
								
								Hpthread.start();
							}

				});
				dia1.setCancelable(true);
				dia1.show();
				
			}
		});
	}

	public void SetBanList() {
		
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
							null,"findban");

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

					banlist = inhp.message.split("/");
					banlistid = inhp.ID.split("/");
					if (inhp.message.equals("falseresult")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for(int i=0;i<banlist.length;i++)
						{
							banarraylist.add("닉네임 : " + banlist[i]);
						}
						banarrayadapter = new ArrayAdapter<String>(HPBanList.this, android.R.layout.simple_list_item_1,banarraylist);

						lv1.setAdapter(banarrayadapter);
						
						// handler2.sendEmptyMessage(2);
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
				mtoast = Toast.makeText(HPBanList.this, "네트워크 상태가 원활하지 않습니다.",
						0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				banarrayadapter.notifyDataSetChanged();
				break;
			case 2:
				break;
			}
		}
	};
}
