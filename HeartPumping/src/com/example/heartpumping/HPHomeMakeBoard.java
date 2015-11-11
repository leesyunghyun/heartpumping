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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HPHomeMakeBoard extends Activity {

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;

	String[] iworld = { "지역(전체)", "서울", "대전", "대구", "부산", "울산", "광주", "전남",
			"전북", "경남", "경북", "충남", "충북", "제주", "강원", "경기" };
	private Button btn1, btn2, btn3;
	private EditText et1, et2;
	private Spinner spin;
	private ArrayAdapter<String> spinadapter;
	Intent modifyintent;
	String subject;
	String content;
	String meetzone;
	String[] invitefriendlistid;
	String[] invitefriendlist;
	String[] invitefriendpoint;
	int peoplecount = 1;
	String people = MainActivity.Loginid + "/";
	int point[] = { 0, 0, 0 };
	int avrpoint = 0;
	int viewable = 0;
	int flag;
	private Intent result;
	
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 자동 생성된 메소드 스텁
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpmakeboard);
		li = (LinearLayout)findViewById(R.id.limakeboard);
		btn1 = (Button) findViewById(R.id.makeboardbtn1);
		btn2 = (Button) findViewById(R.id.makeboardbtn2);
		btn3 = (Button) findViewById(R.id.makeboardbtn3);
		hpdb = new HPDatabase(HPHomeMakeBoard.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.boardwrite_blue);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_blue);
				btn2.setBackgroundResource(R.drawable.btnpartlist_blue);
				btn3.setBackgroundResource(R.drawable.btndr_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.boardwrite_pupple);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_pupple);
				btn2.setBackgroundResource(R.drawable.btnpartlist_pupple);
				btn3.setBackgroundResource(R.drawable.btndr_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.boardwrite_green);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_green);
				btn2.setBackgroundResource(R.drawable.btnpartlist_green);
				btn3.setBackgroundResource(R.drawable.btndr_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.boardwrite_red);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_red);
				btn2.setBackgroundResource(R.drawable.btnpartlist_red);
				btn3.setBackgroundResource(R.drawable.btndr_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.boardwrite_gold);
				btn1.setBackgroundResource(R.drawable.btnfriendadd_gold);
				btn2.setBackgroundResource(R.drawable.btnpartlist_gold);
				btn3.setBackgroundResource(R.drawable.btndr_gold);
				break;
			}
		}
		
		
		modifyintent = getIntent();
		flag = modifyintent.getIntExtra("flag", 0);
		
		btn2.setEnabled(false);
		
		et1 = (EditText) findViewById(R.id.makeboardet1);
		et2 = (EditText) findViewById(R.id.makeboardet2);
		spin = (Spinner) findViewById(R.id.makeboardspin1);
		spinadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, iworld);
		spin.setAdapter(spinadapter);
		
		et1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(30) });

		et2.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(250) });
		btn3.setTag("등록하기");
		if(flag == 1)
		{
			handler2.sendEmptyMessage(4);
			et1.setText(modifyintent.getStringExtra("subject"));
			et2.setText(modifyintent.getStringExtra("content"));
			for(int i=0;i<iworld.length;i++)
			{
				if(modifyintent.getStringExtra("meetzone").equals(iworld[i]))
				{
					spin.setSelection(i);
				}
			}
			btn1.setEnabled(false);
			btn2.setEnabled(false);
			//btn3.setText("수정하기");
			btn3.setTag("수정하기");
			if(c.moveToFirst())
			{
				switch(c.getInt(6))
				{
				case 0:
					btn3.setBackgroundResource(R.drawable.btnmodifying_blue);
					break;
				case 1:
					btn3.setBackgroundResource(R.drawable.btnmodifying_pupple);
					break;
				case 2:
					btn3.setBackgroundResource(R.drawable.btnmodifying_green);
					break;
				case 3:
					btn3.setBackgroundResource(R.drawable.btnmodifying_red);
					break;
				case 4:
					btn3.setBackgroundResource(R.drawable.btnmodifying_gold);
					break;
				}
			}
			
		}
		
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HPHomeMakeBoard.this, HPBoardFriend.class);
				startActivityForResult(intent, 0);
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout abc = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);
				abc.setOrientation(LinearLayout.VERTICAL);
				TextView tv1 = (TextView)abc.findViewById(R.id.textView1);
				TextView tv2 = (TextView)abc.findViewById(R.id.textView2);
				TextView tv3 = (TextView)abc.findViewById(R.id.textView3);
				TextView tv4 = (TextView)abc.findViewById(R.id.textView4);
				TextView tv5 = (TextView)abc.findViewById(R.id.textView5);
				tv2.setVisibility(View.GONE);
				tv3.setVisibility(View.GONE);
				tv4.setVisibility(View.GONE);
				tv5.setVisibility(View.GONE);
				
				AlertDialog dlg = new AlertDialog.Builder(HPHomeMakeBoard.this).create();
				dlg.setTitle("함께 할 친구들");
				dlg.setView(abc);
				dlg.setCanceledOnTouchOutside(true);
				String str ="";
				for(int i=0;i<invitefriendlist.length;i++)
				{
					if(i == invitefriendlist.length-1)
					{	
						str += "친구 : " + invitefriendlist[i];
					}
					else
					{
						str += "친구 : " + invitefriendlist[i] + "\n";
					}
					
				}
				tv1.setText(str);
				tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
				dlg.setCancelable(true);
				dlg.show();
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et1.getText().toString().equals("")
						&& !et2.getText().toString().equals("") && et1.getText().toString().length() > 3 && et2.getText().toString().length()>10) {
					handler.sendEmptyMessage(0);
					subject = et1.getText().toString();
					content = et2.getText().toString();
					meetzone = spin.getSelectedItem().toString();
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
										MainActivity.Loginid, subject, content,
										meetzone, Integer.toString(peoplecount), people,MainActivity.Loginid +"/","",
										Integer.toString(avrpoint), "0", "","0", "0",null);
								
								HPObject hp = new HPObject(null, null, null,
										null, null, null, null, null, null,
										null, boardout, "makeboard");
								if(btn3.getTag().equals("수정하기"))
								{
									boardout = new HPBoardObject(
											MainActivity.Loginid, subject, content,
											meetzone,null,null,null,"",
											null, "0", "","0", "0",null);
									
									hp = new HPObject(null, null, null,
											null, null, null, null, null, null,
											null, boardout, "modifyboard");
								}
								
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
								
								result = new Intent();
								setResult(1,result);
								
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

				} else {
					handler2.sendEmptyMessage(1);
				}

			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn3.setEnabled(false);
				break;
			case 1:
				btn3.setEnabled(true);
				break;
			case 2:
				btn3.setEnabled(true);
				btn3.performClick();
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
				mtoast = Toast.makeText(HPHomeMakeBoard.this,
						"네트워크 상태가 원활하지 않습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 1:
				mtoast = Toast.makeText(HPHomeMakeBoard.this,
						"'제목'과 '내용'이 너무 짧습니다.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 2:
				mtoast = Toast.makeText(HPHomeMakeBoard.this,
						"게시글 등록에 실패하였습니다. 다시 시도해주세요.", 0);
				if(btn3.getText().toString().equals("수정하기"))
				{
					mtoast = Toast.makeText(HPHomeMakeBoard.this,
							"게시글 수정에 실패하였습니다. 다시 시도해주세요.", 0);
				}
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				mtoast = Toast.makeText(HPHomeMakeBoard.this,
						"게시글이 등록되었습니다.", 0);
				if(btn3.getText().toString().equals("수정하기"))
				{
					mtoast = Toast.makeText(HPHomeMakeBoard.this,
							"게시글이 수정되었습니다.", 0);
				}
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				
				finish();
				HPHomeMakeBoard.this.onDestroy();
				break;
			case 4:
				mtoast = Toast.makeText(HPHomeMakeBoard.this,
						"게시판 수정은 편의상 '제목','내용','만남지역' 만 수정하실 수 있습니다.!", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
				
			}
		}
	};

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
	
	
}
