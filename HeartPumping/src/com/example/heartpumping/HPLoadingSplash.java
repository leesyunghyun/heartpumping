package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HPLoadingSplash extends Activity {
	private ProgressDialog progressDialog = null;
	private HPDatabase hpdb;
	private SQLiteDatabase sql;
	private Cursor c = null;
	public int splashflag = 0;
	private LinearLayout li;

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	Toast mtoast;
	HPObject inhp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hploadingsplash);
		li = (LinearLayout) findViewById(R.id.liloadingsplash);
		hpdb = new HPDatabase(this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		hpdb.onCreate(sql);
		if (!c.moveToFirst()) {
			sql.execSQL("insert into personal(id,saveid, autologin, searchnic, loginid, loginpw, tema) values('set',0,0,1,'empty','empty',0);");
		}
		c = sql.rawQuery("select * from personal", null);

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(Html
				.fromHtml("<FONT Color='Black'>서버에 접속중입니다.</FONT>"));
		progressDialog.setIcon(0);
		progressDialog.setCancelable(false);

		if (c.moveToFirst()) {
			switch (c.getInt(6)) {
			case 0:
				li.setBackgroundResource(R.drawable.splash_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.splash_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.splash_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.splash_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.splash_gold);
				break;
			}
		}
		
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				finish();
			}
		};

		handler.sendEmptyMessageDelayed(0, 2000);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressDialog.show();
			}
		}, 800);

		progressDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
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
				
			}
		});

		progressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (!MainActivity.connectthread.channel.isConnected()) {
					mtoast = Toast.makeText(HPLoadingSplash.this,
							"서버연결에 실패하였습니다. ", 0);
					mtoast.show();
				}
				else
				{
					mtoast = Toast.makeText(HPLoadingSplash.this,
							"서버연결에 성공하였습니다.", 0);
					mtoast.show();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		progressDialog.dismiss();
	}

}
