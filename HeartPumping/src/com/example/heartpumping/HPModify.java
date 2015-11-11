package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HPModify extends Activity {

	private Intent intent;
	private Intent result;
	EditText etid, etpw1, etpw2, etnum, etnic, etuniv1, etuniv2, etphone;
	RadioGroup rg1;
	RadioButton rb1, rb2;
	Button btn1, btn2, btn3, btn4, btn5;
	LinearLayout li;

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

	HPDatabase hpdb;
	Cursor c;
	SQLiteDatabase sql;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 자동 생성된 메소드 스텁
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpnewmember);
		back = new BackPressCloseHandler(HPModify.this);
		intent = getIntent();
		li = (LinearLayout)findViewById(R.id.lihpnewmember);
		btn1 = (Button) findViewById(R.id.btnuniv);
		btn2 = (Button) findViewById(R.id.btnphone);
		btn3 = (Button) findViewById(R.id.btnnewmake);
		btn4 = (Button) findViewById(R.id.btnnewcancel);
		btn5 = (Button) findViewById(R.id.idcheck);
		hpdb = new HPDatabase(HPModify.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.modifymember_blue);
				btn1.setBackgroundResource(R.drawable.btninput_blue);
				btn2.setBackgroundResource(R.drawable.btnnomodify_blue);
				btn3.setBackgroundResource(R.drawable.btnmodify_blue);
				btn4.setBackgroundResource(R.drawable.btncancel_blue);
				btn5.setBackgroundResource(R.drawable.btnnomodify_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.modifymember_pupple);
				btn1.setBackgroundResource(R.drawable.btninput_pupple);
				btn2.setBackgroundResource(R.drawable.btnnomodify_pupple);
				btn3.setBackgroundResource(R.drawable.btnmodify_pupple);
				btn4.setBackgroundResource(R.drawable.btncancel_pupple);
				btn5.setBackgroundResource(R.drawable.btnnomodify_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.modifymember_green);
				btn1.setBackgroundResource(R.drawable.btninput_green);
				btn2.setBackgroundResource(R.drawable.btnnomodify_green);
				btn3.setBackgroundResource(R.drawable.btnmodify_green);
				btn4.setBackgroundResource(R.drawable.btncancel_green);
				btn5.setBackgroundResource(R.drawable.btnnomodify_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.modifymember_red);
				btn1.setBackgroundResource(R.drawable.btninput_red);
				btn2.setBackgroundResource(R.drawable.btnnomodify_red);
				btn3.setBackgroundResource(R.drawable.btnmodify_red);
				btn4.setBackgroundResource(R.drawable.btncancel_red);
				btn5.setBackgroundResource(R.drawable.btnnomodify_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.modifymember_gold);
				btn1.setBackgroundResource(R.drawable.btninput_gold);
				btn2.setBackgroundResource(R.drawable.btnnomodify_gold);
				btn3.setBackgroundResource(R.drawable.btnmodify_gold);
				btn4.setBackgroundResource(R.drawable.btncancel_gold);
				btn5.setBackgroundResource(R.drawable.btnnomodify_gold);
				break;
			}
		}
		
		
		etid = (EditText) findViewById(R.id.etid);
		etpw1 = (EditText) findViewById(R.id.etpw1);
		etpw2 = (EditText) findViewById(R.id.etpw2);
		etnum = (EditText) findViewById(R.id.etschoolnumber);
		etnic = (EditText) findViewById(R.id.etnic);
		etuniv1 = (EditText) findViewById(R.id.etuniv1);
		etuniv2 = (EditText) findViewById(R.id.etuniv2);
		etphone = (EditText) findViewById(R.id.etphone);

		etid.setFilters(new InputFilter[] { HPNewMember.IDFilter,
				new InputFilter.LengthFilter(8) });
		etpw1.setFilters(new InputFilter[] { HPNewMember.pwFilter,
				new InputFilter.LengthFilter(8) });
		etpw2.setFilters(new InputFilter[] { HPNewMember.pwFilter,
				new InputFilter.LengthFilter(8) });
		etnic.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(8) });
		etuniv1.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(8) });
		etuniv2.setFilters(new InputFilter[] { HPNewMember.editFilter,
				new InputFilter.LengthFilter(8) });

		etuniv1.setFocusable(false);
		etuniv2.setFocusable(false);
		etuniv1.setEnabled(false);
		etuniv2.setEnabled(false);
		rg1 = (RadioGroup) findViewById(R.id.rgSex);
		rb1 = (RadioButton) findViewById(R.id.rdmale);
		rb2 = (RadioButton) findViewById(R.id.rdfemale);
		

		//btn2.setText("수정불가");
		
		btn2.setEnabled(false);
		//btn5.setText("수정불가");
		btn5.setEnabled(false);
		//btn3.setText("수정하기");
		
		etid.setText(intent.getStringExtra("id"));
		etnic.setText(intent.getStringExtra("nicname"));
		etuniv1.setText(intent.getStringExtra("univ_1"));
		etuniv2.setText(intent.getStringExtra("univ_2"));
		etphone.setText(intent.getStringExtra("phone"));
		etnum.setText(intent.getStringExtra("schoolnumber"));

		etid.setEnabled(false);
		etphone.setEnabled(false);
		rb1.setEnabled(false);
		rb2.setEnabled(false);
		rg1.setEnabled(false);

		if (intent.getStringExtra("sex").equals("0")) {
			rb1.setChecked(true);
		} else {
			rb2.setChecked(true);
		}

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				mtoast = Toast.makeText(HPModify.this, "대학교 입력 구현중", 0);
				mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				mtoast.show();
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				/*
				 * mtoast = Toast.makeText(HPModify.this, "연락처 인증번호 구현중", 0);
				 * mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				 * mtoast.show();
				 */

				if (phoneflag == 1) {
					etphone.setText("");
					etphone.requestFocus();
					etphone.setEnabled(true);
					//btn2.setText("인증번호발송");
					
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
						// TODO 자동 생성된 메소드 스텁
						try {

							baos = new ByteArrayOutputStream();
							out = new ObjectOutputStream(baos);

							HPObject hp = new HPObject(null, null, null, null,
									null, null, etphone.getText().toString(),
									null, null, null,null, "phonecheck");

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
							// TODO 자동 생성된 catch 블록
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
				// TODO 자동 생성된 메소드 스텁
				if (!etid.getText().toString().isEmpty()
						&& !etpw1.getText().toString().isEmpty()
						&& !etpw2.getText().toString().isEmpty()
						&& !etnic.getText().toString().isEmpty()
						&& !etnum.getText().toString().isEmpty()
						&& !etphone.getText().toString().isEmpty()
						&& !etuniv1.getText().toString().isEmpty()
						&& !etuniv2.getText().toString().isEmpty()) {
					// 빈칸없이 다 적음
					
					if(etpw1.getText().toString().length()<4)
					{
						mtoast = Toast.makeText(HPModify.this,
								"비밀번호가 너무 짧습니다. 4자리이상 입력해주세요", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
						return ;
					}
					
					if(etnic.getText().toString().length()<2)
					{
						mtoast = Toast.makeText(HPModify.this,
								"닉네임이 너무 짧습니다.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
						return ;
					}
					
					
					if (etpw1.getText().toString()
							.equals(etpw2.getText().toString())) {
						// 비번이 일치함
						if (rb1.isChecked()) {
							sex = "0";
						} else {
							sex = "1";
						}

						/*
						 * if (idflag == 0) { mtoast =
						 * Toast.makeText(HPModify.this, "아이디 중복확인 필수!", 0);
						 * mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						 * mtoast.show(); return; }
						 * 
						 * if (phoneflag == 0) { mtoast =
						 * Toast.makeText(HPModify.this, "연락처 인증 필수!", 0);
						 * mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						 * mtoast.show(); return; }
						 */
						handler.sendEmptyMessage(1);
						id = etid.getText().toString();
						pw = etpw1.getText().toString();
						nic = etnic.getText().toString();
						phone = etphone.getText().toString();
						schoolnum = etnum.getText().toString();
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
								// TODO 자동 생성된 메소드 스텁
								try {
									baos = new ByteArrayOutputStream();
									out = new ObjectOutputStream(baos);

									HPObject hp = new HPObject(id, pw, nic,
											sex, univ1, univ2, phone,
											schoolnum, null, null,null,
											"membermodify");

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
									result = new Intent();
									result.putExtra("nicname", nic);
									result.putExtra("sex", sex);
									result.putExtra("univ_1",univ1);
									setResult(0,result);
									finish();
									HPModify.this.onDestroy();
								} catch (IOException e) {
									// TODO 자동 생성된 catch 블록
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
						// 비번 불일치
						mtoast = Toast.makeText(HPModify.this,
								"비밀번호가 서로 일치하지 않습니다.", 0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
					}
				} else {
					// 빈칸이 존재함
					mtoast = Toast.makeText(HPModify.this, "빈칸 없이 모두 입력해주세요.",
							0);
					mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					mtoast.show();
				}
			}

		});

		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁
				finish();
				HPModify.this.onDestroy();
			}
		});

		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 자동 생성된 메소드 스텁

				if (idflag == 1) {
					etid.setText("");
					etid.requestFocus();
					etid.setEnabled(true);
					//btn5.setText("중복확인");
					
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
						// TODO 자동 생성된 메소드 스텁
						try {

							baos = new ByteArrayOutputStream();
							out = new ObjectOutputStream(baos);

							HPObject hp = new HPObject(etid.getText()
									.toString(), null, null, null, null, null,
									null, null, null, null, null,"idcheck");

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
							// TODO 자동 생성된 catch 블록
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
				mtoast = Toast
						.makeText(HPModify.this, "네트워크 상태가 원활하지 않습니다.", 0);
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
					//btn5.setText("중복확인해제");
					
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
						mtoast = Toast.makeText(HPModify.this, "중복되는 아이디 입니다.",
								0);
						mtoast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						mtoast.show();
					}
				}
				break;
			case 1:
				btn3.setEnabled(true);
				mtoast = Toast.makeText(HPModify.this, "회원정보가 수정되었습니다!", 0);
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
					//btn2.setText("연락처 변경");
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
						mtoast = Toast.makeText(HPModify.this,
								"인증이 불가능한 연락처입니다.", 0);
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
		// TODO 자동 생성된 메소드 스텁
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
			toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
