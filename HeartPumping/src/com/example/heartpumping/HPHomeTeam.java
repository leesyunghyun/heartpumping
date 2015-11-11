package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HPHomeTeam extends Activity{

	ListView list;
	Button btn1, btn2;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpteamsetting);
		li = (LinearLayout)findViewById(R.id.lihpteamsetting);
		btn1 = (Button)findViewById(R.id.teambtn1);
		btn2 = (Button)findViewById(R.id.teambtn2);
		
		hpdb = new HPDatabase(HPHomeTeam.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.maketeam_blue);
				btn1.setBackgroundResource(R.drawable.btnrequestlist_blue);
				btn2.setBackgroundResource(R.drawable.btnteammake_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.maketeam_pupple);
				btn1.setBackgroundResource(R.drawable.btnrequestlist_pupple);
				btn2.setBackgroundResource(R.drawable.btnteammake_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.maketeam_green);
				btn1.setBackgroundResource(R.drawable.btnrequestlist_green);
				btn2.setBackgroundResource(R.drawable.btnteammake_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.maketeam_red);
				btn1.setBackgroundResource(R.drawable.btnrequestlist_red);
				btn2.setBackgroundResource(R.drawable.btnteammake_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.maketeam_gold);
				btn1.setBackgroundResource(R.drawable.btnrequestlist_gold);
				btn2.setBackgroundResource(R.drawable.btnteammake_gold);
				break;
			}
		}
		
		list = (ListView)findViewById(R.id.teamlist);
		
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Intent intent = new Intent(HPHomeTeam.this, HPTeamMake.class);
				startActivity(intent);
				*/
			}
		});
	}

}
