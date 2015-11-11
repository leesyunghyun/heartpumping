package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import com.example.heartpumping.HPReqMeetBoard.MyAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HPTeamMake extends Activity{

	EditText et1;
	Button btn1, btn2,btn3;
	
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;
	
	String[] invitefriendlistid;
	String[] invitefriendlist;
	String[] invitefriendpoint;
	String teamname;
	String people = MainActivity.Loginid+ "/";
	int[] point;
	int peoplecount;
	int avrpoint;
	
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpteammake);
		li = (LinearLayout)findViewById(R.id.lihpteammake);
		btn1 = (Button)findViewById(R.id.teammakebtn1);
		btn2 = (Button)findViewById(R.id.teammakebtn2);
		btn3 = (Button)findViewById(R.id.teammakebtn3);
		hpdb = new HPDatabase(HPTeamMake.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.maketeam_blue);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_blue);
				btn2.setBackgroundResource(R.drawable.btnpartlist_blue);
				//btn3.setBackgroundResource(R.drawable.btnfriendadd_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.maketeam_pupple);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_pupple);
				btn2.setBackgroundResource(R.drawable.btnpartlist_pupple);
				//btn3.setBackgroundResource(R.drawable.btnfriendadd_blue);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.maketeam_green);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_green);
				btn2.setBackgroundResource(R.drawable.btnpartlist_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.maketeam_red);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_red);
				btn2.setBackgroundResource(R.drawable.btnpartlist_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.maketeam_gold);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_gold);
				btn2.setBackgroundResource(R.drawable.btnpartlist_gold);
				break;
			}
		}
		
		et1 = (EditText)findViewById(R.id.teammakeet1);
		
		
		btn2.setEnabled(false);
		
		et1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(30) });
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPTeamMake.this, HPBoardFriend.class);
				startActivityForResult(intent, 0);
			}
		});
		
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!et1.getText().toString().isEmpty())
				{
					if(!btn2.isEnabled())
					{
						mtoast = Toast.makeText(HPTeamMake.this,
								"혼자서는 팀을 만들수 없습니다!", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
						return;
					}
					
					teamname = et1.getText().toString();
					point[0] = Integer.parseInt(HPHomeHome.mypoint);
					avrpoint = point[0];
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

								HPBoardObject boardout = new HPBoardObject(
										MainActivity.Loginid, teamname, null,
										null, Integer.toString(peoplecount), people,MainActivity.Loginid +"/","",
										Integer.toString(avrpoint),null,null,null,null,null);
								
								HPObject hp = new HPObject(null, null, null,
										null, null, null, null, null, null,
										null, boardout, "maketeam");
								
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

								HPObject inhp = (HPObject) ois
										.readObject();

								if (inhp.message.equals("falsemakeboard")) {
									handler2.sendEmptyMessage(2);
								} else {
									handler2.sendEmptyMessage(3);
								}
								handler.sendEmptyMessageDelayed(1, 1000);
								
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
									handler.sendEmptyMessageDelayed(2, 1000);
								}
							}

						}
					});
					Hpthread.start();
				}
				else
				{
					mtoast = Toast.makeText(HPTeamMake.this,
							"팀 제목을 입력해주세요!", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 0)
		{
			if(data != null)
			{
				invitefriendlistid = data.getStringExtra("invitefriendid").split("/");
				invitefriendlist = data.getStringExtra("invitefriend").split("/");
				invitefriendpoint = data.getStringExtra("invitefriendpoint").split("/");
				for(int i=0;i<invitefriendlist.length;i++)
				{
					point[i+1] = Integer.parseInt(invitefriendpoint[i]);
					people += invitefriendlistid[i] + "/";
				}
				peoplecount = people.split("/").length;
				
				if(!invitefriendlist[0].equals(""))
				{
					btn2.setEnabled(true);
				}
			}
			else
			{
				btn2.setEnabled(false);
			}
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
				mtoast = Toast.makeText(HPTeamMake.this,
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
}
