package com.example.heartpumping;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class HPDatabase extends SQLiteOpenHelper{

	public HPDatabase(Context context) {
		super(context, Environment.getExternalStorageDirectory().getPath() + "/HpData/heartpumping",null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists personal(id TEXT PRIMARY KEY, saveid INTEGER, autologin INTEGER, searchnic INTEGER, loginid TEXT, loginpw TEXT, tema INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
