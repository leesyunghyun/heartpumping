package com.example.heartpumping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HPSetTema extends Activity{

	LinearLayout li;
	
	HPDatabase hpdb;
	SQLiteDatabase sql;
	Cursor c;
	
	Button btn1;
	Gallery gallery;
	
	Intent result;
	int temaflag = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpsettema);
		btn1 = (Button) findViewById(R.id.btntemachange);
		li = (LinearLayout)findViewById(R.id.lihpsettema);
		gallery = (Gallery)findViewById(R.id.gallery1);
		
		MyGalleryAdapter adapter = new MyGalleryAdapter(this);
		gallery.setAdapter(adapter);
		hpdb = new HPDatabase(HPSetTema.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);
		
		if(c.moveToFirst())
		{
			switch(c.getInt(6))
			{
			case 0:
				li.setBackgroundResource(R.drawable.tema_blue);
				btn1.setBackgroundResource(R.drawable.btnapply_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.tema_pupple);
				btn1.setBackgroundResource(R.drawable.btnapply_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.tema_green);
				btn1.setBackgroundResource(R.drawable.btnapply_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.tema_red);
				btn1.setBackgroundResource(R.drawable.btnapply_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.tema_gold);
				btn1.setBackgroundResource(R.drawable.btnapply_gold);
				break;
			}
		}
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dlg = new AlertDialog.Builder(HPSetTema.this);
				dlg.setTitle("알림");
				dlg.setMessage("이 테마를 적용하시겠습니까?");
				dlg.setPositiveButton("예", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sql.execSQL("update personal set tema =" + temaflag + " where id='set';");
						result = new Intent();
						result.putExtra("flag", temaflag);
						setResult(0, result);
						finish();
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

	public class MyGalleryAdapter extends BaseAdapter {

		Context context;
		Integer[] teamId = { R.drawable.example_blue,R.drawable.example_pupple, R.drawable.example_green, R.drawable.example_red, R.drawable.example_gold};
		
		public MyGalleryAdapter(Context c)
		{
			context = c;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return teamId.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView image = new ImageView(context);
			image.setLayoutParams(new Gallery.LayoutParams(300, 400));
			image.setScaleType(ImageView.ScaleType.FIT_CENTER);
			image.setPadding(5,5,5,5);
			image.setImageResource(teamId[position]);
			
			final int pos = position;
			
			image.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					ImageView iv = (ImageView)findViewById(R.id.temaiv1);
					iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setImageResource(teamId[pos]);
					temaflag = pos;
					return false;
				}
			});			
			return image;
		}
		
	}
	
}
