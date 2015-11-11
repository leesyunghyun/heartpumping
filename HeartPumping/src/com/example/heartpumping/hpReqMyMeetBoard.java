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
import android.content.DialogInterface.OnClickListener;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class hpReqMyMeetBoard extends Activity{

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

	String boardmyid;
	String[] teamname;
	
	HPObject inhp;
	Intent intent;
	
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpbanlist);
		li = (LinearLayout)findViewById(R.id.lihpbanlist);
		tv = (TextView)findViewById(R.id.bantv);
		hpdb = new HPDatabase(hpReqMyMeetBoard.this);
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
		intent = getIntent();
		boardmyid = intent.getStringExtra("boardid");
		lv1 = (ListView) findViewById(R.id.banlist);
		tv.setText("신청자 목록");
		boardarraylist = new ArrayList<String>();
		
		SetReqList();
		
	}

	public void SetReqList() {
		boardarraylist.clear();
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

					HPObject hp = new HPObject(MainActivity.Loginid, boardmyid,
							null, null, null, null, null, null, null, null,
							null, "findmyreqboard");

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
					
					teamname = inhp.message.split("/");
/*
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
					*/
					if (inhp.message.equals("falsemyreqboard")) {
						// handler2.sendEmptyMessage(1);
					} else {
						for (int i = 0; i < teamname.length; i++) {
							boardarraylist.add("팀 이름 : " + teamname[i]);
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
				mtoast = Toast.makeText(hpReqMyMeetBoard.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				break;
			case 2:
				boardarrayadapter = new MyAdapter(hpReqMyMeetBoard.this, android.R.layout.simple_list_item_1,boardarraylist);
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
						
					}
				});

				btnv2[i].setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

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
	
}
