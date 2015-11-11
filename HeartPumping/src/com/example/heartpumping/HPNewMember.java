package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HPNewMember extends Activity {

	EditText etid, etpw1, etpw2, etnum, etnic, etuniv1, etuniv2, etphone;
	RadioGroup rg1;
	RadioButton rb1, rb2;
	Button btn1, btn2, btn3, btn4, btn5;

	Thread Hpthread;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	ByteArrayInputStream bais;
	ObjectInputStream ois;
	String id, pw, schoolnum, nic, univ1, univ2, phone;
	String sex;
	int idflag = 0;
	int phoneflag = 0;
	String idcheck = "falseid";
	String phonecheck = "falsephone";
	Toast mtoast;
	boolean back_cheak = false;
	BackPressCloseHandler back;

	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpnewmember);
		li = (LinearLayout)findViewById(R.id.lihpnewmember);
		btn1 = (Button) findViewById(R.id.btnuniv);
		btn2 = (Button) findViewById(R.id.btnphone);
		btn3 = (Button) findViewById(R.id.btnnewmake);
		btn4 = (Button) findViewById(R.id.btnnewcancel);
		btn5 = (Button) findViewById(R.id.idcheck);
		hpdb = new HPDatabase(HPNewMember.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.addmember_blue);
				btn1.setBackgroundResource(R.drawable.btninput_blue);
				btn2.setBackgroundResource(R.drawable.btnphonesend_blue);
				btn3.setBackgroundResource(R.drawable.btndr_blue);
				btn4.setBackgroundResource(R.drawable.btncancel_blue);
				btn5.setBackgroundResource(R.drawable.btnoverlap_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.addmember_pupple);
				btn1.setBackgroundResource(R.drawable.btninput_pupple);
				btn2.setBackgroundResource(R.drawable.btnphonesend_pupple);
				btn3.setBackgroundResource(R.drawable.btndr_pupple);
				btn4.setBackgroundResource(R.drawable.btncancel_pupple);
				btn5.setBackgroundResource(R.drawable.btnoverlap_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.addmember_green);
				btn1.setBackgroundResource(R.drawable.btninput_green);
				btn2.setBackgroundResource(R.drawable.btnphonesend_green);
				btn3.setBackgroundResource(R.drawable.btndr_green);
				btn4.setBackgroundResource(R.drawable.btncancel_green);
				btn5.setBackgroundResource(R.drawable.btnoverlap_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.addmember_red);
				btn1.setBackgroundResource(R.drawable.btninput_red);
				btn2.setBackgroundResource(R.drawable.btnphonesend_red);
				btn3.setBackgroundResource(R.drawable.btndr_red);
				btn4.setBackgroundResource(R.drawable.btncancel_red);
				btn5.setBackgroundResource(R.drawable.btnoverlap_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.addmember_gold);
				btn1.setBackgroundResource(R.drawable.btninput_gold);
				btn2.setBackgroundResource(R.drawable.btnphonesend_gold);
				btn3.setBackgroundResource(R.drawable.btndr_gold);
				btn4.setBackgroundResource(R.drawable.btncancel_gold);
				btn5.setBackgroundResource(R.drawable.btnoverlap_gold);
				break;
			}
		}
		
		back = new BackPressCloseHandler(HPNewMember.this);
		etid = (EditText) findViewById(R.id.etid);
		etpw1 = (EditText) findViewById(R.id.etpw1);
		etpw2 = (EditText) findViewById(R.id.etpw2);
		etnum = (EditText) findViewById(R.id.etschoolnumber);
		etnic = (EditText) findViewById(R.id.etnic);
		etuniv1 = (EditText) findViewById(R.id.etuniv1);
		etuniv2 = (EditText) findViewById(R.id.etuniv2);
		etphone = (EditText) findViewById(R.id.etphone);

		etid.setFilters(new InputFilter[] { IDFilter,
				new InputFilter.LengthFilter(8) });
		etpw1.setFilters(new InputFilter[] { pwFilter,
				new InputFilter.LengthFilter(8) });
		etpw2.setFilters(new InputFilter[] { pwFilter,
				new InputFilter.LengthFilter(8) });
		etnic.setFilters(new InputFilter[] { editFilter,
				new InputFilter.LengthFilter(8) });
		etuniv1.setFilters(new InputFilter[] { editFilter,
				new InputFilter.LengthFilter(8) });
		etuniv2.setFilters(new InputFilter[] { editFilter,
				new InputFilter.LengthFilter(8) });

		etuniv1.setFocusable(false);
		etuniv2.setFocusable(false);
		etuniv1.setEnabled(false);
		etuniv2.setEnabled(false);
		rg1 = (RadioGroup) findViewById(R.id.rgSex);
		rb1 = (RadioButton) findViewById(R.id.rdmale);
		rb2 = (RadioButton) findViewById(R.id.rdfemale);
		

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
				mtoast = Toast.makeText(HPNewMember.this, "´ëÇÐ±³ ÀÔ·Â ±¸ÇöÁß", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
				/*
				 * mtoast = Toast.makeText(HPNewMember.this, "¿¬¶ôÃ³ ÀÎÁõ¹øÈ£ ±¸ÇöÁß", 0);
				 * mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				 * mtoast.show();
				 */

				if (phoneflag == 1) {
					etphone.setText("");
					etphone.requestFocus();
					etphone.setEnabled(true);
					//btn2.setText("ÀÎÁõ¹øÈ£¹ß¼Û");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn2.setBackgroundResource(R.drawable.btnphonesend_blue);
							break;
						case 1:
							btn2.setBackgroundResource(R.drawable.btnphonesend_pupple);
							break;
						case 2:
							btn2.setBackgroundResource(R.drawable.btnphonesend_green);
							break;
						case 3:
							btn2.setBackgroundResource(R.drawable.btnphonesend_red);
							break;
						case 4:
							btn2.setBackgroundResource(R.drawable.btnphonesend_gold);
							break;
						}
					}
					phoneflag = 0;
					phonecheck = "falsephone";
					return;
				}
				if (etphone.getText().toString().isEmpty()) {
					return;
				}

				handler.sendEmptyMessage(3);

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
						// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
						try {

							baos = new ByteArrayOutputStream();
							out = new ObjectOutputStream(baos);

							HPObject hp = new HPObject(null, null, null, null,
									null, null, etphone.getText().toString(),
									null,null, null,null, "phonecheck");

							Object obc = hp;

							out.writeObject(obc);

							if (MainActivity.connectthread.channel
									.isConnected()) {
								MainActivity.connectthread.channel
										.write(ByteBuffer.wrap(baos
												.toByteArray()));
							} else {
								handler.sendEmptyMessage(2);
								handler2.sendEmptyMessageDelayed(5, 1000);
							}

							ByteBuffer data = ByteBuffer.allocate(1024);
							MainActivity.connectthread.selector.select();
							MainActivity.connectthread.channel.read(data);

							bais = new ByteArrayInputStream(data.array());

							ois = new ObjectInputStream(bais);

							HPObject inhp = (HPObject) ois.readObject();

							phonecheck = inhp.message;

							Hpthread.interrupt();
							handler2.sendEmptyMessageDelayed(5, 1000);
						} catch (Exception e) {
							// TODO ÀÚµ¿ »ý¼ºµÈ catch ºí·Ï
							if (MainActivity.connectthread.channel
									.isConnected()) {
								Hpthread.interrupt();
								MainActivity.connectthread.interrupt();
								MainActivity.connectthread = new HPConnectThread(
										MainActivity.context);
								MainActivity.connectthread.start();
								handler2.sendEmptyMessage(6);
							}
						}

					}
				});
				Hpthread.start();
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
				if (!etid.getText().toString().isEmpty()
						&& !etpw1.getText().toString().isEmpty()
						&& !etpw2.getText().toString().isEmpty()
						&& !etnic.getText().toString().isEmpty()
						&& !etnum.getText().toString().isEmpty()
						&& !etphone.getText().toString().isEmpty()
						&& !etuniv1.getText().toString().isEmpty()
						&& !etuniv2.getText().toString().isEmpty()) {
					// ºóÄ­¾øÀÌ ´Ù ÀûÀ½

					if(etpw1.getText().toString().length()<4)
					{
						mtoast = Toast.makeText(HPNewMember.this,
								"ºñ¹Ð¹øÈ£°¡ ³Ê¹« Âª½À´Ï´Ù. 4ÀÚ¸®ÀÌ»ó ÀÔ·ÂÇØÁÖ¼¼¿ä", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
						return ;
					}
					
					if(etnic.getText().toString().length()<2)
					{
						mtoast = Toast.makeText(HPNewMember.this,
								"´Ð³×ÀÓÀÌ ³Ê¹« Âª½À´Ï´Ù.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
						return ;
					}
					
					if (etpw1.getText().toString()
							.equals(etpw2.getText().toString())) {
						// ºñ¹øÀÌ ÀÏÄ¡ÇÔ
						if (rb1.isChecked()) {
							sex = "0";
						} else {
							sex = "1";
						}

						if (idflag == 0) {
							mtoast = Toast.makeText(HPNewMember.this,
									"¾ÆÀÌµð Áßº¹È®ÀÎ ÇÊ¼ö!", 0);
							mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							mtoast.show();
							return;
						}

						if (phoneflag == 0) {
							mtoast = Toast.makeText(HPNewMember.this,
									"¿¬¶ôÃ³ ÀÎÁõ ÇÊ¼ö!", 0);
							mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
							mtoast.show();
							return;
						}

						handler.sendEmptyMessage(1);
						id = etid.getText().toString();
						pw = etpw1.getText().toString();
						nic = etnic.getText().toString();
						schoolnum = etnum.getText().toString();
						phone = etphone.getText().toString();
						univ1 = etuniv1.getText().toString();
						univ2 = etuniv2.getText().toString();

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
								// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
								try {
									baos = new ByteArrayOutputStream();
									out = new ObjectOutputStream(baos);

									HPObject hp = new HPObject(id, pw, nic,
											sex, univ1, univ2, phone,
											schoolnum,null, null,null, "newmember");

									Object obc = hp;

									out.writeObject(obc);

									if (MainActivity.connectthread.channel
											.isConnected()) {
										MainActivity.connectthread.channel
												.write(ByteBuffer.wrap(baos
														.toByteArray()));
									} else {
										handler.sendEmptyMessage(2);
										handler2.sendEmptyMessageDelayed(4,
												1000);
										Hpthread.interrupt();
										return;
									}
									handler2.sendEmptyMessageDelayed(1, 0);
									Hpthread.interrupt();
									finish();
									HPNewMember.this.onDestroy();
								} catch (IOException e) {
									// TODO ÀÚµ¿ »ý¼ºµÈ catch ºí·Ï
									if (MainActivity.connectthread.channel
											.isConnected()) {
										Hpthread.interrupt();
										MainActivity.connectthread.interrupt();
										MainActivity.connectthread = new HPConnectThread(
												MainActivity.context);
										MainActivity.connectthread.start();
										handler2.sendEmptyMessage(3);
									}
								}

							}
						});
						Hpthread.start();
					} else {
						// ºñ¹ø ºÒÀÏÄ¡
						mtoast = Toast.makeText(HPNewMember.this,
								"ºñ¹Ð¹øÈ£°¡ ¼­·Î ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
					}
				} else {
					// ºóÄ­ÀÌ Á¸ÀçÇÔ
					mtoast = Toast.makeText(HPNewMember.this,
							"ºóÄ­ ¾øÀÌ ¸ðµÎ ÀÔ·ÂÇØÁÖ¼¼¿ä.", 0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
			}

		});

		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
				finish();
				HPNewMember.this.onDestroy();
			}
		});

		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ

				if (idflag == 1) {
					etid.setText("");
					etid.requestFocus();
					etid.setEnabled(true);
					//btn5.setText("Áßº¹È®ÀÎ");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn5.setBackgroundResource(R.drawable.btnoverlap_blue);
							break;
						case 1:
							btn5.setBackgroundResource(R.drawable.btnoverlap_pupple);
							break;
						case 2:
							btn5.setBackgroundResource(R.drawable.btnoverlap_green);
							break;
						case 3:
							btn5.setBackgroundResource(R.drawable.btnoverlap_red);
							break;
						case 4:
							btn5.setBackgroundResource(R.drawable.btnoverlap_gold);
							break;
						}
					}
					idflag = 0;
					idcheck = "falseid";
					return;
				}
				if (etid.getText().toString().isEmpty()) {
					return;
				}

				if(etid.getText().toString().length() < 2)
				{
					mtoast = Toast.makeText(HPNewMember.this, "¾ÆÀÌµð°¡ ³Ê¹« Âª½À´Ï´Ù.",
							0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
					return ;
				}
				handler.sendEmptyMessage(0);

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
						// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
						try {

							baos = new ByteArrayOutputStream();
							out = new ObjectOutputStream(baos);

							HPObject hp = new HPObject(etid.getText()
									.toString(), null, null, null, null, null,
									null, null, null, null,null,"idcheck");

							Object obc = hp;

							out.writeObject(obc);

							if (MainActivity.connectthread.channel
									.isConnected()) {
								MainActivity.connectthread.channel
										.write(ByteBuffer.wrap(baos
												.toByteArray()));
							} else {
								handler.sendEmptyMessage(2);
								handler2.sendEmptyMessageDelayed(0, 1000);
							}

							ByteBuffer data = ByteBuffer.allocate(1024);
							MainActivity.connectthread.selector.select();
							MainActivity.connectthread.channel.read(data);

							bais = new ByteArrayInputStream(data.array());

							ois = new ObjectInputStream(bais);

							HPObject inhp = (HPObject) ois.readObject();

							idcheck = inhp.message;

							Hpthread.interrupt();
							handler2.sendEmptyMessageDelayed(0, 1000);
						} catch (Exception e) {
							// TODO ÀÚµ¿ »ý¼ºµÈ catch ºí·Ï
							if (MainActivity.connectthread.channel
									.isConnected()) {
								Hpthread.interrupt();
								MainActivity.connectthread.interrupt();
								MainActivity.connectthread = new HPConnectThread(
										MainActivity.context);
								MainActivity.connectthread.start();
								handler2.sendEmptyMessage(2);
							}
						}

					}
				});
				Hpthread.start();
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn5.setEnabled(false);
				break;
			case 1:
				btn3.setEnabled(false);
				break;
			case 2:
				mtoast = Toast.makeText(HPNewMember.this,
						"³×Æ®¿öÅ© »óÅÂ°¡ ¿øÈ°ÇÏÁö ¾Ê½À´Ï´Ù.", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 3:
				btn2.setEnabled(false);
				break;
			}
		}
	};

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btn5.setEnabled(true);
				if (idcheck.equals("trueid")) {
					etpw1.requestFocus();
					etid.setEnabled(false);
					//btn5.setText("Áßº¹È®ÀÎÇØÁ¦");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn5.setBackgroundResource(R.drawable.btnoverlapclear_blue);
							break;
						case 1:
							btn5.setBackgroundResource(R.drawable.btnoverlapclear_pupple);
							break;
						case 2:
							btn5.setBackgroundResource(R.drawable.btnoverlapclear_green);
							break;
						case 3:
							btn5.setBackgroundResource(R.drawable.btnoverlapclear_red);
							break;
						case 4:
							btn5.setBackgroundResource(R.drawable.btnoverlapclear_gold);
							break;
						}
					}
					idflag = 1;
				} else {
					if (MainActivity.connectthread.channel.isConnected()) {
						mtoast = Toast.makeText(HPNewMember.this,
								"Áßº¹µÇ´Â ¾ÆÀÌµð ÀÔ´Ï´Ù.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
					}
				}
				break;
			case 1:
				btn3.setEnabled(true);
				mtoast = Toast.makeText(HPNewMember.this, "È¸¿ø°¡ÀÔÀ» ÃàÇÏµå¸³´Ï´Ù!", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
				break;
			case 2:
				btn5.performClick();
				break;
			case 3:
				btn3.performClick();
				break;
			case 4:
				btn3.setEnabled(true);
				break;
			case 5:
				btn2.setEnabled(true);
				if (phonecheck.equals("truephone")) {
					etphone.setEnabled(false);
					//btn2.setText("¿¬¶ôÃ³ º¯°æ");
					if(c.moveToFirst())
					{
						switch(c.getInt(6))
						{
						case 0:
							btn2.setBackgroundResource(R.drawable.btnchangetel_blue);
							break;
						case 1:
							btn2.setBackgroundResource(R.drawable.btnchangetel_pupple);
							break;
						case 2:
							btn2.setBackgroundResource(R.drawable.btnchangetel_green);
							break;
						case 3:
							btn2.setBackgroundResource(R.drawable.btnchangetel_red);
							break;
						case 4:
							btn2.setBackgroundResource(R.drawable.btnchangetel_gold);
							break;
						}
					}
					phoneflag = 1;
				} else {
					if (MainActivity.connectthread.channel.isConnected()) {
						mtoast = Toast.makeText(HPNewMember.this,
								"ÀÎÁõÀÌ ºÒ°¡´ÉÇÑ ¿¬¶ôÃ³ÀÔ´Ï´Ù.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
					}
				}
				break;
			case 6:
				btn2.performClick();
				break;
			}

		}
	};

	@Override
	public void onBackPressed() {
		// TODO ÀÚµ¿ »ý¼ºµÈ ¸Þ¼Òµå ½ºÅÓ
		if (back.onBackPressed()) {
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
			toast = Toast.makeText(activity, "\'µÚ·Î\'¹öÆ°À» ÇÑ¹ø ´õ ´©¸£½Ã¸é Á¾·áµË´Ï´Ù.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	public static InputFilter editFilter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			// Pattern pattern = Pattern.compile("^[A-Z]+$"); // ¿µ¹®´ë¹®ÀÚ
			// Pattern pattern = Pattern.compile("^[a-zA-Z]+$"); // ¿µ¹®
			// Pattern pattern = Pattern.compile("^[¤¡-¤¾°¡-ÆR]+$"); // ÇÑ±Û
			
			
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9¤¡-¤¾ °¡-ÆR¤¿-¤Ó~!@#$:%^&*().?,]+$"); // ¿µ¹®,¼ýÀÚ,ÇÑ±Û
			if (!pattern.matcher(source).matches()) {
				return "";
			}
			return null;
		}

	};
	
	public static InputFilter editFilter2 = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			// Pattern pattern = Pattern.compile("^[A-Z]+$"); // ¿µ¹®´ë¹®ÀÚ
			// Pattern pattern = Pattern.compile("^[a-zA-Z]+$"); // ¿µ¹®
			// Pattern pattern = Pattern.compile("^[¤¡-¤¾°¡-ÆR]+$"); // ÇÑ±Û
			Pattern pattern = Pattern.compile("^[/]+$"); // ¿µ¹®,¼ýÀÚ,ÇÑ±Û
			if (pattern.matcher(source).matches()) {
				return "";
			}

			return null;
		}

	};
	
	public static InputFilter IDFilter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			// Pattern pattern = Pattern.compile("^[A-Z]+$"); // ¿µ¹®´ë¹®ÀÚ
			// Pattern pattern = Pattern.compile("^[a-zA-Z]+$"); // ¿µ¹®
			// Pattern pattern = Pattern.compile("^[¤¡-¤¾°¡-ÆR]+$"); // ÇÑ±Û
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$"); // ¿µ¹®,¼ýÀÚ,ÇÑ±Û
			if (!pattern.matcher(source).matches()) {
				return "";
			}

			return null;
		}

	};

	public static InputFilter pwFilter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			// Pattern pattern = Pattern.compile("^[A-Z]+$"); // ¿µ¹®´ë¹®ÀÚ
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9~!@#$%^&*().?,]+$"); // ¿µ¹®
			// Pattern pattern = Pattern.compile("^[¤¡-¤¾°¡-ÆR]+$"); // ÇÑ±Û
			// Pattern pattern = Pattern.compile("^[a-zA-Z0-9¤¡-¤¾°¡-ÆR]+$"); //
			// ¿µ¹®,¼ýÀÚ,ÇÑ±Û
			if (!pattern.matcher(source).matches()) {
				return "";
			}
			return null;
		}
	};
}
