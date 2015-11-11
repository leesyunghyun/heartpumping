package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HPBoardFriend extends Activity{
	
	Button btn1;
	Button btnv[];
	CheckBox chv[];
	ListView list1;
	ArrayAdapter<String> adapter;
	ArrayList<String> arraylist;
	int chkcount = 0;
	String chkstr[];
	Intent result, intent;
	
	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;
	int flag=0;
	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpboardfriend);
		li = (LinearLayout)findViewById(R.id.lihpboardfriend);
		hpdb = new HPDatabase(HPBoardFriend.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		btn1 = (Button)findViewById(R.id.btnboardfriend1);
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.chodae_blue);
				btn1.setBackgroundResource(R.drawable.btninvite_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.chodae_pupple);
				btn1.setBackgroundResource(R.drawable.btninvite_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.chodae_green);
				btn1.setBackgroundResource(R.drawable.btninvite_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.chodae_red);
				btn1.setBackgroundResource(R.drawable.btninvite_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.chodae_gold);
				btn1.setBackgroundResource(R.drawable.btninvite_gold);
				break;
			}
		}
		
		intent = getIntent();
		flag = intent.getIntExtra("flag", 0);
		list1 = (ListView)findViewById(R.id.listboardfriend1);
		arraylist = new ArrayList<String>();
		
		for(int i=0;i<HPHomeHome.friendlist.length;i++)
		{
			if(!HPHomeHome.friendlist[i].equals("") && !HPHomeHome.friendlist[i].equals("falseresult"))
			{
				arraylist.add("닉네임 : " + HPHomeHome.friendlist[i]);
			}
		}
		
		
		adapter = new MyAdapter(HPBoardFriend.this, android.R.layout.simple_list_item_1, arraylist);
		list1.setAdapter(adapter);
		
		list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(chv[position].isEnabled())
				{
					if(chv[position].isChecked())
					{
						chv[position].setChecked(false);
						chkcount--;
						if(chkcount == 2)
						{
							for(int i=0;i<list1.getCount();i++)
							{
								if(chv[i].isChecked())
								{
									chv[i].setEnabled(true);
								}
								else
								{
									chv[i].setEnabled(false);
								}
							}
							return ;
						}
						else
						{
							for(int i=0;i<list1.getCount();i++)
							{
								chv[i].setEnabled(true);
							}
						}
					}
					else
					{
						chv[position].setChecked(true);
						chkcount++;
						if(chkcount == 2)
						{
							for(int i=0;i<list1.getCount();i++)
							{
								if(chv[i].isChecked())
								{
									chv[i].setEnabled(true);
								}
								else
								{
									chv[i].setEnabled(false);
								}
							}
							return ;
						}
						else
						{
							for(int i=0;i<list1.getCount();i++)
							{
								chv[i].setEnabled(true);
							}
						}
					}
				}
				
			
				adapter.notifyDataSetChanged();
			}
		});
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				result = new Intent();
				String str = "";
				String str2 = "";
				String str3 = "";
				for(int i=0;i<list1.getCount();i++)
				{
					if(chv[i].isChecked())
					{
						str += HPHomeHome.friendlistid[i] + "/";
						str2 += HPHomeHome.friendlist[i] + "/";
						str3 += HPHomeHome.friendpoint[i] + "/";
					}
				}
				if(!str.isEmpty())
				{
					result.putExtra("invitefriendid",str);
					result.putExtra("invitefriend",str2);
					result.putExtra("invitefriendpoint", str3);
					setResult(0,result);
				}
				finish();
				HPBoardFriend.this.onDestroy();
			}
		});
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
			btnv = new Button[m_arrayList.size()];
			chv = new CheckBox[m_arrayList.size()];
			for (int i = 0; i < m_arrayList.size(); i++) {
				final int index = i;
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				LinearLayout a1 = (LinearLayout) vi.inflate(
						R.layout.hpfindfriendlist, null);

				chv[i] = new CheckBox(m_context);
				TextView tv = new TextView(m_context);
				btnv[i] = new Button(m_context);
				
				chv[i].setFocusable(false);
				chv[i].setClickable(false);
				tv.setFocusable(false);
				tv.setClickable(false);
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
				
				chv[i].setText(m_arrayList.get(i));
				DisplayMetrics outMetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
				int pixel = (int) (1.9 * outMetrics.densityDpi);
				chv[i].setWidth(pixel);
				//tv.setText(m_arrayList.get(i));
				//tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				
				btnv[i].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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

						for (int i = 0; i < (int) (HPHomeHome.friendphone[index].length() / 2.0 + 0.5); i++) {
							starstr += "*";
						}

						if (HPHomeHome.friendsex[index].equals("0")) {
							sexstr = "남자";
						} else {
							sexstr = "여자";
						}

						phonestr = HPHomeHome.friendphone[index].substring(0,
								HPHomeHome.friendphone[index].length() / 2) + starstr;

						nic.setText("닉네임 : " + HPHomeHome.friendlist[index]);
						sex.setText("성별 : " + sexstr);
						univ_1.setText("대학교 : " + HPHomeHome.frienduniv_1[index] + "대학교");
						phone.setText("연락처 : " + phonestr);
						heart.setText("두근두근 : " + HPHomeHome.friendpoint[index] + " 점");

						AlertDialog dia1 = new AlertDialog.Builder(
								HPBoardFriend.this).create();
						dia1.setTitle("정보");
						dia1.setView(abc);
						dia1.setCancelable(true);
						dia1.setCanceledOnTouchOutside(true);
						dia1.show();
					}
				});
				
				a1.addView(chv[i]);
				a1.addView(btnv[i]);

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
