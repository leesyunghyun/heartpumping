package com.example.heartpumping;

import java.lang.reflect.Field;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class HPFont extends Application{
	Context context;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
		initDefaultTypeface();
	}
	
	private void initDefaultTypeface()
	{
		try{
		Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/cherry.ttf");
		final Field field = Typeface.class.getDeclaredField("DEFAULT");
		field.setAccessible(true);
		field.set(null, typeface);
		}
		catch(Exception e)
		{
			
		}
	}
}
